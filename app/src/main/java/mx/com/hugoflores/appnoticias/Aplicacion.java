package mx.com.hugoflores.appnoticias;

import android.app.Application;

/**
 * Created by Hugo Flores on 28/01/2017.
 */

public class Aplicacion extends Application {
    private String usuario;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
