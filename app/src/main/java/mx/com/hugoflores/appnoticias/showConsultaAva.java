package mx.com.hugoflores.appnoticias;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class showConsultaAva extends AppCompatActivity {

    TextView textView, a,b;
    ListView listView;
    String Titulo ="";
    String Municipio = "";
    String Fecha = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_consulta_ava);
        /*textView = (TextView) findViewById(R.id.Titulo);
        a=(TextView) findViewById(R.id.Fecha);
        b=(TextView) findViewById(R.id.Municipo);*/

        listView = (ListView) findViewById(R.id.lvConsultaAva);


        Titulo = getIntent().getExtras().getString("Titulo");
        Municipio = getIntent().getExtras().getString("Municipio");
        Fecha = getIntent().getExtras().getString("Fecha");

        /*textView.setText(Titulo);
        a.setText(Fecha);
        b.setText(Municipio);*/
        ObDatos();

    }

    public void CargaLista(ArrayList<String> datos){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,datos);
        listView.setAdapter(adapter);
    }

    public void ObDatos(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url ="http://192.168.0.105:8080/android/noticias/consultaAva.php";

        RequestParams params = new RequestParams();
        params.put("titulo","ITSOEH");
        params.put("fecha","");
        params.put("municipio","");

        //System.out.println(params);
        Toast.makeText(getApplicationContext(),params.toString(),Toast.LENGTH_LONG).show();


        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {

                    CargaLista(obtDatosJSON(new String(responseBody)));
                    Toast.makeText(getApplicationContext(),"Listo",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(),"Error: "+ error,Toast.LENGTH_SHORT).show();
            }
        });


    }
    public ArrayList<String> obtDatosJSON(String response){
        ArrayList<String> listado = new ArrayList<String>();
        try{
            JSONArray jsonArray = new JSONArray(response);
            String texto;
            for (int i=0; i<jsonArray.length(); i++){
                texto = jsonArray.getJSONObject(i).getString("post_title")+" "+jsonArray.getJSONObject(i).getString("post")+" ";
                listado.add(texto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  listado;
    }

}
