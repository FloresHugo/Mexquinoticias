package mx.com.hugoflores.appnoticias;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
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
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class publicarReportero extends AppCompatActivity implements View.OnClickListener {

    private static final int TAKE_PICTURE=1;

    Button btnPublicar;
    Button btnAnImage;
    private ProgressBar load;
    Spinner spGenero;
    Spinner lugar;
    private String [] Genero={"Social", "Cultural", "Deportiva", "Policiaca"};
    private String [] aLugar={"Seleccione Municipio","Actopan","Ajacuba","Atitalaquia","Mixquiahuala","Progreso",
            "Tetepango","Tzontepec","Tlahuelilpan","Tlaxcoapan","Tula"};
    ArrayAdapter<String> adaptador;
    ArrayAdapter<String> adapterLugar;
    EditText etRedactar, etTitulo, etTown;

    private String opcLugar;
    
    Direccion dir = new Direccion();
    private Configuracion conf;
    
    private static final  int MY_PERMISSIONS= 100;
    private static final  int PHOTO_CODE=200;
    private static final  int SELECT_PICTURE=2;
    private boolean flag=false;

    double aleatorio = new Double(Math.random() * 100).intValue();
    String foto = Environment.getExternalStorageDirectory() + "/imagen/"+ System.currentTimeMillis() +".jpg";
    File image ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_reportero);

        etRedactar = (EditText) findViewById(R.id.txtRedactar);
        etTitulo = (EditText)findViewById(R.id.txtTitulo);
        btnPublicar = (Button) findViewById(R.id.btnPublicar);
        btnAnImage = (Button) findViewById(R.id.btnAnImage);
        //etTown = (EditText) findViewById(R.id.txtLugar);
        btnPublicar.setOnClickListener(this);
        btnAnImage.setOnClickListener(this);
        load = (ProgressBar) findViewById(R.id.anfPrbLoad);





        spGenero = (Spinner) findViewById(R.id.spGenero);

        //spGenero.setOnItemSelectedListener(this);

        adaptador=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, Genero);

        spGenero.setAdapter(adaptador);

        lugar = (Spinner) findViewById(R.id.fanSpLugar);
        adapterLugar = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,  aLugar);
        lugar.setAdapter(adapterLugar);
        lugar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//AdapterVi ew .OnItemSelectedListener()
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                opcLugar = aLugar[position];
                Toast.makeText(getApplicationContext(),opcLugar,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        conf = new Configuracion(getApplicationContext());

        String suceso = getIntent().getStringExtra("suceso");

        if (suceso!=null){
            etRedactar.setText(suceso);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPublicar:
                Toast.makeText(getApplicationContext(),foto,Toast.LENGTH_LONG).show();
                btnPublicar.setVisibility(View.GONE);
                load.setVisibility(View.VISIBLE);

                publicar(foto);



                break;
            case R.id.btnAnImage:
                //seleccionarImagen();
                if (shouldAskPermissions()) {
                    askPermissions();

                }
                /*String fileName= "mexquinoticias"+System.currentTimeMillis()+".jpg";
                File imagePath = new File(Environment.getExternalStorageDirectory(),"imagen");
                File file = new File(imagePath,fileName);
                File file1= new File(foto);
                Intent camara = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                final Uri outputUri = FileProvider.getUriForFile(getActivity(),BuildConfig.APPLICATION_ID+".provider",file);
                camara.putExtra(MediaStore.EXTRA_OUTPUT,outputUri);
                getContext().grantUriPermission(
                        "com.google.android.GoogleCamera",
                        outputUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getActivity().startActivityForResult(camara,TAKE_PICTURE);

                //opciones();*/

                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    Toast.makeText(getApplicationContext(),"Camara No disponible",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri output = Uri.fromFile(new File(foto));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
                    startActivityForResult(intent, TAKE_PICTURE);
                }

                break;
        }
    }
    public  void publicar(String ImageLink){
        String url= dir.getUrl()+"publica.php";
        RequestParams params = new RequestParams();
        try {
            params.put("title",etTitulo.getText().toString());
            params.put("news",etRedactar.getText().toString());
            params.put("town",opcLugar);
            //requestParams.put("image", image.getAbsolutePath());
            params.put("user",conf.getUserEmail());
            params.put("imagen", new File(ImageLink));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // System.out.println("statusCode "+statusCode);//statusCode 200
                if (statusCode==200){

                    Toast.makeText(getApplicationContext(),"Noticia Publicada",Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(getActivity(),Noticias.class));

                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }else{
                    load.setVisibility(View.GONE);
                    btnPublicar.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Error al publicar noticia", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                load.setVisibility(View.GONE);
                btnPublicar.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Error en la red,\n Verifica tu conexión a internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }
}
