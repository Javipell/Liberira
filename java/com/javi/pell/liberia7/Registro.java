package com.javi.pell.liberia7;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ConjuntoDatos.Datos;
import ConjuntoDatos.SQLite_OpenHelper;

public class Registro extends AppCompatActivity {

    Button btnGuardar;
    EditText titulo, autor, resumen, enlace, imagen;
    TextView fecha;

    SQLite_OpenHelper helper= new SQLite_OpenHelper(Registro.this,
            "DbLibros",null,1);
    SQLiteDatabase db;
    Datos d;

    int posicion ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        titulo = (EditText) findViewById(R.id.r_txtTitulo);
        autor = (EditText) findViewById(R.id.r_txtAutor);
        resumen = (EditText) findViewById(R.id.r_txtResumen);
        enlace = (EditText) findViewById(R.id.r_txtEnlace);
        imagen = (EditText) findViewById(R.id.r_txtImagen);
        fecha = (TextView) findViewById(R.id.r_txtFecha);
        //titulo.setText("La chica del tren");
        //autor.setText("Paula Hawkins");
        //resumen.setText("¿Estabas en el tren de las 8.04? ¿Viste algo sospechoso? Rachel, síRachel toma siempre el tren de las 8.04 h. Cada mañana lo mismo: el mismo paisaje, las mismas casas… y la misma parada en la señal roja. Son solo unos segundos, pero le permiten observar a una pareja desayunando tranquilamente en su terraza.");
        //enlace.setText("http://espamobi.com/down.php?book=20068&t=La chica del tren.mobi");
        //imagen.setText("http://assets.espamobi.com/b/Paula%20Hawkins/La%20chica%20del%20tren%20(3307)/big.jpg");
        Intent intento = getIntent();
        final Bundle bundle = intento.getExtras();
        if (bundle!=null)
        {
            posicion = bundle.getInt ("id");
            titulo.setText(bundle.getString("titulo"));
            autor.setText(bundle.getString("autor"));
            resumen.setText(bundle.getString("resumen"));
            enlace.setText(bundle.getString("enlace"));
            imagen.setText(bundle.getString("imagen"));
        }

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Guardando libro...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //helper = new SQLite_OpenHelper(Registro.this, "DbLibros",null,1);
                if (bundle==null)
                {
                    helper.abir();
                    helper.insertarRegistro(
                            titulo.getText().toString(),
                            autor.getText().toString(),
                            resumen.getText().toString(),
                            enlace.getText().toString(),
                            imagen.getText().toString(),
                            fecha.getText().toString(),
                            0);
                    helper.cerrar();
                    Snackbar.make(view,"Añadido el registro", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Intent intento = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intento);
                    finish();
                }else{
                    db = helper.getWritableDatabase();
                    Datos d = new Datos();
                    d.set_Id(posicion);
                    d.setTitulo(titulo.getText().toString());
                    d.setAutor(autor.getText().toString());
                    d.setResumen(resumen.getText().toString());
                    d.setEnlace(enlace.getText().toString());
                    d.setImagen(imagen.getText().toString());
                    d.setFecha("07/12/2017");
                    d.setDescargado(0);

                    helper.abir();
                    helper.actualizarRegistro(db, d);
                    helper.cerrar();
                    Snackbar.make(view,"Modificado el registro", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Intent intento = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intento);
                    finish();
                }
            }
        });
    }
    //@Override
    public boolean OnCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.modificar_registros, menu);
        return true;
    }

}
