package mx.com.hugoflores.appnoticias;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Editar extends AppCompatActivity implements View.OnClickListener{

    private Button btnPublicar;
    private ProgressBar load;
    Button btnAnImage;
    Spinner spGenero;
    String [] Genero={"Social", "Cultural", "Deportiva", "Policiaca"};
    ArrayAdapter<String> adaptador;
    EditText etRedactar, etTitulo, etLugar;
    Direccion dir = new Direccion();
    private int id;
    private String lugar;
    private Configuracion conf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        etRedactar = (EditText) findViewById(R.id.edTxtRedactar);
        etTitulo = (EditText)findViewById(R.id.edTxtTitulo);
        btnPublicar = (Button) findViewById(R.id.edBtnEditar);
        etLugar= (EditText) findViewById(R.id.edTxtLugar);
        btnPublicar.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        lugar= bundle.getString("titulo");
        conf = new Configuracion(this);
        load = (ProgressBar) findViewById(R.id.edPrbLoad);
        load.setVisibility(View.VISIBLE);
        ObDatos();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edBtnEditar:
                load.setVisibility(View.VISIBLE);
                actualizar();
                break;
        }
    }
    public void actualizar(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url= dir.getUrl()+"update.php";
        RequestParams requestParams = new RequestParams();
        requestParams.put("title",etTitulo.getText().toString());
        requestParams.put("news",etRedactar.getText().toString());
        requestParams.put("town",etLugar.getText().toString());
        requestParams.put("id",id);
        Toast.makeText(getApplicationContext(),requestParams.toString(),Toast.LENGTH_SHORT).show();

        RequestHandle post = client.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode==200){
                    Toast.makeText(getApplicationContext(),"Noticia Actualizada",Toast.LENGTH_SHORT).show();
                    load.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }else{
                    load.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Error al Actualizar, Intentelo más tarde",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                load.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Error al actualizar, Intentelo más tarde",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void ObDatos(){
        AsyncHttpClient client = new AsyncHttpClient();
        // String url ="http://192.168.1.149:8080/android/noticias/noticias.php";
        String url= dir.getUrl()+"obdateditar.php";
        RequestParams params = new RequestParams();
        params.put("titulo",lugar);
        params.put("id",conf.getUserEmail());

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    load.setVisibility(View.GONE);
                    obtDatosJSON(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                load.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Error acargar noticia, Intentelo más tarde",Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void obtDatosJSON(String response){
        //ArrayList<ModeloVista> listado = new ArrayList<ModeloVista>();
        try{
            JSONArray jsonArray = new JSONArray(response);
           // String titulo,lugar,reportero;
           // String imagenJ;
            for (int i=0; i<jsonArray.length(); i++){
                //texto = jsonArray.getJSONObject(i).getString("post_title")+" "+jsonArray.getJSONObject(i).getString("post")+" ";
                etTitulo.setText( jsonArray.getJSONObject(i).getString("post_title"));
                etRedactar.setText( jsonArray.getJSONObject(i).getString("post"));
                etLugar.setText(jsonArray.getJSONObject(i).getString("town"));
                id= jsonArray.getJSONObject(i).getInt("id_post");
                //listado.add(new ModeloVista(titulo,fecha,reportero,imagenJ));


            }
        }catch (Exception e){
            e.printStackTrace();
        }
       // return  listado;
    }

}
