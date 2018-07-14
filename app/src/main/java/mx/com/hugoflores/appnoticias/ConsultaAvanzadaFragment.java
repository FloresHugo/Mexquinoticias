package mx.com.hugoflores.appnoticias;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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
 * {@link ConsultaAvanzadaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConsultaAvanzadaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultaAvanzadaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView listView;

    String [] Ciudades={"TV noticias", "Progreso" , "Acayuclan"};
    Spinner sp_ciudades,sp_marca;
    String[] marcas;

    private OnFragmentInteractionListener mListener;

    public ConsultaAvanzadaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConsultaAvanzadaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultaAvanzadaFragment newInstance(String param1, String param2) {
        ConsultaAvanzadaFragment fragment = new ConsultaAvanzadaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        //ObDatos();etView()

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //listView = (ListView) getActivity().findViewById(R.id.lvNoticias) ;
        sp_ciudades = (Spinner) getActivity().findViewById(R.id.sp_ciudades);
        sp_marca = (Spinner) getActivity().findViewById(R.id.sp_marca);
        marcas= getResources().getStringArray(R.array.marcas_auto);

        //CargaCiudades();
        //CargaMarcas();
       // RegistraAdaptadores();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_consulta_avanzada, container, false);
        //ObDatos();

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

    public void CargaCiudades(){
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,Ciudades);
        sp_ciudades.setAdapter(adaptador);
    }



    public void RegistraAdaptadores(){
        sp_ciudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent , View view, int position, long id){
                Toast.makeText(getActivity(),Ciudades[position],Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){
                Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
            }
        });

        sp_marca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent , View view, int position, long id){
                Toast.makeText(getActivity(),marcas[position],Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){
                Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
            }
        });



    }


    public void mostrarOnClick(View v){
        String seleccion = sp_ciudades.getSelectedItem().toString();
        Toast.makeText(this.getActivity(),seleccion,Toast.LENGTH_SHORT).show();
    }

    public void CargaMarcas(){
        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(this.getActivity(),R.array.marcas_auto, android.R.layout.simple_spinner_item);
        sp_marca.setAdapter(adaptador);
    }


}
