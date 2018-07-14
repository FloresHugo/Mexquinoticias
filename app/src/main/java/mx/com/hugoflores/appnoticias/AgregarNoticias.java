package mx.com.hugoflores.appnoticias;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class AgregarNoticias extends AppCompatActivity  implements View.OnClickListener{


    Button btnPublicar;
    Spinner spGenero;
    String [] Genero={"Social", "Cultural", "Deportiva", "Policiaca"};
    ArrayAdapter<String> adaptador;
    EditText etRedactar, etTitulo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_noticias);

        etRedactar = (EditText) findViewById(R.id.txtRedactar);
        etTitulo = (EditText)findViewById(R.id.txtTitulo);
        btnPublicar = (Button) findViewById(R.id.btnPublicar);
        btnPublicar.setOnClickListener(this);

        spGenero = (Spinner) findViewById(R.id.spGenero);

        //spGenero.setOnItemSelectedListener(this);

        adaptador=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Genero);

        spGenero.setAdapter(adaptador);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPublicar:
                AsyncHttpClient client = new AsyncHttpClient();
                String url= "http://192.168.0.105:8080/android/noticias/publica.php";
                RequestParams requestParams = new RequestParams();
                requestParams.put("title",etTitulo.getText().toString());
                requestParams.put("news",etRedactar.getText().toString());
                Toast.makeText(getApplicationContext(),requestParams.toString(),Toast.LENGTH_SHORT).show();

                RequestHandle post = client.post(url, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (statusCode==200){
                            Toast.makeText(getApplicationContext(),"Noticia Publicada",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Noticias.class));
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"Error al registrase, Intentelo más tarde",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
                break;
        }
    }
}
