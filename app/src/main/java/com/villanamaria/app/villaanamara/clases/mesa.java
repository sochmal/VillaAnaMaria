package com.villanamaria.app.villaanamara.clases;

/**
 * Created by Christian on 15/12/2017.
 */

public class mesa {
    String codigo;
    String npersonas;
    String nombre;
    String descripcion;
    String items;
    String estado;
    String nombrecli;
    String cicli;
    String inv;
    String posicion;

    public mesa(String codigo, String npersonas, String nombre, String descripcion, String items, String estado, String nombrecli, String cicli, String inv,String posicion) {
        this.codigo = codigo;
        this.npersonas = npersonas;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.items = items;
        this.estado = estado;
        this.nombrecli = nombrecli;
        this.cicli = cicli;
        this.inv=inv;
        this.posicion=posicion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNpersonas() {
        return npersonas;
    }

    public void setNpersonas(String npersonas) {
        this.npersonas = npersonas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombrecli() {
        return nombrecli;
    }

    public void setNombrecli(String nombrecli) {
        this.nombrecli = nombrecli;
    }

    public String getInv() {
        return inv;
    }

    public void setInv(String inv) {
        this.inv = inv;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getCicli() {
        return cicli;
    }

    public void setCicli(String cicli) {
        this.cicli = cicli;
    }
}
