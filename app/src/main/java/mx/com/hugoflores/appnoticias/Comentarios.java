package mx.com.hugoflores.appnoticias;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Comentarios extends AppCompatActivity  implements View.OnClickListener{

    private ListView listView;
    private Button comentar;
    private EditText comentario;
    private ProgressBar load;
    private ProgressBar etLoad;
    private Direccion dir = new Direccion();
    private Configuracion conf;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);
        listView = (ListView) findViewById(R.id.cmLvComentarios);
        comentar = (Button) findViewById(R.id.cmBtnComentar);
        comentar.setOnClickListener(this);
        comentario = (EditText) findViewById(R.id.CoEtComentario);
        Bundle bundle = getIntent().getExtras();
        id= bundle.getInt("id");
        conf = new Configuracion(this);
        load =(ProgressBar) findViewById(R.id.cmPrbLoad);
        load.setVisibility(View.VISIBLE);
        etLoad = (ProgressBar) findViewById(R.id.cmEtPrgLoad);

        ObDatos();

//        titulo.setText(bundle.get("Titulo").toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cmBtnComentar:
                etLoad.setVisibility(View.VISIBLE);
                publicar();
                break;
        }
    }

    public void CargaLista(ArrayList<String> datos){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,datos);
        listView.setAdapter(adapter);
    }

    public void ObDatos(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url= dir.getUrl()+"comentario.php";

        RequestParams params = new RequestParams("id",id);
        //params.put("post_title","ITSOEH");

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    CargaLista(obtDatosJSON(new String(responseBody)));
                    load.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }
    public ArrayList<String> obtDatosJSON(String response){
        ArrayList<String> listado = new ArrayList<String>();
        try{
            JSONArray jsonArray = new JSONArray(response);
            String texto;
            for (int i=0; i<jsonArray.length(); i++){
                texto = jsonArray.getJSONObject(i).getString("comment");
                listado.add(texto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  listado;
    }
    private void publicar(){
        AsyncHttpClient client = new AsyncHttpClient();
        //String url= "http://192.168.43.204:8080/android/noticias/register.php";
        String url= dir.getUrl()+"agregarcomentario.php";
        RequestParams requestParams = new RequestParams();
        requestParams.put("comentario",comentario.getText().toString());
        requestParams.put("id",id);
        requestParams.put("user",conf.getUserEmail());
        //Toast.makeText(getApplicationContext(),requestParams.toString(),Toast.LENGTH_SHORT).show();

        RequestHandle post = client.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode==200){
                    etLoad.setVisibility(View.GONE);
                    comentario.setText("");

                   ObDatos();
                }else{
                    Toast.makeText(getApplicationContext(),"Error al comentar, \n Intentelo más tarde",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                etLoad.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Error,Verifique su conexion a Internet",Toast.LENGTH_SHORT).show();
            }
        });
    }



}
