package ConjuntoDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by javier on 27/11/17.
 */

public class SQLite_OpenHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "dba_datos";
    private static final String TABLE_NAME = "tbl_datos";
    private static final int DATABASE_VERSION = 1;

    public static final String COL_ID = "_id";
    public static final String COL_TITULO = "titulo";
    public static final String COL_AUTOR = "autor";
    public static final String COL_RESUMEN = "resumen";
    public static final String COL_ENLACE = "enlace";
    public static final String COL_IMAGEN = "imagen";
    public static final String COL_DESCARGADO = "descargado";
    public static final String COL_FECHA = "fecha";


    public SQLite_OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String DATABASE_CREATE =
                "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                        COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                        COL_TITULO + " TEXT, " +
                        COL_AUTOR + " TEXT, " +
                        COL_RESUMEN + " TEXT, " +
                        COL_ENLACE + " TEXT, " +
                        COL_IMAGEN + " TEXT, " +
                        COL_FECHA + " TEXT, " +
                        COL_DESCARGADO + " INT(1) " +
                        " );";
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }

    // abrir la base de datos
    public void abir()
    {
        this.getReadableDatabase();
    }

    // cerrar la base de datos
    public void cerrar()
    {
        this.close();
    }

    public Cursor leerRegistroId(SQLiteDatabase db, int id)
    {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE _id =" +id;
        Cursor c = db.rawQuery(sql,null);

        if (c.moveToFirst())
        {
            Log.w("fallo busqueda id ", sql);
        }

        return c;
    }

    // insertar registro en una tabla
    public void insertarRegistro(String titulo, String autor, String resumen, String enlace,
                                 String imagen, String fecha, int descargado )
    {

        ContentValues values = new ContentValues();
        //values.put("_Id", _Id); // no hace falta es auto incremental
        values.put("Titulo", titulo);
        values.put("Autor", autor);
        values.put("Resumen", resumen);
        values.put("Enlace", enlace);
        values.put("Imagen", imagen);
        values.put("Fecha", fecha);
        values.put("Descargado", descargado);


        this.getWritableDatabase().insert(TABLE_NAME, null, values);
    }

    public boolean insertarRegistro2(SQLiteDatabase db, Datos datos)
    {
        boolean insertado = false;
        String sql = "SELECT * FROM tbl_datos WHERE titulo='" + datos.getTitulo() + "'";

        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst())
        {
            Log.w("fallo",sql);
        }
        else{
            ContentValues values = new ContentValues();
            //values.put("_Id", _Id); // no hace falta es auto incremental
            values.put("Titulo", datos.getTitulo());
            values.put("Autor", datos.getAutor());
            values.put("Resumen", datos.getResumen());
            values.put("Enlace", datos.getEnlace());
            values.put("Imagen", datos.getImagen());
            values.put("Fecha", datos.getFecha());
            values.put("Descargado", datos.getDescargado());

            this.getWritableDatabase().insert(TABLE_NAME, null, values);
            Log.w("insertado", sql);
            insertado = true;
        }
        return  insertado;
    }
    // eliminar registaros
    public boolean borrarRegistro(SQLiteDatabase db, int id)
    {
        boolean error = false;
        String condicion = "_Id=" + id;
        try {
            db.delete(TABLE_NAME, condicion, null);
        }catch (Exception ex)
        {
            Log.w("Error borrado ",ex.getMessage());
            error=true;
        }
        return error;
    }
     // actualizar registros
    public boolean actualizarRegistro(SQLiteDatabase db, Datos datos)
    {
        boolean error = false;

        ContentValues values = new ContentValues();
        values.put("Titulo", datos.getTitulo());
        values.put("Autor", datos.getAutor());
        values.put("Resumen", datos.getResumen());
        values.put("Enlace", datos.getEnlace());
        values.put("Imagen", datos.getImagen());
        values.put("Fecha", datos.getFecha());
        values.put("Descargado", datos.getDescargado());
        try {
            String condicion = "_Id="+datos.get_Id();
            Log.w("condicion ", condicion);
            int cant = db.update(TABLE_NAME, values, "_Id=" + datos.get_Id(), null);
            if (cant==1)
                Log.w("resultado ", ""+cant);
        }catch (Exception ex){
            Log.w("Error actualizar ", ex.getCause());
            error=true;
        }

        return  error;
    }
    public boolean  duplicado2(SQLiteDatabase db, Datos datos)
    {
        boolean insertado = false;

        String txtdato = datos.getTitulo();
        String sql = "SELECT * FROM tbl_datos WHERE titulo='" + datos.getTitulo() + "'";

        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst())
        {
            Log.w("fallo",sql);
        }
        else{
            insertarRegistro(
                    datos.getTitulo(), datos.getAutor(),
                    datos.getResumen(), datos.getEnlace(),
                    datos.getImagen(), datos.getFecha(),
                    datos.getDescargado() );
            Log.w("insertado", sql);
            insertado = true;
        }
        return  insertado;
    }

    public void duplicado(SQLiteDatabase db, Datos datos, View view)
    {
        String txtdato = datos.getTitulo();
        String sql = "SELECT * FROM tbl_datos WHERE titulo='" + datos.getTitulo() + "'";

        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst())
        {
            Log.w("fallo",sql);
        }
        else{
            insertarRegistro(
                    datos.getTitulo(), datos.getAutor(),
                    datos.getResumen(), datos.getEnlace(),
                    datos.getImagen(), datos.getFecha(),
                    datos.getDescargado() );
            Log.w("insertado", sql);
        }
    }

    public void datosPrueba(SQLiteDatabase db, View view)
    {
        duplicado(db, new Datos("COMO FUEGO EN EL HIELO","Luz Gabás",
                "El fatídico día en el que Attua tuvo que ocupar el lugar de su padre supo que su prometedor futuro se había truncado. Ahora debía regentar las termas que habían sido el sustento de su familia, en una tierra fronteriza a la que él nunca hubiera elegido regresar. Junto al suyo, también se frustró el deseo de Cristela, quien anhelaba una vida a su lado y, además, alejarse de su insoportable rutina en un entorno hostil. Un nuevo revés del destino pondrá a prueba el irrefrenable amor entre ellos; y así, entre malentendidos y obligaciones, decisiones y obsesiones, traiciones y lealtades, Luz Gabás teje una bella historia de amor, honor y superación. Los convulsos años de mediados del siglo xix, entre guerras carlistas y revoluciones; la construcción de un sueño en las indomables montañas que separan Francia y España; y una historia de amor que traspasa todas las barreras. Una novela escrita con el apasionante pulso narrativo de la autora de Palmeras en la nieve.",
                "http://www.beeupload.net/filed/QAN8nlpX/Como+fuego+en+el+hielo+-+Luz+Gabas",
                "https://www.quelibroleo.com/images/libros/libro-1484932736.jpg",
                "09/11/2017",0),view);
        duplicado(db, new Datos("El silencio de los corderos","Thomas Harris",
                "A Clarice Starling, joven y ambiciosa estudiante de la academia del FBI, le encomiendan que entreviste a Hannibal Lecter, brillante psiquiatra y despiadado asesino, para conseguir su colaboración en la resolución de un caso de asesinatos en serie. El asombroso conocimiento de Lecter del comportamiento humano y su poderosa personalidad cautivarán de inmediato a Clarice, quien, incapaz de dominarla, establecerá con él una ambigua, inquientante y peligrosa relación. ",
                "http://www.beeupload.net/filed/7Ve3Zpl9/El+silencio+de+los+corderos+-+Thomas+Harris",
                "https://assets2.lectulandia.com/b/Thomas%20Harris/El%20silencio%20de%20los%20corderos%20(2982)/big.jpg",
                "10/11/2017",0),view);
        duplicado(db, new Datos("Dragón rojo","Thomas Harris",
                "Sin una razón de peso, el agente especial Jack Crawford no habría turbado la apacible existencia y el anonimato de Will Graham, el hombre que había conseguido desenmascarar al psicópata doctor Lecter, más conocido en los medios de comunicación como «Hannibal el Caníbal». En efecto, las circunstancias que rodean los asesinatos de dos familias en Birmingham y en Atlanta convierten al investigador Graham en un hombre imprescindible del equipo de detectives que investigan el perfil psicológico del monstruo, el «Dragón Rojo», y las horribles mutilaciones que inflige a sus víctimas. ",
                "http://www.beeupload.net/filed/u1Wvtf3b/Dragon+rojo+-+Thomas+Harris",
                "https://assets2.lectulandia.com/b/Thomas%20Harris/Dragon%20rojo%20(3940)/big.jpg",
                "11/11/2017",0),view);
        duplicado(db, new Datos("Hannibal","Thomas Harris",
                "Siete años han pasado desde que Clarice Starling, agente especial del FBI, se entrevistara con el doctor Hannibal Lecter en un hospital de máxima seguridad. Su ayuda fue decisiva para que ella capturara al asesino en serie Buffalo Bill. Siete años han transcurrido desde que Hannibal el Caníbal burlara la vigilancia y desapareciera dejando una sangrienta estela de víctimas a su paso. Sin embargo, cuando Clarice cae en desgracia en el FBI, el doctor Lecter sale de las sombras para ponerse en contacto con ella. Así se reaviva la caza de la presa más codiciada, y Clarice, que nunca ha podido olvidar su encuentro con la brillante y perversa mente del psiquiatra, es encargada del caso.",
                "http://www.beeupload.net/filed/1CKOBDgF/Hannibal+-+Thomas+Harris",
                "https://assets2.lectulandia.com/b/Thomas%20Harris/Hannibal%20(1710)/big.jpg",
                "12/11/2017",0),view);
        duplicado(db, new Datos("Rodas, la hija del sol","Gillian Bradshaw",
                "Primavera del 246 a.C.\n" +
                        "Cuando el Atalanta, barco de guerra de la república de Rodas, destruye una embarcación pirata, se sitúa, sin saberlo, en el ojo de un huracán inesperado. Entre las víctimas que rescata de los piratas se encuentra una hermosa mujer, Dionisia, favorita del rey de Siria, conocedora de un secreto capaz de sumergir a todo el Mediterráneo oriental en una guerra larga y de vencedor incierto.\n" +
                        "Isócrates, el capitán del barco de guerra, un hombre sencillo que ha dedicado toda su vida a combatir la piratería, se verá envuelto en un conflicto diplomático difícil de manejar; tendrá que conseguir evitar que estalle la guerra entre los tres imperios que rodean a Rodas: Egipto, Siria y Macedonia. Amenazado de muerte por la despiadada reina siria Laodice, viajará de un lado a otro del mar en un intento por atrapar a su mayor enemigo y salvar a Dionisia de una muerte segura.",
                "http://www.beeupload.net/filed/1CYAVgHb/Rodas+la+hija+del+sol+-+Gillian+Bradshaw",
                "https://assets2.lectulandia.com/b/ab/Gillian%20Bradshaw/Rodas%20la%20hija%20del%20sol%20(5)/big.jpg",
                "13/11/2017",0),view);
        duplicado(db, new Datos("El faro de Alejandría","Gillian Bradshaw",
                "Con tan sólo quince años, la bella Caris abrigaba el deseo inconfesable de poder estudiar medicina. Inteligente, curiosa y con la determinación propia de quien ansía cumplir con un ideal, no era extraño verla ilustrarse, apasionadamente, con los escritos de Galeno e Hipócrates, además de estudiar con fruición latín o literatura. Un sueño, por demás, imposible.",
                "http://www.beeupload.net/file/O6GyMYsE",
                "https://assets2.lectulandia.com/b/Gillian%20Bradshaw/El%20faro%20de%20Alejandria%20(5)/big.jpg",
                "14/11/2017",0),view);
        duplicado(db, new Datos("El susurro del diablo","Miyuki Miyabe",
                "res muertes se suceden en un breve intervalo de tiempo: una chica salta desde la azotea de un edificio de seis plantas; otra, cae del andén al paso de un tren; y una tercera es atropellada de noche por un taxi. Pero ¿qué relación guardan estos tres casos? ¿Accidentes, suicidios... o asesinatos? Mamoru, un joven de dieciséis años, tratará de desenmarañar el misterio. Su tío es el taxista que ha atropellado a la tercera víctima y se encuentra en prisión preventiva, acusado de homicidio involuntario. ",
                "http://www.beeupload.net/file/8K6x1ET9",
                "https://assets2.lectulandia.com/b/Miyuki%20Miyabe/El%20susurro%20del%20diablo%20(2090)/big.jpg",
                "15/11/2017",0),view);
        duplicado(db, new Datos("Secretos","Christian Martins",
                "A falta de unos días para dar el “sí, quiero”, Julia decide mandar todo a paseo y comenzar una vida de cero. Para hacerlo, toma la decisión que disfrutar en solitario del viaje que tenía programado para la luna de miel, sin saber lo que encontrará en éste.En pleno Caribe, conocerá a Elías Castro, un poderoso empresario que tiene todo lo que quiere en el momento en el que lo pide. Ambos comenzarán un apasionante romance rodeados de los más exquisitos lujos. Julia no tardará demasiado en enamorarse del irresistible Elías, pero también descubrirá que no todo es lo que parece.",
                "http://mundofile.info/mmrX?pt=XnE0qozyWTdMAb75PbP9DlvCQvg7vWbihHyvOffmYa8%3D",
                "http://www.descargalibros.net/img/portadas/2017/10/30/4133.jpg",
                "16/11/2017",0),view);
        duplicado(db, new Datos("El amargo don del olvido","A. V. San Martín",
                "Finalista del concurso indie de amazon 2017.UNA HISTORIA DE INTRIGA, MENTIRAS, AMOR, CELOS Y TRAGEDIA DONDE NADA ES LO QUE PARECE.UN MONUMENTAL THRILLER, TREPIDANTE, INESPERADO Y SINGULAR, DONDE EL FUEGO NO ES LO ÚNICO QUE ARDE.SINOPSISAquí es donde debería dejar impresas esas pequeñas pinceladas que resuman la historia de este libro. Pero la verdad es que poco importa que describa cómo es Inés, su protagonista; que narre de forma atrayente su pasado o deje constancia del incendio que todo lo trunca porque todo lo que diga puede ser cierto, o no, o tal vez cambiar antes de que termines de leer esta sinopsis.También puedo incluir una descripción sobre Oliver y esa extraña relación amor-odio que mantienen, pero antes deberás asegurarte de que Oliver es de verdad quien sospechas.Así que no importa de qué forma lo resuma porque, en realidad, la historia podría ser otra...",
                "http://mundofile.info/i8wY?pt=Hzv4BC5n7jV2lEkeJkjQ9g9%2FxXfvJWMZ8a3ZozDzdJE%3D",
                "http://www.descargalibros.net/img/portadas/2017/10/23/4086.jpg",
                "17/11/2017",0),view);
        duplicado(db, new Datos("La cacería","J.M. Peace",
                "Samantha Willis es una oficial de policía de Queensland, Australia, y una mujer convencida de su capacidad de cuidar de sí misma. Almenos hasta que cae en manos de un peligroso psicópata, cuyo juego consistirá en cazarla como a un animal. La detective Janine Postlewaite no conoce a Sammi personalmente, pero los agentes de la ley se cuidan los unos a los otros, y dirigirá la investigación contenacidad. Mientras un asesino da caza a Sammi, Janine deberá reunir las pistas que podrían conducir hasta esta. Todo se convertirá en una carrera contra el reloj, en la que Sammi deberá apelar, para sobrevivir, a su sangre fría y su experiencia como policía, mientras sus colegas intentan descifrar las dialogo antes de que el sádico asesino lleve a cabo su juego mortal.",
                "http://mundofile.info/dWc4?pt=Hyhy1Qw1ws88xW1JFjT5VGZbYx43J7b5lAL4EO2wvfk%3D",
                "http://www.descargalibros.net/img/portadas/2017/10/16/4039.jpg",
                "18/11/2017",0),view);
        duplicado(db, new Datos("Septiembre puede esperar","Susana Fortes",
                "El 8 de mayo de 1955 la escritora Emily J. Parker desaparece en Londres mientras la ciudad celebra el décimo aniversario del final de la II Guerra Mundial. Nunca más vuelve a saberse nada de ella. Años más tarde, Rebeca, una estudiante española de filología, decide trasladarse a Londres para preparar su tesis doctoral sobre la misteriosa escritora. Durante la investigación, la infancia y la vida familiar de Rebeca se van trenzando con el pasado de Emily en el Londres del Blitz y de la posguerra en un entramado de espionaje y relaciones sentimentales que forman un extraño puzle tan sugerente como difícil de interpretar. Susana Fortes crea una apasionante trama de misterio e intriga psicológica que abarca desde el mundo del espionaje hasta los rincones más personales de sus protagonistas.",
                "https://drive.google.com/file/d/1aQIxCmPf-mBiZ0nnzvV2SrtBDNDq8rUR/view?usp=sharing",
                "http://www.descargalibros.net/img/portadas/2017/11/24/4176.jpg",
                "02/12/2017",0),view);

        /*duplicado(db, new Datos("Rodas","Gillian",
                "Primavera",
                "http://www.beeupload.net/file/",
                "https://","2017/01/01",0),view);*/
    }


}
