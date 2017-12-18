package com.javi.pell.liberia7;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import ConjuntoDatos.Datos;
import ConjuntoDatos.SQLite_OpenHelper;

public class Lector extends AppCompatActivity {

    Button mButton, btnVolver ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector);
        mButton = (Button) findViewById(R.id.buttonAbrir);
        btnVolver = (Button) findViewById(R.id.buttonVolver);

        leerArchivo();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leerArchivo();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "volver ",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void leerArchivo()
    {
        SQLite_OpenHelper helper = new SQLite_OpenHelper(this,
                "DbLibros",null,1);
        SQLiteDatabase db ;

        String todo ="";
        File ruta = Environment.getExternalStorageDirectory();
        ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File fichero = new File(ruta+"/libros.txt");

            if (fichero.exists()) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(fichero));
                    String linea;

                    while ((linea = bufferedReader.readLine()) != null) {
                        String[] campos = linea.split(";;");

                        Datos d = new Datos();
                        try {
                            d.setTitulo(campos[0].trim().substring(1, campos[0].length() - 1));
                            d.setAutor(campos[1].trim().substring(1, campos[1].length() - 1));
                            d.setResumen(campos[2].trim().substring(1, campos[2].length() - 1));
                            d.setEnlace(campos[3].trim().substring(1, campos[3].length() - 1));
                            d.setImagen(campos[4].trim().substring(1, campos[4].length() - 1));
                            d.setFecha(campos[5].trim().substring(1, campos[5].length() - 1));
                            d.setDescargado(Integer.parseInt(campos[6].trim().substring(1, campos[6].length() - 1)));

                            //db = helper.getReadableDatabase();
                            db = helper.getWritableDatabase();
                            boolean inseartado = helper.duplicado2(db, d);
                            if (inseartado)
                                Toast.makeText(getApplicationContext(), "añadido " + d.getTitulo(),
                                        Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), "error datos ", Toast.LENGTH_SHORT).show();
                        }
                    }
                    Toast.makeText(getApplicationContext(), "fin fichero ", Toast.LENGTH_SHORT).show();
                    borrarFichero();
                } catch (SQLException ex) {
                    System.out.println("Error SQL " + ex.getMessage());
                } catch (Exception ex) {
                    System.out.println("Error lectura fichero " + ex.getMessage());
                }
            }
            else
            {
                System.out.println("Error lectura fichero no existe ");
            }
    }

    public void borrarFichero()
    {

        File ruta = Environment.getExternalStorageDirectory();
        File fichero = new File(ruta.getAbsolutePath()+"/Download/libros.txt");

        if (fichero.delete())
            System.out.println("El fichero ha sido borrado satisfactoriamente");
        else
            System.out.println("El fichero no pudó ser borrado");
        Toast.makeText(getApplicationContext(), "termino ", Toast.LENGTH_SHORT).show();
    }


    public void WriteFileDemo(String cadena)
    {

        File ruta0 = Environment.getExternalStorageDirectory();
        String fichero = "/Download/libros_nuevos.txt";
        String contenido = muestraContenido(fichero);

        File rutaFichero = new File(ruta0.getAbsolutePath() + fichero);

        String ruta = String.valueOf(rutaFichero);
        File archivo = new File(ruta);
        BufferedWriter bw;

        try {
            if (archivo.exists()) {
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write(contenido+"\n"+cadena);
            } else {
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write(cadena);
            }
            bw.close();
        }catch (IOException ex)
        {

        }
    }
    public String muestraContenido(String fichero)
    {
        String todo ="";
        File ruta = Environment.getExternalStorageDirectory();
        File rutaFichero = new File(ruta.getAbsolutePath()+fichero);

            if (rutaFichero.exists())
            {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(rutaFichero));
                    String linea;
                    while ((linea = bufferedReader.readLine()) != null)
                    {
                        todo += linea ;

                    }
                }catch (IOException ex)
                {

                }
            }
            todo = todo.replace(";;\"0\";;\"",";;\"0\";;"+"\n"+"\"");

    return todo;
    }



}
