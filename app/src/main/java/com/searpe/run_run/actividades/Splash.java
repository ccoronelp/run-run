package com.searpe.run_run.actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.searpe.run_run.Entidades.Logica.LUsuario;
import com.searpe.run_run.Persistencia.UsuarioDAO;
import com.searpe.run_run.R;
import com.searpe.run_run.recursos.Constantes;
import com.searpe.run_run.recursos.RecursosStatic;

import java.util.Timer;
import java.util.TimerTask;


public class Splash extends AppCompatActivity {


    private static final long SPLASH_SCREEN_DELAY = 3000;
    public static Activity act;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        act = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        RecursosStatic.initialize(this);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                load();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(Constantes.URL_INSTANCE);
    }


    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void load() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            UsuarioDAO.getInstancia().obtenerInformacionDeUsuarioPorLLave(currentUser.getUid(), new UsuarioDAO.IDevolverUsuario() {
                @Override
                public void devolverUsuario(LUsuario lUsuario) {
                    RecursosStatic.user = lUsuario.getUsuario();
                    //startForegroundService(new Intent(Splash.this, MessageService.class));
                    Intent mainIntent = new Intent().setClass(
                            Splash.this, MainActivity.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.finish();
                }

                @Override
                public void devolverError(String error) {
                    Toast.makeText(Splash.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent().setClass(
                            Splash.this, IniciarSesion.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.finish();
                }
            });

        } else {
            Intent mainIntent = new Intent().setClass(
                    Splash.this, IniciarSesion.class);
            Splash.this.startActivity(mainIntent);
            Splash.this.finish();
        }
    }
}
