package mx.com.hugoflores.appnoticias;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Hugo Flores on 17/04/2017.
 */

public class Token {

    private final String SHARED_PREFS_FILE = "com.google.android.gms.appid";
    private final String TOKEN = "|T|931766730597|*";

    private Context mContext;

    public Token(Context context){
        mContext = context;
    }

    private SharedPreferences getSettings(){
        return mContext.getSharedPreferences(SHARED_PREFS_FILE, 0);
    }

    public String getTOKEN() {
        return getSettings().getString(TOKEN,null);
    }
}
