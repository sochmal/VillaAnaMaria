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
 * Created by Christian on 23/03/2018.
 */

public class AdapterClienteC extends CursorAdapter {
    private TextView nombre,cedula;

    public AdapterClienteC(Context context, Cursor c) {

        super(context, c, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.itemcliente, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        nombre=(TextView)view.findViewById(R.id.text_cliente_nombre);
        cedula=(TextView)view.findViewById(R.id.text_codigo_producto);
        nombre.setText(cursor.getString(cursor.getColumnIndex(contracts.cliente.cli_nombre)));
        cedula.setText(cursor.getString(cursor.getColumnIndex(contracts.cliente.cli_ci)));
    }
}
