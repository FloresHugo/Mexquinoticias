package mx.com.hugoflores.appnoticias;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.VersionInfo;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AgregarNoticiasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AgregarNoticiasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgregarNoticiasFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

   // private static final int RESULT_SELECT_IMAGE=1;
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
    private static final  int MY_PERMISSIONS= 100;
    private static final  int PHOTO_CODE=200;
    private static final  int SELECT_PICTURE=2;
    private boolean flag=false;

    double aleatorio = new Double(Math.random() * 100).intValue();
    String foto = Environment.getExternalStorageDirectory() + "/imagen/"+ System.currentTimeMillis() +".jpg";
    File image ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Configuracion conf;

    public AgregarNoticiasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgregarNoticiasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgregarNoticiasFragment newInstance(String param1, String param2) {
        AgregarNoticiasFragment fragment = new AgregarNoticiasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=  inflater.inflate(R.layout.fragment_agregar_noticias, container, false);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        etRedactar = (EditText) view.findViewById(R.id.txtRedactar);
        etTitulo = (EditText)view.findViewById(R.id.txtTitulo);
        btnPublicar = (Button) view.findViewById(R.id.btnPublicar);
        btnAnImage = (Button) view.findViewById(R.id.btnAnImage);
        //etTown = (EditText) view.findViewById(R.id.txtLugar);
        btnPublicar.setOnClickListener(this);
        btnAnImage.setOnClickListener(this);
        load = (ProgressBar) view.findViewById(R.id.anfPrbLoad);





        spGenero = (Spinner) view.findViewById(R.id.spGenero);

        //spGenero.setOnItemSelectedListener(this);

        adaptador=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Genero);

        spGenero.setAdapter(adaptador);

        lugar = (Spinner) view.findViewById(R.id.fanSpLugar);
        adapterLugar = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,  aLugar);
        lugar.setAdapter(adapterLugar);
        lugar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                opcLugar = aLugar[position];
                Toast.makeText(getContext(),opcLugar,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        conf = new Configuracion(view.getContext());
       /* if (mayRequestStoragePermission()){
            mOptio
        }*/
        return view ;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPublicar:
                Toast.makeText(getActivity(),foto,Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getContext(),"Camara No disponible",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri output = Uri.fromFile(new File(foto));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
                    startActivityForResult(intent, TAKE_PICTURE);
                }

                break;
        }
    }


    public void opciones(){
        final CharSequence[] option ={"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opcion");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (option[which]=="Tomar Foto"){
                    /*foto = Environment.getExternalStorageDirectory() + "/imagen/"+ System.currentTimeMillis() +".jpg";
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //Uri output = Uri.fromFile(new File(foto));
                    File newFile = new  File(foto);
                    Uri output = FileProvider.getUriForFile(getActivity(),BuildConfig.APPLICATION_ID+".provider",newFile);
                   //Uri output = FileProvider.getUriForFile(getActivity(),"mx.com.hugoflores.appnoticias",new File(foto));
                    //intent.setData(output);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
                    startActivityForResult(intent, TAKE_PICTURE);*/
                    tomarFoto();
                }else if(option[which]=="Elegir de Galeria"){
                    Intent intent = new Intent();
//                    Uri salida = Uri.fromFile(new File (foto));
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent.createChooser(intent,"Selecciona una opción"),SELECT_PICTURE);
                }
                else{
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void tomarFoto(){
        String fileName= "mexquinoticias"+System.currentTimeMillis()+".jpg";
        File imagePath = new File(getContext().getFilesDir(),"imagen");
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

                    Toast.makeText(getActivity(),"Noticia Publicada",Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(getActivity(),Noticias.class));
                    sendNotification();
                    startActivity(new Intent(getActivity(),MainActivity.class));
                }else{
                    load.setVisibility(View.GONE);
                    btnPublicar.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Error al publicar noticia", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                load.setVisibility(View.GONE);
                btnPublicar.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Error en la red,\n Verifica tu conexión a internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }
    /*private void seleccionarImagen(){
        //abrir galeria
        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galeria,RESULT_SELECT_IMAGE);
    }*/

     @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
         super.onActivityResult(requestCode,resultCode,data);
         if (resultCode== getActivity().RESULT_OK){
             switch (requestCode){
                 case TAKE_PICTURE:
                     image = new File(foto);

                     //Toast.makeText(getActivity(),image.toString(),Toast.LENGTH_LONG).show();
                     //System.out.println(image);
                     break;
                 case SELECT_PICTURE:
                     Uri path= data.getData();

                     foto= path.toString();
                     //Toast.makeText(getContext(),path.toString(),Toast.LENGTH_LONG).show();
                    // System.out.println(foto);
                     Log.i("Arachivo","Archivo : "+path);
                     image = new File(foto);
                     //System.out.println(image);
                     //break;
             }
             Toast.makeText(getActivity(),image.toString(),Toast.LENGTH_LONG).show();
         }
         //if(requestCode==RESULT_SELECT_IMAGE && resultCode == 1 && data!=null){
            // Uri ima = data.getData();
            // image = new File(ima.getPath());
             //image = data.getP

         }
        public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
            cursor.close();
        return cursor.getString(column_index);
    }

    private  void sendNotification(){
        //String id = conf.getUserEmail();
        String url= dir.getUrl()+"push_notification.php";
        RequestParams params = new RequestParams();
        params.put("titulo",etTitulo.getText().toString());
        params.put("email", conf.getUserEmail());
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // System.out.println("statusCode "+statusCode);//statusCode 200
                if (statusCode==200){

                    Toast.makeText(getActivity(),"Noticacion enviada",Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(getActivity(),Noticias.class));

                    startActivity(new Intent(getActivity(),MainActivity.class));
                }else{
                    load.setVisibility(View.GONE);
                    btnPublicar.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                sendNotification();
                Toast.makeText(getContext(), "Error Notificacion", Toast.LENGTH_SHORT).show();
            }
        });
    }




}

