package com.searpe.run_run.Entidades.clases;

public class Estado {

    private String Uid;
    private String fecha;
    private float distancia;
    private long tiempo;

    public Estado() {
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    public long getTiempo() {
        return tiempo;
    }

    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }
}
