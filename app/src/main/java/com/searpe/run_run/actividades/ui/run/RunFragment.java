package com.searpe.run_run.actividades.ui.run;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.searpe.run_run.Entidades.clases.Evento;
import com.searpe.run_run.adaptadores.ListViewFilterAdapter;
import com.searpe.run_run.databinding.FragmentRunBinding;
import com.searpe.run_run.recursos.RecursosStatic;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class RunFragment extends Fragment {

    private FragmentRunBinding binding;
    private ArrayList<Evento> eventos;
    private ListViewFilterAdapter adapter;
    private ListView lv1;
    private TextView tv1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRunBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        lv1 = binding.lv1;
        tv1 = binding.tv1;
        eventos = new ArrayList<Evento>();

        adapter = new ListViewFilterAdapter(getActivity(), eventos);
        lv1.setAdapter(adapter);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecursosStatic.lanzarIntent(getActivity(), RunEvento.class, adapter.getItem(position));
            }
        });
        lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                SweetAlertDialog a = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE).setCancelText("Sí").setConfirmText("No").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        sDialog.dismissWithAnimation();
                    }
                }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Evento evento = adapter.getItem(i);
                        RecursosStatic.bbdd.deleteEvento(RecursosStatic.db, evento);
                        eventos.clear();
                        RecursosStatic.bbdd.getEventos(RecursosStatic.db, eventos);
                        adapter.notifyDataSetChanged();
                        if (eventos.size() == 0) {
                            lv1.setVisibility(View.GONE);
                            tv1.setVisibility(View.VISIBLE);
                        }
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                a.setTitle("¡Atención!");
                a.setContentText("¿Desea borrar su registro para la competición?");
                a.show();
                return true;
            }
        });
        eventos.clear();
        RecursosStatic.bbdd.getEventos(RecursosStatic.db, eventos);
        if (eventos.size() == 0) {
            lv1.setVisibility(View.GONE);
            tv1.setVisibility(View.VISIBLE);
        } else if (eventos.size() == 1) {
            RecursosStatic.lanzarIntent(getActivity(), RunEvento.class, eventos.get(0));
            adapter.notifyDataSetChanged();
        } else {
            adapter.notifyDataSetChanged();
        }
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        eventos.clear();
        RecursosStatic.bbdd.getEventos(RecursosStatic.db, eventos);
        adapter.notifyDataSetChanged();
        if (eventos.size() == 0) {
            lv1.setVisibility(View.GONE);
            tv1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}