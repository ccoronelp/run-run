package com.searpe.run_run.actividades.ui.competencias;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.searpe.run_run.Entidades.clases.Evento;
import com.searpe.run_run.Entidades.clases.Resultado;
import com.searpe.run_run.Holder.EventoViewHolder;
import com.searpe.run_run.Holder.ResultadoViewHolder;
import com.searpe.run_run.R;
import com.searpe.run_run.databinding.FragmentCompetenciasBinding;
import com.searpe.run_run.recursos.Constantes;
import com.searpe.run_run.recursos.RecursosStatic;

import java.util.Date;


public class CompetenciasFragment extends Fragment {

    private RecyclerView rv;
    private FloatingActionButton fab;
    private FirebaseRecyclerAdapter adapter;
    private FirebaseRecyclerAdapter adapterResultados;
    private EditText et1;
    private TextView tv1, tv2, tv3, tv4;
    private FirebaseDatabase database;
    private FragmentCompetenciasBinding binding;
    private boolean editar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentCompetenciasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        rv = binding.rv1;
        et1 = binding.et1;
        fab = binding.fab;
        tv1 = binding.tv1;
        tv2 = binding.tv2;
        tv3 = binding.tv3;
        tv4 = binding.tv4;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(linearLayoutManager);
        database = FirebaseDatabase.getInstance(Constantes.URL_INSTANCE);
        long time = System.currentTimeMillis();
        Query query = database
                .getReference()
                .child(Constantes.NODO_EVENTOS).orderByChild("datetime")
                .endAt(time);
        FirebaseRecyclerOptions<Evento> options =
                new FirebaseRecyclerOptions.Builder<Evento>()
                        .setQuery(query, Evento.class)
                        .build();


        Query queryResultado = database
                .getReference()
                .child(Constantes.NODO_RESULTADOS).child(RecursosStatic.user.getID()).orderByChild("datetimeCarrera");
        FirebaseRecyclerOptions<Resultado> optionsResultado =
                new FirebaseRecyclerOptions.Builder<Resultado>()
                        .setQuery(queryResultado, Resultado.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Evento, EventoViewHolder>(options) {
            @Override
            public EventoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_evento, parent, false);
                return new EventoViewHolder(view);
            }

            @NonNull
            @Override
            public ObservableSnapshotArray<Evento> getSnapshots() {
                return super.getSnapshots();
            }

