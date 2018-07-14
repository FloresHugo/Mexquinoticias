package mx.com.hugoflores.appnoticias;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoticiasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NoticiasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoticiasFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ListView listView=null ;
    private ProgressBar load;
    private ArrayList<ModeloVista> arrayItem=null;
    private Adapter adapterP=null;
    Direccion dir = new Direccion();
    Token toke ;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NoticiasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoticiasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoticiasFragment newInstance(String param1, String param2) {
        NoticiasFragment fragment = new NoticiasFragment();
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
        getActivity().setTitle("Noticias");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noticias, container, false);
        listView = (ListView) view.findViewById(R.id.lvNoticiasFr);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView titulo = (TextView) view.findViewById(R.id.lpTvTitulo);
                TextView idView = (TextView) view.findViewById(R.id.lpTvID);
                String text = titulo.getText().toString();
                String idnoticia = idView.getText().toString();
                //Toast.makeText(getContext(),"Seleccionaste un item"+ idnoticia+text,Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(),Noticias_Deatalles.class);
                i.putExtra("Titulo",text);
                i.putExtra("idnoticia",idnoticia);
                startActivity(i);
                Log.d("ID_Noticia", idnoticia);
            }
        });

        load = (ProgressBar) view.findViewById(R.id.fnLoad);
        load.setVisibility(View.VISIBLE);



        ObDatos();

        // Inflate the layout for this fragment
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


    public void CargaLista(ArrayList<ModeloVista> datos){
        //ArrayAdapter<ModeloVista> adapter = new ArrayAdapter<ModeloVista>(getActivity(),android.R.layout.simple_list_item_1,datos);
        //listView.setAdapter(adapter);
        adapterP = new Adapter(getActivity(),datos);
        listView.setAdapter(adapterP);

    }

    public void ObDatos(){
        AsyncHttpClient client = new AsyncHttpClient();
       // String url ="http://192.168.1.149:8080/android/noticias/noticias.php";
        String url= dir.getUrl()+"index";
        RequestParams params = new RequestParams();
        params.put("post_title","ITSOEH");

        /*client.post(url, params, new AsyncHttpResponseHandler() {
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
        });*/
        client.get(url, params, new AsyncHttpResponseHandler() {
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
            String titulo,fecha,reportero,id;
            String imagenJ;

            for (int i=0; i<jsonArray.length(); i++){
                //texto = jsonArray.getJSONObject(i).getString("post_title")+" "+jsonArray.getJSONObject(i).getString("post")+" ";
                titulo = jsonArray.getJSONObject(i).getString("title");
                fecha = jsonArray.getJSONObject(i).getString("created_at");
                reportero =jsonArray.getJSONObject(i).getString("name");
                imagenJ = jsonArray.getJSONObject(i).getString("image");
                id = jsonArray.getJSONObject(i).getString("id");
                listado.add(new ModeloVista(titulo,fecha,reportero,imagenJ,id));
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.d("STATUS", e.getMessage());
        }
        return  listado;
    }
}

