package com.searpe.run_run.Entidades.clases;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

public class Participante implements Serializable {
    @PropertyName("idEvento")
    String idEvento;
    @PropertyName("idUsuario")
    String idUsuario;
    @PropertyName("puesto")
    String puesto;
    @PropertyName("ritmo")
    String ritmo;
    @PropertyName("tiempoTex")
    String tiempoText;
    @PropertyName("nombre")
    String nombre;
    @PropertyName("foto")
    String foto;
    @PropertyName("tiempo")
    long tiempo;
    @PropertyName("distancia")
    float distancia;
    @PropertyName("completado")
    boolean completado;
    float orden;

    public Participante() {
    }

    public Float getOrden() {
        return orden;
    }

    public void setOrden(float orden) {
        this.orden = orden;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getRitmo() {
        return ritmo;
    }

    public void setRitmo(String ritmo) {
        this.ritmo = ritmo;
    }

    public String getTiempoText() {
        return tiempoText;
    }

    public void setTiempoText(String tiempoText) {
        this.tiempoText = tiempoText;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getTiempo() {
        return tiempo;
    }

    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }
}
