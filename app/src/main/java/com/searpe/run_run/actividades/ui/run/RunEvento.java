package com.searpe.run_run.actividades.ui.run;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.searpe.run_run.Entidades.clases.Estado;
import com.searpe.run_run.Entidades.clases.Evento;
import com.searpe.run_run.Entidades.clases.Participante;
import com.searpe.run_run.Holder.ResultadoViewHolder;
import com.searpe.run_run.R;
import com.searpe.run_run.adaptadores.RecycleViewAdapter;
import com.searpe.run_run.databinding.ActivityRunEventoBinding;
import com.searpe.run_run.recursos.Chronometer;
import com.searpe.run_run.recursos.Constantes;
import com.searpe.run_run.recursos.LocationService;
import com.searpe.run_run.recursos.RecursosStatic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RunEvento extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static Activity act;
    private ActivityRunEventoBinding binding;

    private Chronometer mChronometer;
    private float distanciaInicial = 0;
    private TextView tvTitle1, tvTitle2, tvTitle3;
    private LinearLayout ll1, ll2, ll3;
    private Button btn;
    private FirebaseDatabase database;
    public static RecycleViewAdapter adapter;
    private ArrayList<Participante> participantesArray;
    private RecyclerView rv;
    private Evento Evento;
    private Participante Participante;
    private TextView tvDistanceCurrent, tvDistanceTotal, tvPositionCurrent, tvPositionTotal, tvRitmo;
    private ProgressBar pb1;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRunEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setActionBar(binding.toolbar);
        act = this;
        database = FirebaseDatabase.getInstance(Constantes.URL_INSTANCE);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayUseLogoEnabled(true);


        tvTitle1 = findViewById(R.id.tvTitle1);
        tvTitle2 = findViewById(R.id.tvTitle2);
        tvTitle3 = findViewById(R.id.tvTitle3);
        btn = findViewById(R.id.btn1);
        rv = findViewById(R.id.rv1);
        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        tvDistanceCurrent = findViewById(R.id.tvDistanceCurrent);
        tvDistanceTotal = findViewById(R.id.tvDistanceTotal);
        tvPositionCurrent = findViewById(R.id.tvPositionCurrent);
        tvPositionTotal = findViewById(R.id.tvPositionTotal);
        pb1 = findViewById(R.id.pb1);
        tvRitmo = findViewById(R.id.tvRitmo);
        mChronometer = findViewById(R.id.chronometer);
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer chronometer) {

            }
        });
        participantesArray = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        rv.setLayoutManager(linearLayoutManager);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mChronometer.mStarted) {
                    if (RecursosStatic.conectadoWifi(act)) {
                        btn.setVisibility(View.INVISIBLE);
                        Toast.makeText(RunEvento.this, "Se ha iniciado la competición", Toast.LENGTH_LONG).show();
                        if (Participante == null) {
                            Participante = new Participante();
                            Participante.setIdEvento(Evento.getUid());
                            Participante.setIdUsuario(RecursosStatic.user.getID());
                            Participante.setDistancia(0);
                            Participante.setPuesto("0");
                            Participante.setNombre(RecursosStatic.user.getNombreUser());
                            Participante.setRitmo("--:--");
                            Participante.setTiempo(0);
                            Participante.setTiempoText("00:00:00");
                            Participante.setFoto(RecursosStatic.user.getFotoPerfilURL());
                            Participante.setCompletado(false);
                            DatabaseReference reference = database.getReference(Constantes.NODO_PARTICIPANTES).child(Evento.getUid()).child(RecursosStatic.user.getID());
                            reference.setValue(Participante);
                        } else {

                        }
                        mChronometer.start();
                        //followMeLocationSource.getBestAvailableProvider(Participante);
                        //setUpMapIfNeeded();
                        Intent servicio = new Intent(act, LocationService.class);
                        servicio.putExtra("participante", Participante);
                        servicio.putExtra("evento", Evento);
                        servicio.putExtra("distancia", distanciaInicial);
                        startService(servicio);
                    } else {
                        Toast.makeText(RunEvento.this, "Se ha perdido la conexión a internet", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        tvTitle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTitle1.setBackgroundColor(getColor(R.color.colorPrimaryPress));
                tvTitle2.setBackgroundColor(getColor(R.color.colorPrimary));
                tvTitle3.setBackgroundColor(getColor(R.color.colorPrimary));

                ll2.setVisibility(View.GONE);
                ll3.setVisibility(View.GONE);
                ll1.setVisibility(View.VISIBLE);
            }
        });
        tvTitle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTitle1.setBackgroundColor(getColor(R.color.colorPrimary));
                tvTitle2.setBackgroundColor(getColor(R.color.colorPrimaryPress));
                tvTitle3.setBackgroundColor(getColor(R.color.colorPrimary));
                ll1.setVisibility(View.GONE);
                ll3.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);
            }
        });
        tvTitle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTitle1.setBackgroundColor(getColor(R.color.colorPrimary));
                tvTitle2.setBackgroundColor(getColor(R.color.colorPrimary));
                tvTitle3.setBackgroundColor(getColor(R.color.colorPrimaryPress));
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.GONE);
                ll3.setVisibility(View.VISIBLE);
            }
        });
        tvTitle1.setBackgroundColor(getColor(R.color.colorPrimaryPress));


        try {
            Evento = (Evento) extras.getSerializable("object");
            if (Evento != null) {
                getActionBar().setTitle(Evento.getNombre());

            } else {
                finish();
            }
        } catch (Exception e) {
            finish();
        }
        DatabaseReference participantes = database
                .getReference()
                .child(Constantes.NODO_PARTICIPANTES).child(Evento.getUid());
        participantes.keepSynced(true);
        adapter = new RecycleViewAdapter(this, participantesArray);
        rv.setAdapter(adapter);
        participantes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Participante _Participante = null;
                String KEY = "";
                participantesArray.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    try {
                        KEY = dataSnapshot.getKey();
                        _Participante = postSnapshot.getValue(Participante.class);
                        _Participante.setOrden(Evento.getDistancia() * 1000 * _Participante.getTiempo() / _Participante.getDistancia());
                        participantesArray.add(_Participante);
                    } catch (Exception e) {

                    }
                }
                ordenarParticipantes();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        database.getReference(Constantes.NODO_PARTICIPANTES).child(Evento.getUid()).child(RecursosStatic.user.getID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Participante = dataSnapshot.getValue(Participante.class);
                if (Participante != null && !Participante.isCompletado()) {
                    SweetAlertDialog a = new SweetAlertDialog(act, SweetAlertDialog.NORMAL_TYPE).setCancelText("Sí").setConfirmText("No").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            distanciaInicial = Participante.getDistancia();
                            btn.performClick();
                            mChronometer.setBasePlus(Participante.getTiempo());
                            sweetAlertDialog.dismissWithAnimation();
                            asignarDatos();
                        }
                    });
                    a.setTitle("¡Atención!");
                    a.setContentText("Hay datos guardados de tu competición sin terminar ¿Deseas continuar por donde lo dejaste?");
                    a.show();
                } else {
                    Participante = null;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Participante = null;
                Estado estado = RecursosStatic.bbdd.getEstado(RecursosStatic.db, Evento.getUid());
                if (estado != null) {
                    SweetAlertDialog a = new SweetAlertDialog(act, SweetAlertDialog.NORMAL_TYPE).setCancelText("Sí").setConfirmText("No").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            distanciaInicial = estado.getDistancia();
                            btn.performClick();
                            mChronometer.setBasePlus(estado.getTiempo());
                            sweetAlertDialog.dismissWithAnimation();
                            asignarDatos();
                        }
                    });
                    a.setTitle("¡Atención!");
                    a.setContentText("Hay datos guardados del " + estado.getFecha() + " de tu competición sin terminar ¿Deseas continuar por donde lo dejaste?");
                    a.show();
                }
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private void ordenarParticipantes() {
        Collections.sort(participantesArray, new Comparator<Participante>() {
            @Override
            public int compare(Participante s1, Participante s2) {
                return s1.getOrden().compareTo(s2.getOrden());
            }
        });
        //Collections.reverse(participantesArray);
        tvPositionTotal.setText("/ "+participantesArray.size());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(act, "No está habilitado el permiso de ubicación", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
         mMap.setMyLocationEnabled(true);
        animateCamera();
        RecursosStatic.MAP = mMap;
        try {
            asignarDatos();
        } catch (Exception e) {
            finish();
        }

    }

    private void animateCamera() {
        Location location = getLastKnownLocation();
        if (location != null) {

            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            //delay is for after map loaded animation starts
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.5f));

                }
            }, 1000);
        }
    }

    private Location getLastKnownLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    /*private void setUpMapIfNeeded() {
        if (mMap != null) {
            mMap.setLocationSource(followMeLocationSource);
            mMap.moveCamera(CameraUpdateFactory.zoomTo(16.5f));
        }
    }*/

    private void asignarDatos() {
        tvDistanceCurrent.setText(RecursosStatic.formatter.format(distanciaInicial / 1000));
        tvDistanceTotal.setText(" / " + RecursosStatic.formatter.format(Evento.getDistancia()) + " km");
        tvPositionCurrent.setText("" + adapter.getItemCount());
        tvPositionTotal.setText("" + adapter.getItemCount());
        tvRitmo.setText("-- : --");
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (mChronometer.mRunning) {
            SweetAlertDialog a = new SweetAlertDialog(act, SweetAlertDialog.WARNING_TYPE).setCancelText("Sí").setConfirmText("No").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismissWithAnimation();
                }
            }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    mChronometer.stop();
                    //followMeLocationSource.deactivate();
                    Estado estado = new Estado();
                    estado.setUid(Evento.getUid());
                    estado.setFecha(RecursosStatic.dateFormatCompleto.format(new Date()));
                    //estado.setDistancia(followMeLocationSource.distancia);
                    estado.setTiempo(mChronometer.timeElapsed);
                    RecursosStatic.bbdd.deleteEstado(RecursosStatic.db, estado.getUid());
                    RecursosStatic.bbdd.InsertarEstado(RecursosStatic.db, estado);
                    RecursosStatic.IntentVerResultados();
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
            a.setTitle("¡Atención!");
            a.setContentText("¿Deseas salir de la competición? Se perderá su progreso no guardado.");
            a.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        //RecursosStatic.servicio.onDestroy();
        Intent servicio = new Intent(act, LocationService.class);
        stopService(servicio);
        super.onDestroy();
    }
}