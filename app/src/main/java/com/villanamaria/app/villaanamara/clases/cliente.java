package com.villanamaria.app.villaanamara.clases;

import android.content.ContentValues;

import com.villanamaria.app.villaanamara.data.contracts;

/**
 * Created by Christian on 15/12/2017.
 */

public class cliente {
    String id;
    String nombre;
    String cedula;
    String direccion;
    String telefono;
    String email;

    public cliente(String nombre, String cedula, String direccion, String telefono, String email) {
        this.id = null;
        this.nombre = nombre;
        this.cedula = cedula;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }
    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();
        values.put(contracts.cliente.cli_id, id);
        values.put(contracts.cliente.cli_ci, cedula);
        values.put(contracts.cliente.cli_direccion, direccion);
        values.put(contracts.cliente.cli_nombre, nombre);
        values.put(contracts.cliente.cli_telefono, telefono);
        values.put(contracts.cliente.cli_email, email);
        return values;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
