package com.villanamaria.app.villaanamara.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.villanamaria.app.villaanamara.R;
import com.villanamaria.app.villaanamara.data.contracts;

/**
 * Created by Christian on 19/12/2017.
 */

public class propedidoAdapter extends CursorAdapter {
    TextView codigo,descripcion,cantidad,costo, termino,posicion;

    public propedidoAdapter (Context context, Cursor c){
        super (context,c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);

        return inflater.inflate(R.layout.itempropedido,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        descripcion=(TextView) view.findViewById(R.id.text_descripcion_producto);
        posicion=(TextView) view.findViewById(R.id.text_posicion_producto);
        costo=(TextView) view.findViewById(R.id.text_costo_producto);
       //codigo=(TextView) view.findViewById(R.id.text_codigo_producto);
        termino=(TextView) view.findViewById(R.id.text_termino_producto);
        cantidad=(TextView) view.findViewById(R.id.text_cantidad_producto);
        String Termino=cursor.getString(cursor.getColumnIndex(contracts.detpedido.det_termino));

        if(Termino.length()<=0){
            termino.setVisibility(View.GONE);
        }
      //  codigo.setText(cursor.getString(cursor.getColumnIndex(contracts.detpedido.det_codproducto)));
        posicion.setText(cursor.getString(cursor.getColumnIndex(contracts.detpedido.det_posicion)));
        costo.setText(cursor.getString(cursor.getColumnIndex(contracts.detpedido.det_costo)));
        descripcion.setText(cursor.getString(cursor.getColumnIndex(contracts.detpedido.det_descripcion)));
        termino.setText(Termino);
        cantidad.setText(cursor.getString(cursor.getColumnIndex(contracts.detpedido.det_cantidad)));

    }
}
