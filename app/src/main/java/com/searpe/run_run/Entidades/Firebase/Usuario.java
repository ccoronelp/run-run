package com.searpe.run_run.Entidades.Firebase;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

/**
 * Created by user on 19/02/2018. 19
 */

public class Usuario implements Serializable {

    @PropertyName("fotoPerfilURL")
    private String fotoPerfilURL;
    @PropertyName("pass")
    private String pass;
    @PropertyName("telefono")
    private String telefono="";
    @PropertyName("correo")
    private String correo;
    @PropertyName("nombre")
    private String nombre;
    @PropertyName("apellido")
    private String apellido;
    @PropertyName("id")
    private String id;
    @PropertyName("createdTimestamp")
    private Object createdTimestamp;
    @PropertyName("sexo")
    private String sexo="";
    @PropertyName("nombreUser")
    private String nombreUser="";
    @PropertyName("calle")
    private String calle="";
    @PropertyName("CP")
    private String CP="";
    @PropertyName("ciudad")
    private String ciudad="";
    @PropertyName("equipo")
    private String equipo="";


    public Usuario() {
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNombreUser() {
        return nombreUser;
    }

    public void setNombreUser(String nombreUser) {
        this.nombreUser = nombreUser;
    }

    public String getCP() {
        return CP;
    }

    public void setCP(String CP) {
        this.CP = CP;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public Object getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Object createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getFotoPerfilURL() {
        return fotoPerfilURL;
    }

    public void setFotoPerfilURL(String fotoPerfilURL) {
        this.fotoPerfilURL = fotoPerfilURL;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }
}
