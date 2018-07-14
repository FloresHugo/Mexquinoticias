package mx.com.hugoflores.appnoticias;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, BuscarFragment.OnFragmentInteractionListener,
                    ConsultaAvanzadaFragment.OnFragmentInteractionListener, NoticiasFragment.OnFragmentInteractionListener,
                    AgregarNoticiasFragment.OnFragmentInteractionListener, PerfilFragmen.OnFragmentInteractionListener,
                    NotificarFragment.OnFragmentInteractionListener, PeticionesFragment.OnFragmentInteractionListener
                     {


    private long pulsacionBackInicial = 0;
    private long pulsacionBackActual = 0;
    private int tiempoEnMilisegundos = 2000; // = 2 segundos;

    private boolean valor = true;


    private Configuracion conf;
    private Direccion dir = new Direccion();
    private Token token;

     Fragment fragment =null;
     Boolean FracgmentSeleccionado = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        conf = new Configuracion(this);
        token= new Token(this);


        String menuFragment = getIntent().getStringExtra("menuFragment");



        // If menuFragment is defined, then this activity was launched with a fragment selection
        if (menuFragment != null) {

            // Here we can decide what do to -- perhaps load other parameters from the intent extras such as IDs, etc
            if (menuFragment.equals("favoritesMenuItem")) {
                displaySelectedScreen(R.id.nav_peticion);
            }
        } else {
            // Activity was not launched with a menuFragment selected -- continue as if this activity was opened from a launcher (for example)
            displaySelectedScreen(R.id.nav_news);
        }



       // usuario = app.getUsuario().toString();

        //Toast.makeText(this,conf.getTipo(),Toast.LENGTH_LONG).show();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //user = (TextView) findViewById(R.id.UserSesion);

/*        if(conf.getTipo().equals("reportero")){
            navigationView.getMenu().getItem(3).setVisible(false);

        }else{
            navigationView.getMenu().getItem(2).setVisible(false);
            navigationView.getMenu().getItem(4).setVisible(false);
        }*/

       // registerToken(getToken(token.getTOKEN()));
    }


    public void registerToken(String token) {

        String id = conf.getUserEmail();


        AsyncHttpClient client = new AsyncHttpClient();
        //String url ="http://eslikmania.esy.es/registerToken.php";
        String url =dir.getUrl()+"registerToken.php";

        RequestParams params = new RequestParams();
        params.put("Token",token);
        params.put("id",id);
        params.put("tipo",conf.getTipo());

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                   // Toast.makeText(getApplicationContext(),"Registrado",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    public String getToken(String token){
        String[] datos;
        String Tokend=null;
        datos = token.split("\"");
        String[] datosFinal = new String[datos.length];
        for (int i = 0; i < datos.length - 1; i++) {
            if (datos[i].length()==152){
                Tokend=datos[i];
                break;
            }
            //datosFinal[i] = (datos[i]);
        }
        return Tokend;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        pulsacionBackActual = System.currentTimeMillis();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (pulsacionBackActual - pulsacionBackInicial > tiempoEnMilisegundos) {

            pulsacionBackInicial = pulsacionBackActual;
            Toast.makeText(this, "Pulsa de nuevo para salir",Toast.LENGTH_SHORT).show();
        } else if(FracgmentSeleccionado){
            displaySelectedScreen(R.id.nav_noti);
        }else {
        /* Acciones a realizar cuando salgamos.
           Siempre llamar al método súper.*/
            finish();
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView= (android.support.v.widget.SearchView)  item.getActionView();

       // user.setText(app.getUsuario());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private void displaySelectedScreen (int id){


        Fragment fragment =null;
        Boolean FracgmentSeleccionado = false;
        if (id == R.id.nav_news) {
            // Handle the camera action
           // fragment = new ConsultaAvanzadaFragment();
            fragment = new NoticiasFragment();
            FracgmentSeleccionado=true;
        } else if (id == R.id.nav_noti) {
            fragment = new NotificarFragment();
            FracgmentSeleccionado=true;
        } else if (id == R.id.nav_peticion) {
            fragment = new PeticionesFragment();
            FracgmentSeleccionado=true;
        } /*else if (id == R.id.nav_manage) {
            fragment = new Perfil1Fragment();
            FracgmentSeleccionado=true;

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/else if (id == R.id.nav_publicar){
            fragment = new AgregarNoticiasFragment();
            FracgmentSeleccionado=true;
        }
        else if (id == R.id.nav_profile){
            fragment = new PerfilFragmen();
            FracgmentSeleccionado=true;
        }
        else if(id == R.id.nav_close){
            Toast.makeText(getApplicationContext(),"Cerrando Sesión",Toast.LENGTH_SHORT).show();
            cerrarSesion();
        }

        if  (FracgmentSeleccionado){
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.UserSesion:
                //startActivity(new Intent(getApplication(),Inicio.class));
                break;*/
        }
    }


    public void cerrarSesion(){
        AsyncHttpClient client = new AsyncHttpClient();
        //String url ="http://eslikmania.esy.es/cerrarsesion.php";
        String url = dir.getUrl()+"cerrarsesion.php";

        RequestParams params = new RequestParams();
        params.put("email",conf.getUserEmail());

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    Toast.makeText(getApplicationContext(),"Sesion Cerrada",Toast.LENGTH_SHORT).show();
                    //conf.setUserEmail(null);
                    //conf.setUser(null);
                    //conf.setPass(null);
                    conf.eliminarDatos(conf.getUserEmail(),conf.getUser(),conf.getPass());
                    startActivity(new Intent(getApplicationContext(),Login.class));
                    finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    //@Override
    /*public void onClick(View v) {
        switch ( v.getId()){
            case R.id.btConsultaMa:
                startActivity(new Intent(getApplicationContext(),ConsultaAva.class));
                break;
            case R.id.btNoticas:
                startActivity(new Intent(getApplicationContext(),Noticias.class));
                break;
            case R.id.btAgregarNoti:
                startActivity(new Intent(getApplicationContext(),AgregarNoticias.class));
                break;
            default:
                break;
        }
    }*/

   /* public void CargaLista(ArrayList<String> datos){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,datos);
        listView.setAdapter(adapter);
    }

    public void ObDatos(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url ="http://192.168.0.100:8080/android/noticias/noticias.php";

        RequestParams params = new RequestParams();
        params.put("post_title","ITSOEH");

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
    }*/

}
