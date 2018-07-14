package mx.com.hugoflores.appnoticias;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Noticias extends AppCompatActivity implements View.OnClickListener {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);
        listView=(ListView) findViewById(R.id.lvNoticiasA);
        ObDatos();
    }

    @Override
    public void onClick(View v) {

    }
    public void CargaLista(ArrayList<String> datos){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,datos);
        listView.setAdapter(adapter);
    }

    public void ObDatos(){


                AsyncHttpClient client = new AsyncHttpClient();
        String url ="http://192.168.0.103:8080/android/noticias/noticias.php";

        RequestParams params = new RequestParams();
        params.put("post_title", "ITSOEH");



        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    CargaLista(obtDatosJSON(new String(responseBody)));
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
                texto = jsonArray.getJSONObject(i).getString("post_title")+" "+jsonArray.getJSONObject(i).getString("post")+" ";
                listado.add(texto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  listado;
    }
}
