package com.searpe.run_run.Entidades.clases;

import com.google.firebase.database.PropertyName;
import com.searpe.run_run.Entidades.Firebase.Usuario;

import java.io.Serializable;

/**
 * Created by user on 19/02/2018. 19
 */

public class Resultado implements Serializable {

    @PropertyName("fotoPerfilURL")
    private String fotoPerfilURL;
    @PropertyName("telefono")
    private String telefono = "";
    @PropertyName("correo")
    private String correo;
    @PropertyName("nombre")
    private String nombre;
    @PropertyName("apellido")
    private String apellido;
    @PropertyName("id")
    private String id;
    @PropertyName("sexo")
    private String sexo = "";
    @PropertyName("nombreUser")
    private String nombreUser = "";
    @PropertyName("calle")
    private String calle = "";
    @PropertyName("CP")
    private String CP = "";
    @PropertyName("ciudad")
    private String ciudad = "";
    @PropertyName("equipo")
    private String equipo = "";
    @PropertyName("uid")
    String uid;
    @PropertyName("fecha")
    String fecha;
    @PropertyName("localidad")
    String localidad;
    @PropertyName("hora")
    String hora;
    @PropertyName("descripcion")
    String descripcion;
    @PropertyName("premio")
    String premio;
    @PropertyName("campaña")
    String campaña;
    @PropertyName("foto")
    String foto;
    @PropertyName("nombreEvento")
    String nombreEvento;
    @PropertyName("tiempoText")
    String tiempoText;
    @PropertyName("puesto")
    String puesto;
    @PropertyName("ritmo")
    String ritmo;
    @PropertyName("distancia")
    float distancia;
    @PropertyName("distanciaTotal")
    float distanciaTotal;
    @PropertyName("datetime")
    long datetime;
    @PropertyName("datetimeCarrera")
    long datetimeCarrera;
    @PropertyName("tiempo")
    long tiempo;


    public Resultado() {
    }

    public Resultado(Usuario usuario, Evento evento) {
        fotoPerfilURL = usuario.getFotoPerfilURL();
        telefono = usuario.getTelefono();
        correo = usuario.getCorreo();
        nombre = usuario.getNombre();
        apellido = usuario.getApellido();
        id = usuario.getID();
        sexo = usuario.getSexo();
        nombreUser = usuario.getNombreUser();
        calle = usuario.getCalle();
        CP = usuario.getCP();
        ciudad = usuario.getCiudad();
        equipo = usuario.getEquipo();
        uid = evento.getUid();
        fecha = evento.getFecha();
        localidad = evento.getLocalidad();
        hora = evento.getHora();
        descripcion = evento.getDescripcion();
        premio = evento.getPremio();
        campaña = evento.getCampaña();
        foto = evento.getFoto();
        nombreEvento = evento.getNombre();
        distancia = evento.getDistancia();
        datetime = evento.getDatetime();
    }

    public float getDistanciaTotal() {
        return distanciaTotal;
    }

    public void setDistanciaTotal(float distanciaTotal) {
        this.distanciaTotal = distanciaTotal;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        uid = uid;
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

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public String getTiempoText() {
        return tiempoText;
    }

    public void setTiempoText(String tiempoText) {
        this.tiempoText = tiempoText;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public long getDatetimeCarrera() {
        return datetimeCarrera;
    }

    public void setDatetimeCarrera(long datetimeCarrera) {
        this.datetimeCarrera = datetimeCarrera;
    }

    public long getTiempo() {
        return tiempo;
    }

    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
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

    public String getFotoPerfilURL() {
        return fotoPerfilURL;
    }

    public void setFotoPerfilURL(String fotoPerfilURL) {
        this.fotoPerfilURL = fotoPerfilURL;
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
