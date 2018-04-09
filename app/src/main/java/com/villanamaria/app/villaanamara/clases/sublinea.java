package com.villanamaria.app.villaanamara.clases;

/**
 * Created by Christian on 20/12/2017.
 */

public class sublinea  {

    String codigo;
    String descripcion;

    public sublinea(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
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
}
