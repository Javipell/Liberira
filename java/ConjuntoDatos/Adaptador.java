package ConjuntoDatos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.javi.pell.liberia7.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by javier on 18/11/17.
 */

public class Adaptador extends BaseAdapter
{
    Context contexto;
    List<Datos> ListaDatos;

    public Adaptador(Context contexto, List<Datos> listaDatos)
    {
        this.contexto = contexto;
        ListaDatos = listaDatos;
    }

    @Override
    public int getCount() {
        return ListaDatos.size();
    }

    @Override
    public Object getItem(int posicion) {
        return ListaDatos.get(posicion);
    }

    @Override
    public long getItemId(int posicion)
    {
        return ListaDatos.get(posicion).get_Id();
    }

    @Override
    public View getView(int posicion, View view, ViewGroup viewGroup) {
        View vista = view;
        LayoutInflater inflate = LayoutInflater.from(contexto);
        vista = inflate.inflate(R.layout.resumen_libros,null);
        TextView titulo = vista.findViewById(R.id.textViewTitulo);
        TextView autor = vista.findViewById(R.id.textViewAutor);
        TextView resumen = vista.findViewById(R.id.textViewResumen);
        TextView enlace = vista.findViewById(R.id.textViewEnlace);
        ImageView imagen = vista.findViewById(R.id.imageViewCaratula);
        TextView fecha = vista.findViewById(R.id.textViewFecha);

        //CheckBox descargado = (CheckBox) vista.findViewById(R.id.checkboxDescargado);
        TextView descargado = (TextView) vista.findViewById(R.id.checkboxDescargado);

        titulo.setText(ListaDatos.get(posicion).getTitulo());
        autor.setText(ListaDatos.get(posicion).getAutor());
        resumen.setText(ListaDatos.get(posicion).getResumen());
        enlace.setText(ListaDatos.get(posicion).getEnlace());
        fecha.setText(ListaDatos.get(posicion).getFecha());
        //imagen.setImageBitmap(ListaDatos.get(posicion).getImagen());
        String url = ListaDatos.get(posicion).getImagen();
        //Glide.with(contexto).load(ListaDatos.get(posicion).getImagen()).asBitmap().into(imagen);
        //Glide.with (contexto).load (url).asBitmap().into(imagen);

        //Picasso.with(contexto).load(ListaDatos.get(posicion).getImagen()).into(imagen);
        Picasso.with(contexto)
                .load(url)
                .memoryPolicy(MemoryPolicy.NO_CACHE )
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.drawable.ic_menu_camera)
                .noFade()
                .into(imagen);
        //descargado.setChecked(ListaDatos.get(posicion).getDescargado() == 1);

        if (ListaDatos.get(posicion).getDescargado()==1)
        {
            descargado.setText("descargado");
        }else{
            descargado.setText("SIN descargar");
        }

        return vista;
    }
}
