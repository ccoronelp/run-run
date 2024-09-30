package com.searpe.run_run.actividades.ui.competencias;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.searpe.run_run.Entidades.clases.Resultado;
import com.searpe.run_run.R;
import com.searpe.run_run.recursos.RecursosStatic;

public class CompetenciaResultado extends AppCompatActivity {
    private TextView tvChrono, tvDistanceCurrent, tvDistanceTotal, tvPositionCurrent, tvPositionTotal, tvRitmo;
    private ProgressBar pb1;
    public static Activity act;
    Resultado resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competencia_resultado_full);
        act = this;
        tvDistanceCurrent = findViewById(R.id.tvDistanceCurrent);
        tvDistanceTotal = findViewById(R.id.tvDistanceTotal);
        tvPositionCurrent = findViewById(R.id.tvPositionCurrent);
        tvPositionTotal = findViewById(R.id.tvPositionTotal);
        tvRitmo = findViewById(R.id.tvRitmo);
        tvChrono = findViewById(R.id.chronometer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
        }
        try {
            resultado = (Resultado) extras.getSerializable("object");
            if (resultado != null) {
                asignarDatos();
            } else {
                resultado = null;
            }
        } catch (Exception e) {
            resultado = null;
        }
    }

    private void asignarDatos() {
        tvChrono.setText(resultado.getTiempoText());
        tvDistanceCurrent.setText(RecursosStatic.formatter.format(resultado.getDistancia()/1000));
        tvDistanceTotal.setText("/ "+RecursosStatic.formatter.format(resultado.getDistanciaTotal()) + " km");
        tvRitmo.setText(resultado.getRitmo());
        tvPositionCurrent.setText(resultado.getPuesto());
        getSupportActionBar().setTitle(resultado.getNombreEvento());
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