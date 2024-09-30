package com.searpe.run_run.BBDD;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.searpe.run_run.Entidades.clases.Estado;
import com.searpe.run_run.Entidades.clases.Evento;

import java.util.ArrayList;


/**
 * Created by sariza on 25/06/2015.
 */
public class BBDD extends SQLiteOpenHelper {

    String sqlCreateEvento = "CREATE TABLE Eventos (UID TEXT, nombre TEXT, fecha TEXT, hora TEXT, localidad TEXT, descripcion TEXT, premio TEXT, campaña TEXT, foto TEXT, distancia FLOAT)";
    String sqlCreateEstado = "CREATE TABLE Estados (UID TEXT, fecha TEXT, distancia FLOAT, tiempo INTEGER)";

    public BBDD(Context context, String name,
                SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(sqlCreateEvento);
        db.execSQL(sqlCreateEstado);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        try {
            if (versionAnterior == 1) {

            }
        } catch (Exception e) {
        }
    }

    public Evento getEvento(SQLiteDatabase db, String ID) {
        Evento Evento = null;
        Cursor c = null;
        try {
            c = db.rawQuery(
                    "SELECT * from Eventos where UID = '" + ID + "'",
                    null);
            if (c.moveToFirst()) {
                do {
                    Evento = new Evento();
                    Evento.setUid(c.getString(0));
                    Evento.setNombre(c.getString(1));
                    Evento.setFecha(c.getString(2));
                    Evento.setHora(c.getString(3));
                    Evento.setLocalidad(c.getString(4));
                    Evento.setDescripcion(c.getString(5));
                    Evento.setPremio(c.getString(6));
                    Evento.setCampaña(c.getString(7));
                    Evento.setFoto(c.getString(8));
                    Evento.setDistancia(c.getFloat(9));
                } while (c.moveToNext());
            }
        } finally {
            if (c != null)
                c.close();
        }
        return Evento;
    }

    public void getEventos(SQLiteDatabase db, ArrayList<Evento> Eventos) {
        Cursor c = null;
        try {
            c = db.rawQuery(
                    "SELECT * from Eventos",
                    null);
            if (c.moveToFirst()) {
                do {
                    Evento Evento = null;
                    Evento = new Evento();
                    Evento.setUid(c.getString(0));
                    Evento.setNombre(c.getString(1));
                    Evento.setFecha(c.getString(2));
                    Evento.setHora(c.getString(3));
                    Evento.setLocalidad(c.getString(4));
                    Evento.setDescripcion(c.getString(5));
                    Evento.setPremio(c.getString(6));
                    Evento.setCampaña(c.getString(7));
                    Evento.setFoto(c.getString(8));
                    Evento.setDistancia(c.getFloat(9));

                    Eventos.add(Evento);
                } while (c.moveToNext());
            }
        } finally {
            if (c != null)
                c.close();
        }
    }

    public String deleteEvento(SQLiteDatabase db, Evento Evento) {
        try {
            db.execSQL("delete from Eventos where UID = '" + Evento.getUid() + "'");
            return "";
        } catch (Exception e) {
            Log.e("error", e.getMessage());
            return e.getMessage();
        }
    }

    public String InsertarEvento(SQLiteDatabase dbEmpresa, Evento Evento) {
        try {
            if (dbEmpresa != null) {
                dbEmpresa.execSQL("INSERT INTO Eventos (UID , nombre , fecha , hora , localidad , descripcion , premio , campaña , foto ,distancia)\n" +
                        "     VALUES\n" +
                        "           ('" + Evento.getUid() + "'," +
                        "'" + Evento.getNombre() + "'," +
                        "'" + Evento.getFecha() + "'," +
                        "'" + Evento.getHora() + "'," +
                        "'" + Evento.getLocalidad() + "'," +
                        "'" + Evento.getDescripcion() + "'," +
                        "'" + Evento.getPremio() + "'," +
                        "'" + Evento.getCampaña() + "'," +
                        "'" + Evento.getFoto() + "'," +
                        "" + Evento.getDistancia() + ")");
            }
            return "";
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return e.getMessage();
        }
    }


    public Estado getEstado(SQLiteDatabase db, String ID) {
        Estado Estado = null;
        Cursor c = null;
        try {
            c = db.rawQuery(
                    "SELECT * from Estados where UID = '" + ID + "'",
                    null);
            if (c.moveToFirst()) {
                do {
                    Estado = new Estado();
                    Estado.setUid(c.getString(0));
                    Estado.setFecha(c.getString(1));
                    Estado.setDistancia(c.getFloat(2));
                    Estado.setTiempo(c.getLong(3));
                } while (c.moveToNext());
            }
        } finally {
            if (c != null)
                c.close();
        }
        return Estado;
    }

    public String deleteEstado(SQLiteDatabase db, String getUid) {
        try {
            db.execSQL("delete from Estados where UID = '" + getUid + "'");
            return "";
        } catch (Exception e) {
            Log.e("error", e.getMessage());
            return e.getMessage();
        }
    }

    public String InsertarEstado(SQLiteDatabase dbEmpresa, Estado Estado) {
        try {
            if (dbEmpresa != null) {
                dbEmpresa.execSQL("INSERT INTO Estados (UID, fecha, distancia, tiempo)\n" +
                        "     VALUES\n" +
                        "           ('" + Estado.getUid() + "','" + Estado.getFecha() + "'," + Estado.getDistancia() + "," + Estado.getTiempo() + ")");
            }
            return "";
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return e.getMessage();
        }
    }
}