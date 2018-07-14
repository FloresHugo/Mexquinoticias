package mx.com.hugoflores.appnoticias;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.snowdream.android.widget.SmartImage;
import com.github.snowdream.android.widget.SmartImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Noticias_Deatalles extends AppCompatActivity {

    private TextView titulo, noticia;
    private ImageView img;
    private SmartImageView imagen;
    private ProgressBar load;
    private int id;
    private String dat, idnoticia;

    Direccion dir = new Direccion();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias__deatalles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                       .setAction("Action", null).show();*/
                Intent i = new Intent(getApplicationContext(),Comentarios.class);

                i.putExtra("id",id);
                startActivity(i);
            }
        });

        titulo =(TextView) findViewById(R.id.ndTvTitulo);
        noticia = (TextView) findViewById(R.id.ndTvNoticia);
        Bundle bundle = getIntent().getExtras();
        //titulo.setText(bundle.get("Titulo").toString());
        dat=bundle.get("Titulo").toString();
        idnoticia= bundle.get("idnoticia").toString();
        Toast.makeText(this,dat.toString(),Toast.LENGTH_SHORT).show();
        imagen = (SmartImageView) findViewById(R.id.ndSImagen);


        load = (ProgressBar) findViewById(R.id.ndPrbLoad);






        ObDatos();
    }

    public void ObDatos(){


        AsyncHttpClient client = new AsyncHttpClient();
        //String url ="http://192.168.1.149:8080/android/noticias/detalle_noticia.php";
        //String url= dir.getUrl()+"detalle_noticia.php";
        String url= dir.getUrl()+"shownews/"+idnoticia;
        RequestParams params = new RequestParams("titulo",dat);
        //params.put("post_title", "ITSOEH");
        Log.d("URL",url);

       // client.post(url, params, new AsyncHttpResponseHandler() {
        client.get(this,url, new  AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    load.setVisibility(View.GONE);
                    obtDatosJSON(new String(responseBody));
                    Log.d("STATUS","SI");
                    //Toast.makeText(getApplicationContext(),String.valueOf(id),Toast.LENGTH_SHORT).show();

                }else{
                    load.setVisibility(View.GONE);
                    Log.d("STATUS","NO");
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
        //ArrayList<String> listado = new ArrayList<String>();

        try{
            JSONArray jsonArray = new JSONArray(response);
            String tituloJ="";
            String noticiaJ="";
            String imagenJ="";
            for (int i=0; i<jsonArray.length(); i++){
                tituloJ = jsonArray.getJSONObject(i).getString("title");
                noticiaJ = jsonArray.getJSONObject(i).getString("content");
                imagenJ= jsonArray.getJSONObject(i).getString("image");
                id = jsonArray.getJSONObject(i).getInt("id");
            }
            titulo.setText(tituloJ);
            noticia.setText(noticiaJ);
            Rect rect = new Rect(imagen.getLeft(),imagen.getTop(),imagen.getRight(),imagen.getBottom());
            imagen.setImageUrl(imagenJ,rect);
           /* listado.add(titulo);
            listado.add(noticia);*/

        }catch (Exception e){
            e.printStackTrace();
        }
        //return  listado;
    }



}
