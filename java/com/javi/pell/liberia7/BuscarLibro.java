package com.javi.pell.liberia7;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ConjuntoDatos.Adaptador;
import ConjuntoDatos.Datos;
import ConjuntoDatos.SQLite_OpenHelper;

public class BuscarLibro extends AppCompatActivity {

    EditText tituloBuscar;
    Button btnBuscar;

    ListView mListView;
    Adaptador miAdaptador;
    int posicion = -1;
    Datos d;
    SQLiteDatabase db ;
    String enlaceOculto;
    FloatingActionButton fab;

    TextView titulo;
    TextView autor;
    TextView imagen;
    TextView resumen;
    TextView enlace;

    String tmpTitulo ="";
    String tmpAutor ="";
    String tmpResumen ="";
    String tmpImagen ="";
    String tmpEnlace ="";

    List<Datos> mDatosArrayList = new ArrayList<>();
    SQLite_OpenHelper helper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_libro);

        mListView = (ListView) findViewById(R.id.listView);

        tituloBuscar = (EditText) findViewById(R.id.editTextTitulo);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);
        titulo = (TextView) findViewById(R.id.textViewTitulo);
        autor = (TextView) findViewById(R.id.textViewAutor);
        //imagen = (TextView) findViewById(R.id.textViewImagen);

        resumen = (TextView) findViewById(R.id.textViewResumen);
        enlace = (TextView) findViewById(R.id.textViewEnlace);
        //btnAñadir = (Button) findViewById(R.id.btnAñadir);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(tituloBuscar.getWindowToken(), 0);
                Toast.makeText(getApplicationContext(), "Buscando titulo(s) ... ",Toast.LENGTH_SHORT).show();
                mDatosArrayList.clear();
                getWebSiteVarios2();
                getWebSiteVarios();

            }
        });

        // Cómo descargar archivos en la Web
        // https://firebase.google.com/docs/storage/web/download-files?hl=es-419
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Buscando titulo "+ mDatosArrayList.get(i).getTitulo()
                        ,Toast.LENGTH_SHORT).show();
                posicion = i;
                String queWeb = mDatosArrayList.get(i).getImagen().toString();
                if ( queWeb.indexOf("espamobi")!=-1 )
                {
                    getWebSite();
                }else {
                    getWebSite2();
                }
                if (mDatosArrayList.size()>0)
                {
                    fab.show();
                }else {
                    fab.hide();
                }

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (mDatosArrayList.size()>0)
        {
            fab.show();
        }else {
            fab.hide();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Snackbar.make(view, "Guardando libro ... ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                guardarNuevo2();
                Intent intento = new Intent(BuscarLibro.this, MainActivity.class);
                startActivity(intento);
                finish();
                /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(tituloBuscar.getWindowToken(), 0);
                //getWebSite();
                getWebSiteVarios();
                Toast.makeText(getApplicationContext(), "Buscando titulo ",Toast.LENGTH_SHORT).show();*/
            }
        });
    }

    public void guardarNuevo2()
    {
        helper = new SQLite_OpenHelper(this,
                "DbLibros",null,1);
        db = helper.getReadableDatabase();
        helper.abir();
        helper.insertarRegistro2(db, d);
        helper.cerrar();
        String cadena ="";
        cadena += '"' + d.getTitulo().trim().toString() + '"' + ";;";
        cadena += '"' + d.getAutor().trim().toString() + '"' + ";;";
        cadena += '"' + d.getResumen().trim().toString() + '"' + ";;";
        cadena += '"' + d.getEnlace().trim().toString() + '"' + ";;";
        cadena += '"' + d.getImagen().trim().toString() + '"' + ";;";
        cadena += '"' + d.getFecha().trim().toString() + '"' + ";;";
        cadena += '"' + "0" + '"' + ";;";
        System.out.println("cadena "+cadena);
        Lector lector = new Lector();
        //lector.guardarNuevoLibro_(cadena);

        lector.WriteFileDemo(cadena);

    }

    public void guardarNuevo()
    {
        Toast.makeText(getApplicationContext(), "registros "+ mDatosArrayList.size(), Toast.LENGTH_SHORT).show();
        if (mDatosArrayList.get(0).getTitulo()!=null &&
                mDatosArrayList.get(0).getAutor()!=null &&
                mDatosArrayList.get(0).getResumen()!=null &&
                mDatosArrayList.get(0).getEnlace()!=null &&
                mDatosArrayList.get(0).getImagen()!=null &&
                mDatosArrayList.get(0).getFecha()!=null )
        {
            helper = new SQLite_OpenHelper(this,
                    "DbLibros",null,1);
            helper.abir();
            helper.insertarRegistro(mDatosArrayList.get(0).getTitulo(),
                    mDatosArrayList.get(0).getAutor(),
                    mDatosArrayList.get(0).getResumen(),
                    mDatosArrayList.get(0).getEnlace(),
                    mDatosArrayList.get(0).getImagen(),
                    mDatosArrayList.get(0).getFecha(),
                    0 );
            helper.cerrar();
            String cadena ="";
            cadena += '"' + mDatosArrayList.get(0).getTitulo().trim().toString() + '"' + ";;";
            cadena += '"' + mDatosArrayList.get(0).getAutor().trim().toString() + '"' + ";;";
            cadena += '"' + mDatosArrayList.get(0).getResumen().trim().toString() + '"' + ";;";
            cadena += '"' + mDatosArrayList.get(0).getEnlace().trim().toString() + '"' + ";;";
            cadena += '"' + mDatosArrayList.get(0).getImagen().trim().toString() + '"' + ";;";
            cadena += '"' + mDatosArrayList.get(0).getFecha().trim().toString() + '"' + ";;";
            cadena += '"' + "0" + '"' + ";;";
            System.out.println("cadena "+cadena);
            Lector lector = new Lector();
            //lector.guardarNuevoLibro_(cadena);

            lector.WriteFileDemo(cadena);

        }
    }

    public void getWebSiteVarios()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Document doc = null;

                String url = "http://espamobi.com/books/search/";
                String tituloUrl = tituloBuscar.getText().toString();
                tituloUrl = tituloUrl.replace(" ", "%20");
                url = url + tituloUrl;

                try {
                    doc = Jsoup.connect(url).get();
                    tmpTitulo = tituloBuscar.getText().toString();
                    Elements formElement = doc.getElementsByClass("cover");
                    for (Element elemento : formElement) {
                        tmpTitulo = elemento.attr("alt");
                        tmpAutor = "";
                        tmpResumen = "";
                        tmpImagen = elemento.attr("src");
                        tmpEnlace = elemento.attr("href");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        String fechaComoCadena = sdf.format(new Date());

                        Datos d = new Datos(tmpTitulo, tmpAutor, tmpResumen, tmpEnlace, tmpImagen, fechaComoCadena, 0);
                        mDatosArrayList.add(d);

                    }
                } catch (Exception ex) {

                }

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        miAdaptador = new Adaptador(BuscarLibro.this, mDatosArrayList);
                        mListView.setAdapter(miAdaptador);
                    }
                });
            }
        }).start();
    }

    public void getWebSiteVarios2()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Document doc = null;

                String url = "http://www.lectulandia.com/search/";
                int contador = 0;

                String tituloUrl = tituloBuscar.getText().toString();
                tituloUrl = tituloUrl.replace(" ", "+");
                String donde = url + tituloUrl;

                try {
                    doc = Jsoup.connect(donde).get();
                    tmpTitulo = tituloBuscar.getText().toString();

                    Elements enlaces = doc. getElementsByClass("card-click-target");
                    int cuantos = enlaces.size();
                    String losEnlaces[] = new String [cuantos];

                    for (Element enlace : enlaces)
                    {
                        losEnlaces[contador] = enlace.attr("href");
                        contador++;
                    }
                    contador = 0;
                    Elements formElement = doc.getElementsByClass("cover");
                    for (Element elemento : formElement) {

                        tmpAutor = "";
                        tmpResumen = "";
                        tmpImagen = "https:"+elemento.attr("src");
                        tmpTitulo = elemento.attr("alt");
                        //tmpEnlace = url + tmpTitulo.replace(" ","-");
                        tmpEnlace = losEnlaces[contador];
                        contador++;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        String fechaComoCadena = sdf.format(new Date());

                        Datos d = new Datos(tmpTitulo, tmpAutor, tmpResumen, tmpEnlace, tmpImagen, fechaComoCadena, 0);
                        mDatosArrayList.add(d);

                    }
                } catch (Exception ex) {

                }

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        miAdaptador = new Adaptador(BuscarLibro.this, mDatosArrayList);
                        mListView.setAdapter(miAdaptador);
                    }
                });
            }
        }).start();
    }
    public void getWebSite()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Document doc = null;

                String url = "http://espamobi.com/book/";
                String tituloUrl = tituloBuscar.getText().toString();
                tituloUrl = mDatosArrayList.get(posicion).getTitulo().toString();
                tituloUrl = limpiarAcentos(tituloUrl);
                tituloUrl = tituloUrl.replace(" ","-");
                url = url + tituloUrl;

                try
                {
                    doc = Jsoup.connect(url).get();
                    if (posicion==-1)
                    {
                        tmpTitulo = tituloBuscar.getText().toString();
                    }
                    else
                    {
                        tmpTitulo = tituloUrl.replace("-"," ");
                    }

                    Elements formElement = doc.getElementsByClass("autor");
                    System.out.println(formElement.text());
                    tmpAutor = formElement.text();

                    Elements imagenes = doc.getElementsByClass("cover");

                    for (Element imagen1:imagenes)
                    {
                        tmpImagen = imagen1.attr("src");
                    }

                    Elements resumen1 = doc.getElementsByClass("sinopsis");
                    tmpResumen = resumen1.text();

                    Element enlace1 = doc.getElementById("redirectLink");
                    tmpEnlace = enlace1.attr("href");

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    String fechaComoCadena = sdf.format(new Date());

                    mDatosArrayList.clear();

                    d = new Datos(tmpTitulo, tmpAutor, tmpResumen, tmpEnlace, tmpImagen, fechaComoCadena,0);
                    mDatosArrayList.add(d);
                } catch (IOException e)
                {
                    titulo = tituloBuscar;
                    autor.setText(e.getMessage());
                }

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        miAdaptador = new Adaptador(BuscarLibro.this ,mDatosArrayList);
                        mListView.setAdapter(miAdaptador);
                       ;
                    }
                });
            }
        }).start();
    }

    public void getWebSite2()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Document doc = null;

                String url = "https://www.lectulandia.com/";
                String donde = "book/";
                String tituloUrl = tituloBuscar.getText().toString();

                /*tituloUrl = mDatosArrayList.get(posicion).getTitulo().toString();
                tituloUrl = limpiarAcentos(tituloUrl);
                tituloUrl = tituloUrl.replace(" ","-");
                donde = url +donde+ tituloUrl;
                */
                donde = mDatosArrayList.get(posicion).getEnlace().toString();

                try {
                    doc = Jsoup.connect(donde).get();
                    Elements resumen = doc.getElementsByClass("ali_justi");
                    if (resumen.size()>0) {
                        for (Element cell : resumen) {
                            //tmpResumen = cell.attr("span");
                            tmpResumen = cell.text();
                        }
                    }else{
                        Elements resumen2 = doc.getElementById("sinopsis").getElementsByTag("p");
                        for (Element cell:resumen2)
                        {
                            tmpResumen = cell.text();
                        }
                    }
                    Element enlace = doc. getElementById("downloadContainer");
                    Elements enlaces = enlace.getElementsByTag("a");
                    for (Element enla : enlaces)
                    {
                        tmpEnlace = url + enla.attr("href");
                        break;
                    }

                    Element portada = doc.getElementById("cover");
                    Elements portadas = portada.getElementsByTag("img");
                    for (Element cell : portadas)
                    {
                        tmpImagen = "https:" + cell.attr("src");
                        tmpTitulo = cell.attr("title");
                    }

                    Element autor = doc.getElementById("autor");
                    Elements cells = autor.getElementsByTag("rel");
                    for (Element cell : cells)
                    {
                        //tmpAutor = cell.attr("tag");
                        tmpAutor = cell.text();
                        System.out.println("el autor "+ tmpAutor);
                        //Toast.makeText(getApplicationContext(),"el autor "+ tmpAutor, Toast.LENGTH_SHORT).show();
                    }

                    enlaceOculto = tmpEnlace;
                    //getWebOculto();
                    System.out.println("enlace "+ tmpEnlace);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    String fechaComoCadena = sdf.format(new Date());

                    mDatosArrayList.clear();

                    d = new Datos(tmpTitulo, tmpAutor, tmpResumen, tmpEnlace, tmpImagen, fechaComoCadena,0);
                    mDatosArrayList.add(d);

                } catch (Exception ex) {

                }

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        miAdaptador = new Adaptador(BuscarLibro.this ,mDatosArrayList);
                        mListView.setAdapter(miAdaptador);
                        ;
                    }
                });
            }
        }).start();
    }

    public void getWebOculto()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Document doc = null;

                try {
                    doc = Jsoup.connect(enlaceOculto).get();
                    Elements resumen = doc.getElementsByClass("bloky");

                    for (Element cell : resumen)
                    {
                        String enlaceEncontrado = cell.getElementsByTag("a").attr("href");
                        if (enlaceEncontrado != "")
                        {
                            if (enlaceEncontrado != null)
                            {
                                tmpEnlace = cell.getElementsByTag("a").attr("href");
                            }
                        }
                    }
                    System.out.println("enlace "+ tmpEnlace);
                    doc = Jsoup.connect(tmpEnlace).get();
                    Element enlace2 = doc.getElementById("downloadB");
                    tmpEnlace = enlace2.getElementsByTag("a").attr("href");
                    System.out.println("enlace "+ tmpEnlace);

                } catch (Exception ex) {

                }

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        //miAdaptador = new Adaptador(BuscarLibro.this ,mDatosArrayList);
                        //mListView.setAdapter(miAdaptador);

                    }
                });
            }
        }).start();
    }
    public static String limpiarAcentos(String cadena) {
        String limpio =null;
        if (cadena !=null) {
            String valor = cadena;
            valor = valor.toUpperCase();
            // Normalizar texto para eliminar acentos, dieresis, cedillas y tildes
            limpio = Normalizer.normalize(valor, Normalizer.Form.NFD);
            // Quitar caracteres no ASCII excepto la enie, interrogacion que abre, exclamacion que abre, grados, U con dieresis.
            limpio = limpio.replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "");
            // Regresar a la forma compuesta, para poder comparar la enie con la tabla de valores
            limpio = Normalizer.normalize(limpio, Normalizer.Form.NFC);
        }
        return limpio;
    }
}
