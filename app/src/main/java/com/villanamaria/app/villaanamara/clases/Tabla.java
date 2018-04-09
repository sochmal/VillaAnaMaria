package com.villanamaria.app.villaanamara.clases;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.villanamaria.app.villaanamara.Mesas;
import com.villanamaria.app.villaanamara.Pedido;
import com.villanamaria.app.villaanamara.R;
import com.villanamaria.app.villaanamara.data.dbhandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Christian on 29/12/2017.
 */

public class Tabla {
    private TableLayout tabla; // Layout donde se pintará la tabla
    private ArrayList<TableRow> filas; // Array de las filas de la tabla
    private Activity actividad;
    private Resources rs;
    private int ancho,anchoLayout;
    private String Elemento;
    private dbhandler mdbdata;
    private int FILAS, COLUMNAS; // Filas y columnas de nuestra tabla

    /**
     * Constructor de la tabla
     * @param actividad Actividad donde va a estar la tabla
     * @param tabla TableLayout donde se pintará la tabla
     */
    public Tabla(Activity actividad, TableLayout tabla,int ancho,int cantidadelementos)
    {
        this.actividad = actividad;
        this.tabla = tabla;
         this.ancho=ancho;
       /*this.cantindadelementos=cantidadelementos;*/
        this.anchoLayout=ancho/cantidadelementos;
        anchoLayout=anchoLayout-10;
        rs = this.actividad.getResources();
        FILAS = COLUMNAS = 0;
        filas = new ArrayList<TableRow>();
        mdbdata =new dbhandler(actividad);
        Log.i(TAG, "agregarFilaTablaMesa: anchocelda "+anchoLayout);
    }

    public void agregarFilaTablaMesa(List<mesa> elementos)
    {
        TableRow.LayoutParams layoutCelda;
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(ancho, TableRow.LayoutParams.WRAP_CONTENT);
        layoutFila.setMargins(5,5,5,5);
        TableRow fila = new TableRow(actividad);

        fila.setLayoutParams(layoutFila);

        for(int i = 0; i< elementos.size(); i++)
        {
            //Log.i(TAG, "agregarFilaTabla: i: " + i);
            mesa obj=(mesa)elementos.get(i);
            TextView texto = new TextView(actividad);
            Button mbtn = new Button(actividad);
            String visible=obj.getEstado();
            Elemento =String.valueOf(obj.getNombre());
            mbtn.setText(String.valueOf(obj.getNombre()));
            mbtn.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.mesajpg,0,0);
            Log.i(TAG, "agregarFilaTabla: i: " + i + "  visible "+ visible);
            //layoutCelda = new TableRow.LayoutParams(anchoLayout, TableRow.LayoutParams.MATCH_PARENT);
            layoutCelda = new TableRow.LayoutParams(anchoLayout, 200);
            layoutCelda.setMargins(5,5,5,5);
            texto.setLayoutParams(layoutCelda);
            mbtn.setLayoutParams(layoutCelda);
            mbtn.setTextColor(Color.parseColor("#FFFFFF"));
            mbtn.setBackgroundColor(Color.parseColor("#3F51B5"));
            mbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "onClick: btn2 " + ((Button)view).getText());
                    Intent m=new Intent(actividad,Pedido.class);
                    String codpedido=cod_transaccion();
                    String fecha=fecha_actual();
                    m.putExtra("mesa",  ((Button)view).getText());
                    m.putExtra("codpedido",  codpedido);
                    m.putExtra("fecha", fecha  );
                    mdbdata.nuevoCabPedido(new cabpedido(codpedido,"99999999","Consumidor Final",fecha,"","",""));
                    actividad.startActivity(m);

                }
            });
            fila.addView(mbtn);
            if(visible.equals("2")){
                mbtn.setVisibility(View.INVISIBLE);
            }
        }
        tabla.addView(fila);
        filas.add(fila);
        FILAS++;
    }
    public String fecha_actual(){
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);
        return dateString;
    }
    public String cod_transaccion(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Calendar calendario= Calendar.getInstance();
        return sdf.format(calendario.getTime());
    }
}
