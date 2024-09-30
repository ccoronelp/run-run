package com.searpe.run_run.actividades.ui.competencias;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.searpe.run_run.Entidades.clases.Evento;
import com.searpe.run_run.Entidades.clases.Participante;
import com.searpe.run_run.R;
import com.searpe.run_run.actividades.ui.run.RunEvento;
import com.searpe.run_run.adaptadores.RecycleViewAdapter;
import com.searpe.run_run.recursos.Constantes;
import com.searpe.run_run.recursos.RecursosStatic;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

public class CompetenciaParticipar extends AppCompatActivity {
    public static Activity act;
    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8;
    private TextView tvTitle1, tvTitle2, tvTitle3;
    private ImageView img, img2;
    private LinearLayout ll1, ll2;
    private ScrollView sv1;
    private Button btn, btn2;
    private FirebaseDatabase database;
    private RecycleViewAdapter adapter;
    private ArrayList<Participante> participantesArray;
    private RecyclerView rv;
    Evento Evento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competencia_participar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        database = FirebaseDatabase.getInstance(Constantes.URL_INSTANCE);
        act = this;
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);
        tv7 = findViewById(R.id.tv7);
        tv8 = findViewById(R.id.tv8);


        tvTitle1 = findViewById(R.id.tvTitle1);
        tvTitle2 = findViewById(R.id.tvTitle2);
        tvTitle3 = findViewById(R.id.tvTitle3);

        rv = findViewById(R.id.rv1);
        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        sv1 = findViewById(R.id.sv1);

        img = findViewById(R.id.fotoPerfil);
        img2 = findViewById(R.id.fotoCompartir);
        btn = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        participantesArray = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
        }
        try {
            Evento = (Evento) extras.getSerializable("object");
            if (Evento != null) {
                asignarDatos();
            } else {
                onBackPressed();
            }
        } catch (Exception e) {
            onBackPressed();
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RecursosStatic.bbdd.getEvento(RecursosStatic.db, Evento.getUid()) == null)
                    RecursosStatic.bbdd.InsertarEvento(RecursosStatic.db, Evento);
                RecursosStatic.lanzarIntent(act, RunEvento.class, Evento);

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage();
            }
        });
        tvTitle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTitle1.setBackgroundColor(getColor(R.color.colorPrimaryPress));
                tvTitle2.setBackgroundColor(getColor(R.color.colorPrimary));
                tvTitle3.setBackgroundColor(getColor(R.color.colorPrimary));

                ll2.setVisibility(View.GONE);
                ll1.setVisibility(View.GONE);
                sv1.setVisibility(View.VISIBLE);
            }
        });
        tvTitle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTitle1.setBackgroundColor(getColor(R.color.colorPrimary));
                tvTitle2.setBackgroundColor(getColor(R.color.colorPrimaryPress));
                tvTitle3.setBackgroundColor(getColor(R.color.colorPrimary));
                ll1.setVisibility(View.GONE);
                sv1.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);
            }
        });
        tvTitle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTitle1.setBackgroundColor(getColor(R.color.colorPrimary));
                tvTitle2.setBackgroundColor(getColor(R.color.colorPrimary));
                tvTitle3.setBackgroundColor(getColor(R.color.colorPrimaryPress));
                sv1.setVisibility(View.GONE);
                ll2.setVisibility(View.GONE);
                ll1.setVisibility(View.VISIBLE);
            }
        });
        tvTitle1.setBackgroundColor(getColor(R.color.colorPrimaryPress));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        rv.setLayoutManager(linearLayoutManager);
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
                participantesArray.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    try {
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
    }

    private void ordenarParticipantes() {
        /*ArrayList<Participante> listCompletada = new ArrayList<>(), listNoCompletada = new ArrayList<>();
        for (Participante item : participantesArray) {
            if (item.isCompletado()) {
                listCompletada.add(item);
            } else {
                item.setOrden(Evento.getDistancia() * 1000 * item.getTiempo() / item.getDistancia());
                listNoCompletada.add(item);

            }
        }

        Collections.sort(listCompletada, new Comparator<Participante>() {
            @Override
            public int compare(Participante s1, Participante s2) {
                return s1.getTiempo().compareTo(s2.getTiempo());
            }
        });
        Collections.sort(listCompletada, new Comparator<Participante>() {
            @Override
            public int compare(Participante s1, Participante s2) {
                return s1.getOrden().compareTo(s2.getOrden());
            }
        });
        participantesArray.clear();
        participantesArray.addAll(listCompletada);
        participantesArray.addAll(listNoCompletada);*/
        Collections.sort(participantesArray, new Comparator<Participante>() {
            @Override
            public int compare(Participante s1, Participante s2) {
                return s1.getOrden().compareTo(s2.getOrden());
            }
        });
        //Collections.reverse(participantesArray);
        adapter.notifyDataSetChanged();
    }

    private void shareImage() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img2.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        Uri uri = getImageToShare(bitmap);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        //intent.putExtra(Intent.EXTRA_TEXT,"Compartiendo foto");
        //intent.putExtra(Intent.EXTRA_SUBJECT,"Invitacion");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent, "Share Image"));
    }

    private Uri getImageToShare(Bitmap bitmap) {
        File folder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            folder.mkdirs();
            File file = new File(folder, "shared_image.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            uri = FileProvider.getUriForFile(this, "com.searpe.run_run", file);
        } catch (Exception e) {
            Toast.makeText(act, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return uri;
    }

    private void asignarDatos() {
        getSupportActionBar().setTitle(Evento.getNombre());
        tv1.setText(Evento.getNombre());
        tv2.setText(Evento.getFecha());
        tv3.setText(Evento.getHora());
        tv4.setText(RecursosStatic.formatter.format(Evento.getDistancia()) + " km");
        tv5.setText(Evento.getLocalidad());
        tv6.setText(Evento.getDescripcion());
        tv7.setText(Evento.getCampaña());
        tv8.setText(Evento.getPremio());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
        Glide.with(act).load(Evento.getFoto()).apply(requestOptions).error(R.drawable.ic_outline_no_photography_24).into(img);
        Glide.with(act).load(Evento.getFoto()).apply(requestOptions).error(R.drawable.ic_outline_no_photography_24).into(img2);
        long time = System.currentTimeMillis();
        if (time > Evento.getDatetime()) {
            btn.setText("Empezar ahora");
        } else {
            long disponible = Evento.getDatetime() - time;
            long days = TimeUnit.MILLISECONDS.toDays(disponible);
            btn.setText("Disponible en " + days + " días");
            btn.setEnabled(false);
        }
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
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}