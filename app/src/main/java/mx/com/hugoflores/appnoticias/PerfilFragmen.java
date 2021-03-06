package mx.com.hugoflores.appnoticias;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PerfilFragmen.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PerfilFragmen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragmen extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private  String user;

    private ListView listView=null ;
    private ArrayList<ModeloVista> arrayItem=null;
    private Adapter adapterP=null;

    private ProgressBar load;

    private Configuracion conf;
    private String texto;
    Direccion dir = new Direccion();

    TextView titulo;
    //manager = new DataBaseManager(this);


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PerfilFragmen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragmen.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragmen newInstance(String param1, String param2) {
        PerfilFragmen fragment = new PerfilFragmen();
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
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        listView = (ListView) view.findViewById(R.id.pLvPerfil);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 TextView titulo = (TextView) view.findViewById(R.id.lpTvTitulo);

                texto = titulo.getText().toString();
            }
        });
        /*listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TextView titulo = (TextView) v.findViewById(R.id.lpTvTitulo);
                texto = titulo.getText().toString();
                return false;
            }
        });*/

        //Toast.makeText(getContext(), user,Toast.LENGTH_LONG).show();
        conf = new Configuracion(view.getContext());
        user= conf.getUserEmail();
        //registrar los controles del menu
        registerForContextMenu(listView);
        setHasOptionsMenu(true);
        load = (ProgressBar) view.findViewById(R.id.pPrbLoad);
        load.setVisibility(View.VISIBLE);

        ObDatos();
        return view;
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();

        inflater.inflate(R.menu.menu_item,menu);

        //texto=listView.getTextFilter().toString();
        //listView.getId();


       titulo = (TextView) v.findViewById(R.id.lpTvTitulo);


    }

    /*@Override
    public boolean nCreateOptionsMenu(Menu menu){
        getActivity().getMenuInflater().inflate(R.menu.menu_item,menu);
       return  true;

    }*/
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        //String f= this.arrayItem.get((int)info.id).getTitulo();
        switch (item.getItemId()){
            case R.id.mnIEditar:
                editar();

               /* Toast.makeText(getContext(),"Seleccionaste el elemento "+
                        info.position+" opcion del menú y titulo "+texto+
                        item.getTitle(),Toast.LENGTH_SHORT ).show();*/

                return true;
            case R.id.mnIEliminar:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setMessage("¿Desea eliminar la noticia?");
                alertDialog.setTitle("Eliminar Noticia");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //load.setVisibility(View.VISIBLE);
                        eliminar();
                        //Toast.makeText(getContext(), "La nota ha sido eliminada", Toast.LENGTH_SHORT).show();
                       /* manager.eliminar(texto);
                        Toast.makeText(getApplicationContext(), "La nota ha sido eliminada", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();*/
                    }

                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "La nota no ha sido eliminada", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
                alertDialog.show();
                /*Toast.makeText(getContext(),"Seleccionaste el elemento "+
                        info.id+" opcion del menú "+
                        item.getTitle(),Toast.LENGTH_SHORT ).show();*/
                return true;
            default:
                return super.onContextItemSelected(item);
                //return super.onContextItemSelected(item);
        }


    }

    public void CargaLista(ArrayList<ModeloVista> datos){
        //ArrayAdapter<ModeloVista> adapter = new ArrayAdapter<ModeloVista>(getActivity(),android.R.layout.simple_list_item_1,datos);
        //listView.setAdapter(adapter);
        adapterP = new Adapter(getActivity(),datos);
        listView.setAdapter(adapterP);

    }

    public void ObDatos(){
        AsyncHttpClient client = new AsyncHttpClient();
        //String url ="http://192.168.43.204:8080/android/noticias/profile.php";
        String url= dir.getUrl()+"profile.php";

        //String perfil = us.getEmail();
        RequestParams params = new RequestParams("user",user);
        //params.put("user",user);

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    load.setVisibility(View.GONE);
                    CargaLista(obtDatosJSON(new String(responseBody)));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                load.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error en la red \nVerifique su conexion a internet", Toast.LENGTH_SHORT).show();
            }
        });


    }
    public ArrayList<ModeloVista> obtDatosJSON(String response){
        ArrayList<ModeloVista> listado = new ArrayList<ModeloVista>();
        try{
            JSONArray jsonArray = new JSONArray(response);
            String titulo,fecha,reportero;
            String imagen;
            for (int i=0; i<jsonArray.length(); i++){
                //texto = jsonArray.getJSONObject(i).getString("post_title")+" "+jsonArray.getJSONObject(i).getString("post")+" ";
                titulo = jsonArray.getJSONObject(i).getString("post_title");
                fecha = jsonArray.getJSONObject(i).getString("post_date");
                reportero = jsonArray.getJSONObject(i).getString("email");
                imagen = jsonArray.getJSONObject(i).getString("post_image");
                listado.add(new ModeloVista(titulo,fecha,reportero,imagen,"1"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  listado;
    }

    public void editar(){
        Intent intent = new Intent(getContext(),Editar.class);
        intent.putExtra("titulo", texto);
        startActivity(intent);
    }
    public void eliminar(){
        AsyncHttpClient client = new AsyncHttpClient();
        // String url ="http://192.168.1.149:8080/android/noticias/noticias.php";
        String url= dir.getUrl()+"delete.php";
        RequestParams params = new RequestParams();
        params.put("title",texto);
        params.put("id",conf.getUserEmail());

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    load.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Noticia Eliminada",Toast.LENGTH_SHORT).show();
                    ObDatos();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                load.setVisibility(View.GONE);
                Toast.makeText(getContext(),"Error al Eliminar la Noticia",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
