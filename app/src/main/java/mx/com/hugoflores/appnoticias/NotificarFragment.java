package mx.com.hugoflores.appnoticias;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificarFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NotificarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificarFragment newInstance(String param1, String param2) {
        NotificarFragment fragment = new NotificarFragment();
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
    private EditText etPaso;
    private EditText etRedactar;
    private Button imagen;
    private Button notificar;
    private ProgressBar load;

    Direccion dir = new Direccion();

    String foto = Environment.getExternalStorageDirectory() + "/imagen/"+ System.currentTimeMillis() +".jpg";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notificar, container, false);
        etPaso = (EditText) view.findViewById(R.id.ntEdSuceso);
        etRedactar = (EditText) view.findViewById(R.id.ntEdLugar);
        imagen = (Button) view.findViewById(R.id.ntBtImagen);
        notificar = (Button) view.findViewById(R.id.ntBtNotificar);
        load = (ProgressBar) view.findViewById(R.id.ntPbLoad);
        notificar.setOnClickListener(this);

         etRedactar.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                 notificar.setEnabled(false);
             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etRedactar.getText().toString().length()>10){
                    notificar.setEnabled(true);
                }else{{
                    notificar.setEnabled(false);
                }}
             }

             @Override
             public void afterTextChanged(Editable s) {
                 notificar.setEnabled(true);
             }
         });

        return view;
    }

    //TODO: Atributos

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ntBtNotificar:
                Toast.makeText(getContext(),"TAP",Toast.LENGTH_SHORT).show();
                notificar(foto);
                load.setVisibility(View.VISIBLE);
                break;
        }
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

    public  void notificar(String ImageLink){
        String url= dir.getUrl()+"peticion.php";
        RequestParams params = new RequestParams();
        try {
            params.put("suceso",etPaso.getText().toString());
            params.put("ubicacion",etRedactar.getText().toString());
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

                    Toast.makeText(getActivity(),"Notificacion Enviada",Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(getActivity(),Noticias.class));
                    sendNotification();

                    startActivity(new Intent(getActivity(),MainActivity.class));
                }else{
                    load.setVisibility(View.GONE);
                    //btnPublicar.setVisibility(View.VISIBLE);

                    Toast.makeText(getContext(), "Error al publicar noticia", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                load.setVisibility(View.GONE);
                //btnPublicar.setVisibility(View.VISIBLE);
                sendNotification();
                startActivity(new Intent(getActivity(),MainActivity.class));
                Toast.makeText(getContext(), "Error en la red,\n Verifica tu conexión a internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendNotification(){
        String url= dir.getUrl()+"push_notification_reportero.php";
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // System.out.println("statusCode "+statusCode);//statusCode 200
                if (statusCode==200){

                    Toast.makeText(getActivity(),"Notificacion Enviada",Toast.LENGTH_SHORT).show();

                }else{

                    Toast.makeText(getContext(), "Error al publicar noticia", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                load.setVisibility(View.GONE);
                //btnPublicar.setVisibility(View.VISIBLE);

                Toast.makeText(getContext(), "Error en la red,\n Verifica tu conexión a internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
