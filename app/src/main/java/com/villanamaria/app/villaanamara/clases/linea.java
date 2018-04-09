package com.villanamaria.app.villaanamara.clases;

/**
 * Created by Christian on 19/12/2017.
 */

public class linea {
    String codigo;
    String descripcion;

    public linea(String codigo, String descripcion) {
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
