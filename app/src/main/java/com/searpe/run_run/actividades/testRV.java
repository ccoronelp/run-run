package com.searpe.run_run.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.searpe.run_run.Entidades.clases.Participante;
import com.searpe.run_run.Holder.ResultadoViewHolder;
import com.searpe.run_run.R;
import com.searpe.run_run.recursos.Constantes;

public class testRV extends AppCompatActivity {
    FirebaseRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_rv);
        RecyclerView rv = findViewById(R.id.rv);
        FirebaseDatabase database;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(testRV.this);
        rv.setLayoutManager(linearLayoutManager);
        database = FirebaseDatabase.getInstance(Constantes.URL_INSTANCE);
        Query query = database
                .getReference()
                .child(Constantes.NODO_PARTICIPANTES).child("1234");
        FirebaseRecyclerOptions<Participante> options =
                new FirebaseRecyclerOptions.Builder<Participante>()
                        .setQuery(query, Participante.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Participante, ResultadoViewHolder>(options) {
            @Override
            public ResultadoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_resultados, parent, false);
                return new ResultadoViewHolder(view);
            }

            @Override
            public ObservableSnapshotArray<Participante> getSnapshots() {
                return super.getSnapshots();
            }

            @Override
            protected void onBindViewHolder(ResultadoViewHolder holder, int position, final Participante model) {

                holder.itemView.setVisibility(View.VISIBLE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.getTv1().setText(model.getNombre());
                holder.getTv2().setText(model.getTiempoText());
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
                Glide.with(testRV.this).load(model.getFoto()).apply(requestOptions).error(R.drawable.ic_outline_no_photography_24).into(holder.getFoto());
            }
        };
        rv.setAdapter(adapter);
        rv.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }
}