package mx.com.hugoflores.appnoticias;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class segunda extends AppCompatActivity {

    TextView tvNombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);
        tvNombre = (TextView) findViewById(R.id.tvNombreS);
        Aplicacion app = (Aplicacion)getApplicationContext();
        tvNombre.setText(app.getUsuario());
    }

   /* private SharedPreferences appSettings =
            getSharedPreferences(Configuracion.SHARED_SETTINGS_NAME,
                    Context.MODE_PRIVATE);

    private void salvarDatos(Integer idUsuario, String nombre){
//nos ponemos a editar las preferencias
        SharedPreferences.Editor editor = appSettings.edit();
        editor.putString(&amp;quot;nombreUsuario&amp;quot;, nombre);
        editor.putInt(&amp;quot;idUsuario&amp;quot;, idUsuario);
// quizás para adelante, chequear por timeout
        editor.putLong(&amp;quot;currentTimestamp&amp;quot;, System.currentTimeMillis());
// Commit la edición
        editor.commit();
    }

    private void borrarDatos(){
//editamos nuevamente las preferencias
        SharedPreferences.Editor editor = appSettings.edit();
// removemos los datos agregados
        editor.remove(&amp;quot;nombreUsuario&amp;quot;);
        editor.remove(&amp;quot;idUsuario&amp;quot;);
// quizás para adelante, chequear por timeout
        editor.remove(&amp;quot;currentTimestamp&amp;quot;);
// Commit la edición
        editor.commit();
    }

    // finalmente chequeamos si esta logueado o hay que pedirle que se loguee.
// al iniciar la app luego de un splash logo o algo asi: startActivity(crearIntent());
    private Intent crearIntent(){
//le pedimos a la configuración que nos de el id del usuario
// si no lo encuentra, por omisión -1
        Integer idUsuario = appSettings.getInt(&amp;quot;idUsuario&amp;quot;,-1);
        if(idUsuario&amp;gt;1){
            return getPantallaPrincipalIntent();
        }
        return getLoginIntent();
    }*/
}
