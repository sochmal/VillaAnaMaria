package com.villanamaria.app.villaanamara.clases;

import android.content.ContentValues;

import com.villanamaria.app.villaanamara.data.contracts;

/**
 * Created by Christian on 19/12/2017.
 */

public class detpedido {
    /*

     public static final String Table_det="detpedido";
        public static final String det_id="_id";
        public static final String det_codigoped="codped";
        public static final String det_codproducto="procodigo";
        public static final String det_cantidad="procantidad";
        public static final String det_costo="procosto";
        public static final String det_termino="protermino";
        public static final String det_posicion="proposicion";
     */

    private String id;
    private String codpedido;
    private String propedido;
    private String procantidad;
    private String prodescripcion;
    private String procosto;
    private String protermino;
    private String proposicion;
    private String proobservaciones;
    private String pronombre;
    private String subtotal;
    private String proorden;
    private String promodificar;

    public detpedido( String codpedido, String propedido, String procantidad, String prodescripcion, String procosto, String protermino, String proposicion, String proobservaciones, String pronombre, String subtotal,String proorden, String promodificar) {
        this.id = null;
        this.codpedido = codpedido;
        this.propedido = propedido;
        this.procantidad = procantidad;
        this.prodescripcion = prodescripcion;
        this.procosto = procosto;
        this.protermino = protermino;
        this.proposicion = proposicion;
        this.proobservaciones = proobservaciones;
        this.pronombre = pronombre;
        this.subtotal = subtotal;
        this.proorden = proorden;
        this.promodificar = promodificar;
    }

    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();
        values.put(contracts.detpedido.det_id, id);
        values.put(contracts.detpedido.det_codigoped, codpedido);
        values.put(contracts.detpedido.det_codproducto, propedido);
        values.put(contracts.detpedido.det_cantidad, procantidad);
        values.put(contracts.detpedido.det_costo, procosto);
        values.put(contracts.detpedido.det_termino, protermino);
        values.put(contracts.detpedido.det_posicion, proposicion);
        values.put(contracts.detpedido.det_descripcion, prodescripcion);
        values.put(contracts.detpedido.det_observaciones, proobservaciones);
        values.put(contracts.detpedido.det_nomproducto, pronombre);
        values.put(contracts.detpedido.det_subtotal, subtotal);
        values.put(contracts.detpedido.det_orden, proorden);
        values.put(contracts.detpedido.det_promodificar, promodificar);
        return values;
    }
}
