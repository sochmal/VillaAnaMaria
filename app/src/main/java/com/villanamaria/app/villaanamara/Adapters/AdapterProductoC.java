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

import java.util.Currency;

/**
 * Created by Christian on 23/03/2018.
 */

public class AdapterProductoC extends CursorAdapter {
    private TextView codigo,descripcion,termino,costo;

    public AdapterProductoC(Context context, Cursor c) {

        super(context, c, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.itemproducto, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        codigo=(TextView)view.findViewById(R.id.text_codigo_producto);
        descripcion=(TextView)view.findViewById(R.id.text_descripcion_producto);
        termino=(TextView)view.findViewById(R.id.text_precio_producto);
        costo=(TextView)view.findViewById(R.id.text_termino_producto);

        codigo.setText(cursor.getString(cursor.getColumnIndex(contracts.producto.pro_codigo)));
        descripcion.setText(cursor.getString(cursor.getColumnIndex(contracts.producto.pro_descripcion)));
        termino.setText(cursor.getString(cursor.getColumnIndex(contracts.producto.pro_termino)));
        costo.setText(cursor.getString(cursor.getColumnIndex(contracts.producto.pro_precio)));

    }
}
