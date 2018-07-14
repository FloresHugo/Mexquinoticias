package mx.com.hugoflores.appnoticias;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Hugo Flores on 26/03/2017.
 */

public class Configuracion {

    private final String SHARED_PREFS_FILE = "HMPrefs";
    private final String KEY_EMAIL = "email";
    private final String KEY_USER_NAME="user_name";
    private final String KEY_PASSWORD="password";
    private final String KEY_TIPO="tipo";
    private Context mContext;





    public Configuracion(Context context){
        mContext = context;
    }

    private SharedPreferences getSettings(){
        return mContext.getSharedPreferences(SHARED_PREFS_FILE, 0);
    }

    public String getUserEmail(){
        return getSettings().getString(KEY_EMAIL, null);
    }
    public String getUser(){
        return  getSettings().getString(KEY_USER_NAME,null);
    }
    public String getPass(){
        return getSettings().getString(KEY_PASSWORD,null);
    }
    public String getTipo() {return  getSettings().getString(KEY_TIPO,null);}


    public void setUserEmail(String email){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_EMAIL, email );
        editor.commit();
    }
    public void setUser(String user){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_USER_NAME,user);
        editor.commit();
    }
    public void setPass(String pass){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_PASSWORD,pass);
        editor.commit();
    }

    public void eliminarDatos(String email, String user, String pass){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.clear();
        editor.commit();
    }

    public void setTipo(String tipo){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_TIPO,tipo);
        editor.commit();
    }



}
