package mx.com.hugoflores.appnoticias;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends Activity {

    private Configuracion conf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        conf = new Configuracion(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (conf.getUser() != null && conf.getPass() != null) {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(SplashScreen.this,Login.class));
                    finish();
                }

            }
        },4000);


    }
}
