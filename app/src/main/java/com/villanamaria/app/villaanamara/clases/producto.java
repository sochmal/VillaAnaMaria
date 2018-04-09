package com.villanamaria.app.villaanamara.clases;

import android.content.ContentValues;

import com.villanamaria.app.villaanamara.data.contracts;

/**
 * Created by Christian on 18/12/2017.
 */

public class producto {
    String id;
    String codigo;
    String descripcion;
    String precio;
    String  termino;
    String  sublinea;


    public producto(String codigo, String descripcion, String precio, String termino, String sublinea) {

        this.id = null;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.termino = termino;
        this.sublinea = sublinea;
    }
    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();
        values.put(contracts.producto.pro_id, id);
        values.put(contracts.producto.pro_codigo, codigo);
        values.put(contracts.producto.pro_descripcion, descripcion);
        values.put(contracts.producto.pro_precio, precio);
        values.put(contracts.producto.pro_termino, termino);
        values.put(contracts.producto.pro_sublinea, sublinea);
        return values;
    }
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getTermino() {
        return termino;
    }

    public void setTermino(String termino) {
        this.termino = termino;
    }

    public String getSublinea() {
        return sublinea;
    }

    public void setSublinea(String sublinea) {
        this.sublinea = sublinea;
    }
}
