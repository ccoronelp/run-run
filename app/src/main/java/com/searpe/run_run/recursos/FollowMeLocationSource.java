package com.searpe.run_run.recursos;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.searpe.run_run.Entidades.clases.Evento;
import com.searpe.run_run.Entidades.clases.Participante;
import com.searpe.run_run.Entidades.clases.Resultado;
import com.searpe.run_run.R;
import com.searpe.run_run.actividades.ui.run.RunEvento;

import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FollowMeLocationSource implements LocationSource, LocationListener {
    Activity mContext;
    private OnLocationChangedListener mListener;
    private LocationManager locationManager;
    private final Criteria criteria = new Criteria();
    private String bestAvailableProvider;
    private final int minTime = 5000;
    private final int minDistance = 1;
    private Location lastLocation;
    public float distancia = 0;
    private GoogleMap mMap;
    private Participante Participante;
    private Chronometer mChronometer;
    private TextView tvDistanceCurrent, tvPositionCurrent, tvRitmo;
    DatabaseReference reference;
    ProgressBar pb1;
    Evento evento;


    public FollowMeLocationSource(Evento evento, Participante participante, float distancia, DatabaseReference reference) {
        mContext = RunEvento.act;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        this.mMap = RecursosStatic.MAP;
        this.mChronometer = mContext.findViewById(R.id.chronometer);
        this.reference = reference;
        this.tvDistanceCurrent = mContext.findViewById(R.id.tvDistanceCurrent);
        this.tvPositionCurrent = mContext.findViewById(R.id.tvPositionCurrent);
        this.tvRitmo = mContext.findViewById(R.id.tvRitmo);
        this.pb1 = mContext.findViewById(R.id.pb1);
        this.evento = evento;
        this.Participante = participante;
        this.distancia = distancia;

        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setSpeedRequired(true);
        criteria.setCostAllowed(true);
    }

    public void getBestAvailableProvider() {
        bestAvailableProvider = locationManager.getBestProvider(criteria, true);
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap != null) {
            mMap.setLocationSource(this);
            mMap.moveCamera(CameraUpdateFactory.zoomTo(16.5f));
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (bestAvailableProvider != null) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(bestAvailableProvider, minTime, minDistance, this);
        } else {
        }
    }

    @Override
    public void deactivate() {
        locationManager.removeUpdates(this);
        mListener = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mListener != null) {
            mListener.onLocationChanged(location);
        }
        if (lastLocation != null) {
            mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), new LatLng(location.getLatitude(), location.getLongitude()))
                    .width(5)
                    .color(Color.BLUE)
                    .geodesic(true)
                    .clickable(false));
            distancia += lastLocation.distanceTo(location);
            lastLocation = location;

            calcularCambios();
            updateParticipante(false);
            compruebaFin();

        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            lastLocation = location;

        }
    }

    private void compruebaFin() {
        if (distancia / 1000 >= evento.getDistancia()) {
            mChronometer.stop();
            deactivate();
            updateParticipante(true);
            createResultado();

        }
    }

    private void createResultado() {
        Resultado Resultado = new Resultado(RecursosStatic.user, evento);
        Resultado.setTiempo(mChronometer.timeElapsed);
        Resultado.setTiempoText(mChronometer.getText().toString());
        Resultado.setPuesto(tvPositionCurrent.getText().toString());
        Resultado.setRitmo(tvRitmo.getText().toString());
        Resultado.setDistancia(evento.getDistancia() * 1000);
        Resultado.setDistanciaTotal(evento.getDistancia());
        Resultado.setDatetimeCarrera(new Date().getTime());
        Resultado.setDatetime(evento.getDatetime());

        DatabaseReference reference = FirebaseDatabase.getInstance(Constantes.URL_INSTANCE).getReference(Constantes.NODO_RESULTADOS).child(RecursosStatic.user.getID()).child(evento.getUid());
        reference.setValue(Resultado);
        RecursosStatic.bbdd.deleteEstado(RecursosStatic.db, evento.getUid());
        RecursosStatic.bbdd.deleteEvento(RecursosStatic.db, evento);
        SweetAlertDialog a = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE).setCancelText("Salir").setConfirmText("Resultados").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                RecursosStatic.IntentVerResultados();
            }
        }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
        a.setTitle("¡Felicidades!");
        a.setContentText("Has completado el recorrido en el puesto " + Resultado.getPuesto() + ". Revisa tus resultados y vuelve a competir. ");
        a.show();

    }

    private void calcularCambios() {
        float porcentajePB = 0;
        porcentajePB = distancia / evento.getDistancia() / 10;
        tvDistanceCurrent.setText(RecursosStatic.formatter.format(distancia / 1000));
        pb1.setProgress((int) porcentajePB);
        if (lastLocation.hasSpeed())
            tvRitmo.setText(RecursosStatic.formatter.format(lastLocation.getSpeed() / 1000 * 3600));
        for (int i = 0; i < RunEvento.adapter.getItemCount(); i++) {
            Participante p = RunEvento.adapter.getItem(i);
            if (p.getIdUsuario().equals(Participante.getIdUsuario())) {
                tvPositionCurrent.setText("" + (i + 1));
            }
        }
    }

    private void updateParticipante(boolean fin) {
        Participante.setDistancia(distancia);
        Participante.setTiempo(mChronometer.timeElapsed);
        Participante.setTiempoText(mChronometer.getText().toString());
        Participante.setPuesto(tvPositionCurrent.getText().toString());
        Participante.setRitmo(tvRitmo.getText().toString());
        Participante.setCompletado(fin);
        if (Participante.isCompletado()) {
            Participante.setDistancia(evento.getDistancia() * 1000);
            //reference.removeValue();
            reference.setValue(Participante);
        } else {
            reference.setValue(Participante);
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }
}
