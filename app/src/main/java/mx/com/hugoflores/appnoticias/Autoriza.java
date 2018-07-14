package mx.com.hugoflores.appnoticias;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

public class Autoriza extends AppCompatActivity implements View.OnClickListener{

    private TextView suceso;
    private TextView lugar;
    private Button autoriza;
    private Button denegar;
    private  Button reportar;
    private ProgressBar load;
    private LinearLayout cont;

    private String pSuceso;

    Direccion dir = new Direccion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoriza);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        suceso = (TextView) findViewById(R.id.auTvSuceso);
        lugar = (TextView) findViewById(R.id.auTvUbicacion);
        autoriza = (Button) findViewById(R.id.auBtPublizar);
        denegar = (Button) findViewById(R.id.aubtDescartar);
        reportar= (Button) findViewById(R.id.auBTReportar);
        load = (ProgressBar) findViewById(R.id.auPbLoad);
        cont = (LinearLayout) findViewById(R.id.auLyCont);

        autoriza.setOnClickListener(this);
        denegar.setOnClickListener(this);
        reportar.setOnClickListener(this);


        Bundle b = getIntent().getExtras();
        pSuceso= b.getString("a");
        ObDatos();
    }

    public void ObDatos(){
        AsyncHttpClient client = new AsyncHttpClient();
        // String url ="http://192.168.1.149:8080/android/noticias/noticias.php";
        String url= dir.getUrl()+"consultasuceso.php";
        RequestParams params = new RequestParams();
        params.put("suceso",pSuceso);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    load.setVisibility(View.GONE);
                    cont.setVisibility(View.VISIBLE);
                    obtDatosJSON(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                load.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error en la red \nVerifique su conexion a internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void obtDatosJSON(String response){
        try{
            JSONArray jsonArray = new JSONArray(response);

            for (int i=0; i<jsonArray.length(); i++){
                //texto= jsonArray.getJSONObject(i).getString("peticion");
                suceso.setText( jsonArray.getJSONObject(i).getString("peticion"));
                lugar.setText(jsonArray.getJSONObject(i).getString("lugar"));

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.auBtPublizar:
                autorizar(1);
                preguntar();
                break;
            case R.id.aubtDescartar:
                autorizar(2);
                break;
            case R.id.auBTReportar:
                autorizar(3);
                break;
        }
    }

    private void preguntar() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Autoriza.this);
        alertDialog.setMessage("¿Desea conservar La descripcción de la noticia?");
        alertDialog.setTitle("Guardar información");
        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                startActivity(new Intent(getApplicationContext(),publicarReportero.class)
                        .putExtra("suceso", suceso.getText().toString()));
            }

        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), "La nota no ha sido eliminada", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),publicarReportero.class));
                return;
            }
        });
        alertDialog.show();
    }

    private void descartar() {

    }

    private void reportarM() {

    }

    public void autorizar(int cod){
        AsyncHttpClient client = new AsyncHttpClient();
        // String url ="http://192.168.1.149:8080/android/noticias/noticias.php";
        String url= dir.getUrl()+"estado_peticion.php";
        RequestParams params = new RequestParams();
        params.put("par",cod);
        params.put("id",suceso);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    load.setVisibility(View.GONE);
                    cont.setVisibility(View.VISIBLE);
                    obtDatosJSON(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                load.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error en la red \nVerifique su conexion a internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
