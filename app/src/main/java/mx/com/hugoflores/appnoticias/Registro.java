package mx.com.hugoflores.appnoticias;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    EditText email, userName, password,password2;
    Button registrar;
    Spinner municipio;
    ProgressBar load;
    String[] contenido = {"Seleccione Municipio", "Actopan", "Ajacuba","Alfajayucan","" +
            "El Arenal","Atitalaquia","Atotonilco de Tula","Chapantongo","Chicuautla",
            "Francisco I. Madero", "Ixmiquilpan","Mixquiahuala","Progreso de Obregón",
            "San Agustín Tlaxiaca","Santiago de Anaya","Tecozautla", "Tepejí delRío",
            "Tepetitlan", "Tetepango","Tezontepec de Aldama","Tlahuelilpan","Tlaxcoapan", "Tula de Allende"};
    Direccion dir = new Direccion();

    private String muni=null;
    //URL=http://localhost:8080/android/noticias/register.php?email=email@gmail.com&name=jjhhj&last_name=j&user=hg&password=123&password2=123&type=reportero&town=tlahue
    //HttpURLConnection="192.168.0.101:8080/android/noticias/" + "register.php?email=email@gmail.com&name=jjhhj&last_name=j&user=hg&password=123&password2=123&type=reportero&town=tlahue";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        email = (EditText) findViewById(R.id.etEmail);
        userName = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);
        password2 = (EditText)findViewById(R.id.etPassword2);
        registrar = (Button) findViewById(R.id.btRegistrarReportero);
        registrar.setOnClickListener(this);
        municipio = (Spinner) findViewById(R.id.rSpMunicipio);



        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, contenido);
        municipio.setAdapter(adaptador);

        municipio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                muni=contenido[position];
                Toast.makeText(getApplicationContext(),muni,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /*tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Toast.makeText(getApplicationContext(),contenido[position],Toast.LENGTH_SHORT).show();
                        medio.setVisibility(View.GONE);
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(),contenido[position],Toast.LENGTH_SHORT).show();
                        medio.setVisibility(View.GONE);
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(),contenido[position],Toast.LENGTH_SHORT).show();
                        medio.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        load =(ProgressBar) findViewById(R.id.rePrbLoad);




    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btRegistrarReportero:
                registrar.setVisibility(View.GONE);
                load.setVisibility(View.VISIBLE);
                if (password.getText().toString().equals(password2.getText().toString())){
                    registrar();
                }else{
                    load.setVisibility(View.GONE);
                    registrar.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    public void  registrar(){
        AsyncHttpClient client = new AsyncHttpClient();
        //String url= "http://192.168.43.204:8080/android/noticias/register.php";
        String url= dir.getUrl()+"register.php";
        RequestParams requestParams = new RequestParams();
        requestParams.put("user",userName.getText().toString());
        requestParams.put("password",password.getText().toString());
        requestParams.put("email",email.getText().toString());
        requestParams.put("town",muni);
        //Toast.makeText(getApplicationContext(),requestParams.toString(),Toast.LENGTH_SHORT).show();

        RequestHandle post = client.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode==200){
                    Toast.makeText(getApplicationContext(),"Usuario Registrado Exitosamente",Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(),Login.class));
                    finish();
                }else{
                    load.setVisibility(View.GONE);
                    registrar.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Error al registrase, Intentelo más tarde",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                load.setVisibility(View.GONE);
                registrar.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"Error al registrase, Intentelo más tarde",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
