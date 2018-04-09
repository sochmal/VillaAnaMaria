package com.villanamaria.app.villaanamara.clases;

import android.content.ContentValues;

import com.villanamaria.app.villaanamara.data.contracts;

/**
 * Created by Christian on 19/12/2017.
 */

public class cabpedido {
    /*

    public static final String Table_cab="cabpedido";
        public static final String cab_id="_id";
        public static final String cab_codigoped="codped";
        public static final String cab_cliente="cliente";
        public static final String cab_fecha="cliente";
     */

    private String id;
    private String codpedido;
    private String cliente;
    private String nom_cliente;
    private String fecha;
    private String observacion;
    private String canpersonas;
    private String codmesa;


    public cabpedido( String codpedido, String cliente,String nom_cliente, String fecha, String observacion, String codmesa, String canpersonas) {
        this.id = null;
        this.codpedido = codpedido;
        this.cliente = cliente;
        this.fecha = fecha;
        this.observacion = observacion;
        this.nom_cliente = nom_cliente;
        this.codmesa = codmesa;
        this.canpersonas = canpersonas;


    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(contracts.cabpedido.cab_id, id);
        values.put(contracts.cabpedido.cab_cliente, cliente);
        values.put(contracts.cabpedido.cab_fecha, fecha);
        values.put(contracts.cabpedido.cab_codigoped, codpedido);
        values.put(contracts.cabpedido.cab_observaciones, observacion);
        values.put(contracts.cabpedido.cab_nomcliente, nom_cliente);
        values.put(contracts.cabpedido.cab_codmesa, codmesa);
        values.put(contracts.cabpedido.cab_canpersonar, canpersonas);
        return  values;
    }
}
