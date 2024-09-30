package com.searpe.run_run.actividades;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.searpe.run_run.Entidades.clases.Evento;
import com.searpe.run_run.R;
import com.searpe.run_run.databinding.ActivityMainBinding;
import com.searpe.run_run.recursos.RecursosStatic;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        act = this;
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public void onBackPressed() {
        SweetAlertDialog a = new SweetAlertDialog(act, SweetAlertDialog.WARNING_TYPE).setCancelText("Sí").setConfirmText("No").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                sDialog.dismissWithAnimation();
            }
        }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                finish();
                sweetAlertDialog.dismissWithAnimation();
            }
        });
        a.setTitle("¡Atención!");
        a.setContentText("¿Desea salir de la aplicación?");
        a.show();

    }
}