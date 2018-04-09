package com.villanamaria.app.villaanamara.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.villanamaria.app.villaanamara.R;
import com.villanamaria.app.villaanamara.clases.mesa;

import java.util.List;

/**
 * Created by Christian on 15/12/2017.
 */

public class mesaAdapter  extends ArrayAdapter<mesa>{
    private Context context;
    private List<mesa> mesas;
    public mesaAdapter( Context context, int resource, List<mesa> objects) {
        super(context, resource, objects);
        this.context=context;
        this.mesas = objects;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View gridView = inflater.inflate(R.layout.itemmesa,null);
        //return super.getView(position, convertView, parent);
        TextView textView = (TextView) gridView
                .findViewById(R.id.grid_item_label);
        LinearLayout lymesa=(LinearLayout) gridView.findViewById(R.id.ly_mesa);
        String estado=mesas.get(position).getEstado();
        String inv=mesas.get(position).getInv();
        textView.setText(mesas.get(position).getNombre());
        if(estado.equals("LIBRE")){
            lymesa.setBackgroundColor(Color.parseColor("#99CCFF"));
        }else if(estado.equals("OCUPADO")){

            lymesa.setBackgroundColor(Color.parseColor("#EA1548"));
        }
        //1 invisible
        if(estado.length()<=0){
            gridView.setVisibility(View.INVISIBLE);
        }
        return gridView;
    }
}
