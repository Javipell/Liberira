package com.javi.pell.liberia7;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import ConjuntoDatos.Datos;
import ConjuntoDatos.SQLite_OpenHelper;

public class Detalle extends AppCompatActivity {

    SQLite_OpenHelper helper;
    SQLiteDatabase db;
    Datos d;
    Dialog customDialog = null;

    int posicion ;
    List<Datos> mDatosArrayList = new ArrayList<>();
    ImageView imageButton;
    TextView textViewTituloDetalle,textViewAutorDetalle,textViewResumenDetalle,textViewEnlaceDetalle;
    Button btnDescargarDetalle;
    CheckBox checkboxDescargadoDetalle;
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);
        imageButton = (ImageView) findViewById(R.id.imageButton);
        textViewTituloDetalle = (TextView) findViewById(R.id.textViewTituloDetalle);
        textViewAutorDetalle = (TextView) findViewById(R.id.textViewAutorDetalle);
        textViewResumenDetalle = (TextView) findViewById(R.id.textViewResumenDetalle);
        textViewEnlaceDetalle = (TextView) findViewById(R.id.textViewEnlaceDetalle);
        btnDescargarDetalle = (Button) findViewById(R.id.btnDescargarDetalle);
        checkboxDescargadoDetalle = (CheckBox) findViewById(R.id.checkboxDescargadoDetalle);
        //checkboxDescargado = (TextView) findViewById(R.id.checkboxDescargado);

        vista();

        textViewTituloDetalle.setText(mDatosArrayList.get(0).getTitulo().toString());
        textViewAutorDetalle.setText((mDatosArrayList.get(0).getAutor().toString()));
        textViewResumenDetalle.setText(mDatosArrayList.get(0).getResumen().toString());
        textViewEnlaceDetalle.setText(mDatosArrayList.get(0).getEnlace().toString());
        Picasso.with(this).load(mDatosArrayList.get(0).getImagen()).into(imageButton);

        Toast.makeText(getApplicationContext(), "estado "+ mDatosArrayList.get(0).getDescargado(), Toast.LENGTH_SHORT).show();
        if (mDatosArrayList.get(0).getDescargado()==1)
        {
            checkboxDescargadoDetalle.setChecked(true);
        }else{
            checkboxDescargadoDetalle.setChecked(false);
        }

        //checkboxDescargadoDetalle.setChecked(mDatosArrayList.get(0).getDescargado() == 1);

        checkboxDescargadoDetalle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                /*if (!checkboxDescargadoDetalle.isSelected())
                {
                    mDatosArrayList.get(0).setDescargado(1);
                }else{
                    mDatosArrayList.get(0).setDescargado(0);
                }*/
                if (0 == mDatosArrayList.get(0).getDescargado()) {
                    mDatosArrayList.get(0).setDescargado(1);
                } else {
                    mDatosArrayList.get(0).setDescargado(0);
                }
                marcarDescarga();
            }
        });

        btnDescargarDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String archivo = textViewTituloDetalle.getText().toString() + " - " +
                        textViewAutorDetalle.getText().toString() +".epub";

                Snackbar.make(view, "Descargando "+archivo,
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                descargar(textViewEnlaceDetalle.getText().toString(), archivo);
            }
        });
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
    }

    /*
     * fuente:
     * https://danielme.com/tip-android-15-dialog-personalizado/
    */
    public void mostrar()
    {
        // con este tema personalizado evitamos los bordes por defecto
        customDialog = new Dialog(this,R.style.Theme_Dialog_Translucent);
        //deshabilitamos el título por defecto
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        customDialog.setCancelable(false);
        //establecemos el contenido de nuestro dialog
        customDialog.setContentView(R.layout.dialogo);

        TextView titulo = (TextView) customDialog.findViewById(R.id.titulo);
        titulo.setText("Atención : Borrado de datos");

        TextView contenido = (TextView) customDialog.findViewById(R.id.contenido);
        contenido.setText("¿Desea eliminar este Registro?\n No se podrá recuperar.");

        ((Button) customDialog.findViewById(R.id.aceptar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                customDialog.dismiss();
                helper.abir();
                boolean error =  helper.borrarRegistro(db, posicion);
                helper.cerrar();
                if (!error) {
                    Toast.makeText(Detalle.this, "Registro borrado", Toast.LENGTH_SHORT).show();
                    Intent intento = new Intent(Detalle.this, MainActivity.class);
                    startActivity(intento);
                    finish();
                }else{
                    Toast.makeText(Detalle.this, "ERROR EN BORRRADO", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ((Button) customDialog.findViewById(R.id.cancelar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                customDialog.dismiss();
                Toast.makeText(Detalle.this, "Se cancelo el borrado", Toast.LENGTH_SHORT).show();

            }
        });

        customDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.modificar_registros, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_editar:
                //openSearch();
                Intent intento = new Intent(Detalle.this, Registro.class);
                intento.putExtra("id",d.get_Id());
                intento.putExtra("titulo", d.getTitulo().toString());
                intento.putExtra("autor", d.getAutor().toString());
                intento.putExtra("resumen", d.getResumen().toString());
                intento.putExtra("enlace", d.getEnlace().toString());
                intento.putExtra("imagen", d.getImagen().toString());
                startActivity(intento);
                return true;
            case R.id.action_borrar:

                mostrar();
                return true;
            case android.R.id.home:
                overridePendingTransition(R.anim.zoom_back_out, R.anim.zoom_back_in);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void marcarDescarga()
    {
        boolean error = helper.actualizarRegistro(db, d);
        if (!error) {
            Toast.makeText(getApplicationContext(), "EXITO AL ACTUALIZAR", Toast.LENGTH_SHORT).show();


            helper.abir();
            helper.actualizarRegistro(db, d);

            helper.cerrar();
            Intent intento = new Intent(Detalle.this, MainActivity.class);
            startActivity(intento);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "ERROR AL ACTUALIZAR", Toast.LENGTH_SHORT).show();
        }
    }


    private void vista()
    {
        helper = new SQLite_OpenHelper(getApplicationContext(),"DbLibros",null,1);
        db = helper.getReadableDatabase();

        Intent intento = getIntent();
        Bundle bundle = intento.getExtras();
        if (bundle!=null)
        {
            posicion = bundle.getInt("posicion");
            Cursor c = helper.leerRegistroId(db, posicion);
            if (c.moveToFirst())
            {
                d = new Datos();
                d.set_Id(c.getInt(0));
                d.setTitulo(c.getString(1));
                d.setAutor(c.getString(2));
                d.setResumen(c.getString(3));
                d.setEnlace(c.getString(4));
                d.setImagen(c.getString(5));
                d.setFecha(c.getString(6));
                d.setDescargado(c.getInt(7));
                mDatosArrayList.add(d);
            }
            c.close();
        }
    }
    public void descargar(String enlace, String archivo)
    {
        Uri uri = Uri.parse(enlace);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        //finishActivity(intent);

        try {
            //primero especificaremos el origen de nuestro archivo a descargar utilizando
            //la ruta completa
            URL url = new URL(enlace);

            //establecemos la conexión con el destino
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //establecemos el método jet para nuestra conexión
            //el método setdooutput es necesario para este tipo de conexiones
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            //por último establecemos nuestra conexión y cruzamos los dedos 😛
            try {
                urlConnection.connect();
            }catch (Exception ex)
            {
                Log.w("conexion descarga","fallo");
                //falloDescarga=1;
            }

            //vamos a establecer la ruta de destino para nuestra descarga
            //para hacerlo sencillo en este ejemplo he decidido descargar en
            //la raíz de la tarjeta SD
            File SDCardRoot = Environment.getExternalStorageDirectory();

            //vamos a crear un objeto del tipo de fichero
            //donde descargaremos nuestro fichero, debemos darle el nombre que
            //queramos, si quisieramos hacer esto mas completo
            //cogeríamos el nombre del origen
            File file = new File(SDCardRoot,archivo);

            //utilizaremos un objeto del tipo fileoutputstream
            //para escribir el archivo que descargamos en el nuevo
            FileOutputStream fileOutput = new FileOutputStream(file);

            //leemos los datos desde la url
            InputStream inputStream = urlConnection.getInputStream();

            //obtendremos el tamaño del archivo y lo asociaremos a una
            //variable de tipo entero
            int totalSize = urlConnection.getContentLength();
            int downloadedSize = 0;

            //creamos un buffer y una variable para ir almacenando el
            //tamaño temporal de este
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            //ahora iremos recorriendo el buffer para escribir el archivo de destino
            //siempre teniendo constancia de la cantidad descargada y el total del tamaño
            //con esto podremos crear una barra de progreso
            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {

                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                //podríamos utilizar una función para ir actualizando el progreso de lo
                //descargado
                //actualizaProgreso(downloadedSize, totalSize);
            }
            //cerramos
            fileOutput.close();
            inputStream.close();
            //y gestionamos errores
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.w("ErrorMalUrl ",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.w("ErrorIO ",e.getMessage());
        }
    }
}
