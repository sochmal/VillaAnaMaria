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
 * Created by Christian on 20/12/2017.
 */

public class sublineaAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private ArrayList<sublinea> arraylist;

    public sublineaAdapter(Context context, ArrayList<sublinea> arraylist) {
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
    public sublinea getItem(int position) {
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
            view = inflater.inflate(R.layout.itemslinea, null);
            holder.nombre = (TextView) view.findViewById(R.id.text_slineanombre);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.nombre.setText(arraylist.get(position).getDescripcion());
        return view;
    }
}
