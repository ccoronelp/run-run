package com.searpe.run_run.Entidades.clases;

import java.io.Serializable;

public class Evento implements Serializable {
    String id, Uid, fecha, localidad, hora, descripcion, premio, campaña, foto, nombre;
    float distancia;
    long datetime;

    public Evento() {
    }

    public long getDatetime() {
        return datetime;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPremio() {
        return premio;
    }

    public void setPremio(String premio) {
        this.premio = premio;
    }

    public String getCampaña() {
        return campaña;
    }

    public void setCampaña(String campaña) {
        this.campaña = campaña;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }
}
