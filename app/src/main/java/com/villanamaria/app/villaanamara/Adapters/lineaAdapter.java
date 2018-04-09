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
 * Created by Christian on 19/12/2017.
 */
//text_lineanombre
public class lineaAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private ArrayList<linea> arraylist;

    public lineaAdapter(Context context, ArrayList<linea> arraylist) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = arraylist;

    }
    public class ViewHolder {
        TextView nombre;
    }
    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public linea getItem(int position) {
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
            view = inflater.inflate(R.layout.itemlinea, null);
            // Locate the TextViews in listview_item.xml
            holder.nombre = (TextView) view.findViewById(R.id.text_lineanombre);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.nombre.setText(arraylist.get(position).getDescripcion());
        return view;
    }
}
