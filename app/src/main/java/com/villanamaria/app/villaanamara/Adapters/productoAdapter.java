package com.villanamaria.app.villaanamara.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.villanamaria.app.villaanamara.R;
import com.villanamaria.app.villaanamara.clases.*;

import java.util.ArrayList;

/**
 * Created by Christian on 18/12/2017.
 */

public class productoAdapter  extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private ArrayList<producto> arraylist;

    public productoAdapter(Context context, ArrayList<producto> arraylist) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = arraylist;

    }
    public class ViewHolder {
        TextView codigo;
        TextView descripcion;
        TextView costo;
        TextView termino;
    }
    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public producto getItem(int position) {
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.itemproducto, null);
            // Locate the TextViews in listview_item.xml
            holder.codigo = (TextView) view.findViewById(R.id.text_codigo_producto);
            holder.descripcion = (TextView) view.findViewById(R.id.text_descripcion_producto);
            holder.costo = (TextView) view.findViewById(R.id.text_precio_producto);
            holder.termino = (TextView) view.findViewById(R.id.text_termino_producto);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.codigo.setText(arraylist.get(position).getCodigo());
        holder.descripcion.setText(arraylist.get(position).getDescripcion());
        holder.costo.setText(arraylist.get(position).getPrecio());
        holder.termino.setText(arraylist.get(position).getTermino());


        return view;
    }
}
