package ConjuntoDatos;

/**
 * Created by javier on 27/11/17.
 */

public class Datos
{
    private int _Id;
    private String Titulo;
    private String Autor;
    private String Resumen;
    private String Enlace;
    private String Imagen;
    private String Fecha;
    private int Descargado;

    public Datos()
    {
        _Id = 0;
        Titulo = "";
        Autor = "";
        Resumen = "";
        Enlace = "";
        Imagen = "";
        Fecha = "";
        Descargado = 0;
    }

    public Datos(String titulo, String autor, String resumen, String enlace,
                 String imagen, String fecha, int descargado)
    {
        Titulo = titulo;
        Autor = autor;
        Resumen = resumen;
        Enlace = enlace;
        Imagen = imagen;
        Fecha = fecha;
        Descargado = descargado;
    }

    public int get_Id() {
        return _Id;
    }

    public void set_Id(int _Id) {
        this._Id = _Id;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getAutor() {
        return Autor;
    }

    public void setAutor(String autor) {
        Autor = autor;
    }

    public String getResumen() {
        return Resumen;
    }

    public void setResumen(String resumen) {
        Resumen = resumen;
    }

    public String getEnlace() {
        return Enlace;
    }

    public void setEnlace(String enlace) {
        Enlace = enlace;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getFecha() { return Fecha; }

    public void setFecha(String fecha) { Fecha = fecha;  }

    public int getDescargado() {  return Descargado; }

    public void setDescargado(int descargado) { Descargado = descargado; }

}
