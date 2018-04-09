package com.villanamaria.app.villaanamara.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.villanamaria.app.villaanamara.R;
import com.villanamaria.app.villaanamara.clases.cliente;

import java.util.ArrayList;

/**
 * Created by Christian on 15/12/2017.
 */

public class clienteAdapter extends BaseAdapter implements Filterable {

    Context mContext;
    LayoutInflater inflater;
    private ArrayList<cliente> arraylist;
    public clienteAdapter(Context context, ArrayList<cliente> arraylist) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = arraylist;

    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class ViewHolder {
        TextView name;
        TextView cedula;
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public cliente getItem(int position) {
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
            view = inflater.inflate(R.layout.itemcliente, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.text_cliente_nombre);
            holder.cedula = (TextView) view.findViewById(R.id.text_codigo_producto);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(arraylist.get(position).getNombre());
        holder.cedula.setText(arraylist.get(position).getCedula());
        return view;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    // Filter Class
    public void filter(String charText) {
        //charText = charText.toLowerCase(Locale.getDefault());
        arraylist.clear();
        if (charText.length() == 0) {
            arraylist.addAll(arraylist);
        }
        else
        {
            for (cliente wp : arraylist)
            {
                if (wp.getNombre().contains(charText))
                {
                    arraylist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


}
