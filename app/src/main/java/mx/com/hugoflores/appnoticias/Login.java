package mx.com.hugoflores.appnoticias;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button btLogin, verToken;
    EditText etUserName, etPassword;
    ProgressBar load1;
   // ImageView load;
    private Button registrar;

    private String usuario = null;
    private String tipo =null;

    Direccion dir = new Direccion();
    private Configuracion conf;
    private Token sToken;

   // AnimationDrawable loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserName = (EditText) findViewById(R.id.etUserL);
        etPassword = (EditText) findViewById(R.id.etPasswordL);
        btLogin = (Button) findViewById(R.id.btLogin);
        /*token = (TextView) findViewById(R.id.ttoken);
        verToken = (Button) findViewById(R.id.verToken);*/
//        verToken.setOnClickListener(this);
        btLogin.setOnClickListener(this);
        conf = new Configuracion(this);
        sToken = new Token(this);
        if (conf.getUser() != null && conf.getPass() != null) {
            etUserName.setText(conf.getUser());
            etPassword.setText(conf.getPass());
        }
       // load = (ImageView) findViewById(R.id.lgLoad);
        load1 = (ProgressBar) findViewById(R.id.lgPrbLogin) ;
        load1.setVisibility(View.GONE);
        registrar = (Button) findViewById(R.id.lgBtRegistrar);
        registrar.setOnClickListener(this);
        /*load.setVisibility(View.GONE);
        load.setBackgroundResource(R.drawable.loading);*/


    }




    @Override
    public void onClick(View v) {
//        loading = (AnimationDrawable) load.getBackground();
        switch (v.getId()) {
            case R.id.btLogin:
                etUserName.setEnabled(false);
                etPassword.setEnabled(false);
                btLogin.setEnabled(false);
                /*startActivity(new Intent(this, MainActivity.class));
                finish();*/
                //load1.setVisibility(View.VISIBLE);
                //load.setVisibility(View.VISIBLE);
               // loading.start();



                AsyncHttpClient client = new AsyncHttpClient();
                //String url= "http://192.168.1.149:8080/android/noticias/login.php";
                String url = dir.getUrl() + "auth";
                RequestParams requestParams = new RequestParams();
                requestParams.add("email", etUserName.getText().toString());
                requestParams.add("password", etPassword.getText().toString());

                RequestHandle post = client.post(url, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        if (statusCode == 200) {
                            try {
                                JSONObject o = new JSONObject(new String(responseBody));
                                usuario = o.getString("login");
                                //tipo = o.getString("tipo");
                                conf.setUserEmail(usuario);
                                if (!TextUtils.isEmpty(usuario)) {
                                    //Aplicacion app = (Aplicacion) getApplicationContext();
                                    //app.setUsuario(usuario);
                                    /*Usuario user = new Usuario();
                                    user.setEmail(usuario);*/
                                    conf.setUser(etUserName.getText().toString());
                                    conf.setPass(etPassword.getText().toString());
                                    conf.setTipo(tipo);
                                    //startActivity(new Intent(getBaseContext(),MainActivity.class)) .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));;
                                  //  load.setVisibility(View.GONE);
                                   // loading.stop();

                                   // FirebaseMessaging.getInstance().subscribeToTopic("noticia");
                                    //FirebaseInstanceId.getInstance().getToken();





                                    load1.setVisibility(View.GONE);

                                    Toast.makeText(getApplicationContext(),tipo,Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    finish();
                                } else {
                                    etUserName.setEnabled(true);
                                    etPassword.setEnabled(true);
                                    btLogin.setEnabled(true);
                                    /*load.setVisibility(View.GONE);
                                    loading.stop();*/
                                    load1.setVisibility(View.GONE);
                                    Toast.makeText(Login.this, "Error al Iniciar Sesion", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {

                            }


                        } else {
                            etUserName.setEnabled(true);
                            etPassword.setEnabled(true);
                            btLogin.setEnabled(true);
                            load1.setVisibility(View.GONE);
                            Toast.makeText(Login.this, "Tiempo de respuesta Execedido", Toast.LENGTH_SHORT).show();
                            Toast.makeText(Login.this, "Verifique su conexion a internet", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        etUserName.setEnabled(true);
                        etPassword.setEnabled(true);
                        btLogin.setEnabled(true);
                        load1.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Tiempo de respuesta Execedido", Toast.LENGTH_SHORT).show();
                        Toast.makeText(Login.this, "Verifique su conexion a internet", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.lgBtRegistrar:
                startActivity(new Intent(this,Registro.class));
                break;

            default:
                break;
        }


    }//https://www.youporn.com/watch/9291493/fake-hospital-stiff-neck-followed-by-a-big-stiff-cock-as-fucked-on-doctors-desk/


}

  /*  public class Loading extends AsyncTask<Void, Integer, Void>{

        int progreso;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progreso =0;
            load.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            while (progreso<=200){
                progreso++;
                publishProgress(progreso);
                SystemClock.sleep(20);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            load.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            load.setVisibility(View.GONE);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


}*/
