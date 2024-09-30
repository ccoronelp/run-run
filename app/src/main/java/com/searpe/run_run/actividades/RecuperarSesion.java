package com.searpe.run_run.actividades;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.searpe.run_run.R;

public class RecuperarSesion extends AppCompatActivity {
    public static Activity act;
    Button btn;
    EditText et1;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_sesion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        btn = findViewById(R.id.btn1);
        et1 = findViewById(R.id.email);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et1.getText().toString())) {
                    Toast.makeText(RecuperarSesion.this, "Lo lamento, pero la cuenta de correo no es válida", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.sendPasswordResetEmail(et1.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RecuperarSesion.this, "Por favor, revise su correo para reiniciar su contraseña", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }else{
                                String mError = task.getException().getMessage();
                                Toast.makeText(RecuperarSesion.this, "Ha ocurrido un error: "+mError, Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
        act = this;
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