            @Override
            protected void onBindViewHolder(EventoViewHolder holder, int position, final Evento model) {
                String cadena = et1.getText().toString();
                if (!model.getNombre().toLowerCase().contains(cadena.toLowerCase()) && !cadena.equals("")) {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                } else {
                    holder.itemView.setVisibility(View.VISIBLE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    holder.getTv1().setText(model.getNombre());
                    holder.getTv2().setText(model.getFecha());
                    holder.getTv3().setText(model.getHora());
                    holder.getTv4().setText(RecursosStatic.formatter.format(model.getDistancia()) + " km");
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
                    Glide.with(getActivity()).load(model.getFoto()).apply(requestOptions).error(R.drawable.ic_outline_no_photography_24).into(holder.getFoto());
                    holder.getBtn1().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            RecursosStatic.lanzarIntent(getActivity(), CompetenciaParticipar.class, model);
                        }
                    });
                    holder.getCv1().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (editar) {
                                RecursosStatic.lanzarIntent(getActivity(), CompetenciaCrear.class, model);
                            } else {
                                RecursosStatic.lanzarIntent(getActivity(), CompetenciaParticipar.class, model);
                            }
                        }
                    });
                }
            }
        };
        adapterResultados = new FirebaseRecyclerAdapter<Resultado, ResultadoViewHolder>(optionsResultado) {
            @Override
            public ResultadoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_evento_resultado, parent, false);
                return new ResultadoViewHolder(view);
            }

            @NonNull
            @Override
            public ObservableSnapshotArray<Resultado> getSnapshots() {
                return super.getSnapshots();
            }

            @Override
            protected void onBindViewHolder(ResultadoViewHolder holder, int position, final Resultado model) {
                String cadena = et1.getText().toString();
                if (!model.getNombre().toLowerCase().contains(cadena.toLowerCase()) && !cadena.equals("")) {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                } else {
                    holder.itemView.setVisibility(View.VISIBLE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    holder.getTv1().setText(model.getNombreEvento());
                    holder.getTv2().setText((RecursosStatic.dateFormat.format(new Date(model.getDatetimeCarrera()))));
                    holder.getTv3().setText(model.getTiempoText());
                    holder.getTv4().setText(RecursosStatic.formatter.format(model.getDistancia() / 1000) + " km\n"+model.getRitmo()+" km/h");
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
                    Glide.with(getActivity()).load(model.getFoto()).apply(requestOptions).error(R.drawable.ic_outline_no_photography_24).into(holder.getFoto());
                    holder.getCv1().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                               RecursosStatic.lanzarIntent(getActivity(), CompetenciaResultado.class, model);
                        }
                    });
                }
            }
        };
        rv.setAdapter(adapter);
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.notifyDataSetChanged();
                adapterResultados.notifyDataSetChanged();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecursosStatic.lanzarIntent(getActivity(), CompetenciaCrear.class);
            }
        });
        tv1.setBackgroundColor(getActivity().getColor(R.color.colorPrimaryPress));
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv1.setBackgroundColor(getActivity().getColor(R.color.colorPrimaryPress));
                tv2.setBackgroundColor(getActivity().getColor(R.color.colorPrimary));
                tv3.setBackgroundColor(getActivity().getColor(R.color.colorPrimary));
                tv4.setBackgroundColor(getActivity().getColor(R.color.colorPrimary));
                long time = System.currentTimeMillis();
                Query query = database
                        .getReference()
                        .child(Constantes.NODO_EVENTOS).orderByChild("datetime")
                        .endAt(time);
                FirebaseRecyclerOptions<Evento> options =
                        new FirebaseRecyclerOptions.Builder<Evento>()
                                .setQuery(query, Evento.class)
                                .build();
                adapter.updateOptions(options);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                editar = false;
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv1.setBackgroundColor(getActivity().getColor(R.color.colorPrimary));
                tv2.setBackgroundColor(getActivity().getColor(R.color.colorPrimaryPress));
                tv3.setBackgroundColor(getActivity().getColor(R.color.colorPrimary));
                tv4.setBackgroundColor(getActivity().getColor(R.color.colorPrimary));
                long time = System.currentTimeMillis();
                Query query = database
                        .getReference()
                        .child(Constantes.NODO_EVENTOS).orderByChild("datetime")
                        .startAt(time);
                FirebaseRecyclerOptions<Evento> options =
                        new FirebaseRecyclerOptions.Builder<Evento>()
                                .setQuery(query, Evento.class)
                                .build();
                adapter.updateOptions(options);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                editar = false;
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv1.setBackgroundColor(getActivity().getColor(R.color.colorPrimary));
                tv2.setBackgroundColor(getActivity().getColor(R.color.colorPrimary));
                tv3.setBackgroundColor(getActivity().getColor(R.color.colorPrimaryPress));
                tv4.setBackgroundColor(getActivity().getColor(R.color.colorPrimary));
                Query query = database
                        .getReference()
                        .child(Constantes.NODO_RESULTADOS).child(RecursosStatic.user.getID()).orderByChild("datetimeCarrera");
                FirebaseRecyclerOptions<Resultado> options =
                        new FirebaseRecyclerOptions.Builder<Resultado>()
                                .setQuery(query, Resultado.class)
                                .build();
                adapterResultados.updateOptions(options);
                rv.setAdapter(adapterResultados);
                adapterResultados.notifyDataSetChanged();
                editar = false;
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv1.setBackgroundColor(getActivity().getColor(R.color.colorPrimary));
                tv2.setBackgroundColor(getActivity().getColor(R.color.colorPrimary));
                tv3.setBackgroundColor(getActivity().getColor(R.color.colorPrimary));
                tv4.setBackgroundColor(getActivity().getColor(R.color.colorPrimaryPress));
                Query query = database
                        .getReference()
                        .child(Constantes.NODO_EVENTOS).orderByChild("id").equalTo(RecursosStatic.user.getID());
                FirebaseRecyclerOptions<Evento> options =
                        new FirebaseRecyclerOptions.Builder<Evento>()
                                .setQuery(query, Evento.class)
                                .build();
                adapter.updateOptions(options);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                editar = true;
            }
        });
        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        adapter.notifyDataSetChanged();
        adapterResultados.startListening();
        adapterResultados.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        adapterResultados.stopListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}