package mx.com.hugoflores.appnoticias;

import android.app.Service;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Hugo Flores on 14/04/2017.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    Configuracion conf = new Configuracion(this);
    MainActivity m = new MainActivity();
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        //m.registerToken(token);
        /*if (conf.getUser()!=null){

            registerToken(token);
        }*/


    }

   /* private void registerToken(String token) {

        String id = conf.getUserEmail();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token",token)
                .add("id", id)
                .build();

        Request request = new Request.Builder()
                .url("http://eslikmania.esy.es/registerToken.php")
                .post(body)
                .build();
        try {
            client.newCall(request).execute();
        }catch (IOException e){
            e.printStackTrace();
        }

    }*/
}
