package com.javi.pell.liberia7;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ConjuntoDatos.Adaptador;
import ConjuntoDatos.Datos;
import ConjuntoDatos.SQLite_OpenHelper;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SQLite_OpenHelper helper ;
    SQLiteDatabase db ;


    EditText mEditTextBuscar;
    ListView mListView;
    List<Datos> mDatosArrayList = new ArrayList<>();
    Adaptador miAdaptador;
    String orden = "Titulo";
    public String ascendente = "ASC";
    String textoBuscar = "";
    //String descargado;
    String filtro = " WHERE descargado=";
    Globales mGlobales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGlobales = new Globales();
        mListView = (ListView) findViewById(R.id.listView);
        if (mGlobales.getDescargado() =="0")
        {
            mListView.setBackgroundColor(Color.CYAN);
        }else{
            mListView.setBackgroundColor(Color.BLUE);
        }
        Intent intento = getIntent();
        Bundle bundle = intento.getExtras();
        if (bundle!=null)
        {
            orden = bundle.getString ("orden");
            ascendente = bundle.getString("ascendente");
            mGlobales.setDescargado( bundle.getString("descargados") );
        }
        CargarDatos();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l) {

                Intent intento = new Intent(getApplicationContext(),Detalle.class);
                int id = (int) miAdaptador.getItemId(posicion);
                intento.putExtra("posicion", id);
                startActivity(intento);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                //Toast.makeText(getApplicationContext(), "Registro "+ id, Toast.LENGTH_SHORT).show();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Buscando libros nuevos", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // metodo para leer registros desde un fichero
                CargarFichero();
                CargarDatos();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        boolean ordenar = false;
        boolean descargados = false;

        if (id == R.id.nav_camera) {
            // Handle the camera action
            BuscarLibro buscarLibro = new BuscarLibro();
            Intent intento = new Intent(getApplicationContext(), BuscarLibro.class);
            startActivity(intento);
            //finish();
        } else if (id == R.id.nav_gallery) {
            Registro registro = new Registro();
            Intent intento = new Intent(getApplicationContext(), Registro.class);
            startActivity(intento);

        } else if (id == R.id.nav_slideshow) {
            if (Integer.parseInt(mGlobales.getDescargado()) ==0)
            {
                mGlobales.setDescargado("1");
            }else{
                mGlobales.setDescargado("0");
            }
            descargados = true;
        } else if (id == R.id.nav_manage) {
            orden = "Titulo";
            ordenar = true;
        } else if (id == R.id.nav_share) {
            CargarFichero();
            CargarDatos();
        //} else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_ord_fecha){
            orden = "Fecha";
            ordenar = true;
        }else if (id == R.id.nav_ord_autor){
            orden = "Autor";
            ordenar = true;
        }

        if ( ordenar )
        {
            Toast.makeText(getApplicationContext(),"antes: "+ascendente,Toast.LENGTH_SHORT).show();
            ascendente = ascendente=="DESC" ? "ASC" : "DESC";
            Toast.makeText(getApplicationContext(),"despues: "+ascendente,Toast.LENGTH_SHORT).show();
            Intent intento = new Intent(MainActivity.this,MainActivity.class);
            intento.putExtra("orden", orden);
            intento.putExtra("ascendente", ascendente);
            intento.putExtra("descargados", mGlobales.getDescargado());
            finish();
            startActivity(intento);
        }
        if (descargados)
        {
            Intent intento = new Intent(MainActivity.this,MainActivity.class);
            intento.putExtra("orden", orden);
            intento.putExtra("ascendente", ascendente);
            intento.putExtra("descargados", mGlobales.getDescargado());


            startActivity(intento);        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void CargarDatos()
    {
        ListaDatos();
        miAdaptador = new Adaptador(this ,mDatosArrayList);
        mListView.setAdapter(miAdaptador);
    }
    public void ListaDatos()
    {
        helper = new SQLite_OpenHelper(this,
                "DbLibros",null,1);
        db = helper.getReadableDatabase();
        // metodo para leer registros desde la clase
        //helper.datosPrueba(db, mListView);

        mDatosArrayList.clear();

        if (textoBuscar!="")
        {
            filtro = filtro + mGlobales.getDescargado() + " AND titulo LIKE '%" + textoBuscar +"%' OR autor LIKE '%" + textoBuscar + "%' ";
            //filtro = " WHERE titulo like'%"+ textoBuscar + "%' OR autor LIKE '%" + textoBuscar + "%' ";
        }else{
            filtro  += mGlobales.getDescargado() ;
        }
        if (orden==""){orden="titulo"; }
        if (ascendente==""){ascendente="ASC";}

        String sql = "select * from tbl_datos " + filtro + " order by " + orden + " " + ascendente;
        //Toast.makeText(getApplicationContext(),sql,Toast.LENGTH_SHORT).show();

        Cursor c = db.rawQuery(sql,null);
        if (c.moveToFirst())
        {
            do{
                Datos d = new Datos();
                d.set_Id(c.getInt(0));
                String str = c.getString(1).toLowerCase();
                str = str.substring(0, 1).toUpperCase() + str.substring(1);
                //d.setTitulo(c.getString(1));
                d.setTitulo(str);
                d.setAutor(c.getString(2));
                d.setResumen(c.getString(3));
                d.setEnlace(c.getString(4));
                d.setImagen(c.getString(5));
                d.setFecha(c.getString(6));
                d.setDescargado(c.getInt(7));
                mDatosArrayList.add(d);

            } while (c.moveToNext());
        }
        //db.close();
        c.close();
    }

    public void CargarFichero()
    {
        Lector lector = new Lector();
        Intent intent = new Intent(getApplicationContext(), Lector.class);
        startActivity(intent);;

    }

}
