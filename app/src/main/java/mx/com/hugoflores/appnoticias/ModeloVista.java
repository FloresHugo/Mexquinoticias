package mx.com.hugoflores.appnoticias;

/**
 * Created by Hugo Flores on 25/02/2017.
 */

public class ModeloVista {
    private String titulo;
    private String fecha;
    private String reportero;
    private String imagen;
    private String id;

    public ModeloVista(String titulo, String fecha, String reportero, String imagen, String id) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.reportero = reportero;
        this.imagen = imagen;
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getReportero() {
        return reportero;
    }

    public void setReportero(String reportero) {
        this.reportero = reportero;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
