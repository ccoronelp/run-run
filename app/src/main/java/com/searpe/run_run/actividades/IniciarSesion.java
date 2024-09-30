package com.searpe.run_run.actividades;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.searpe.run_run.Entidades.Firebase.Usuario;
import com.searpe.run_run.Entidades.Logica.LUsuario;
import com.searpe.run_run.Persistencia.UsuarioDAO;
import com.searpe.run_run.R;
import com.searpe.run_run.recursos.Constantes;
import com.searpe.run_run.recursos.RecursosStatic;

public class IniciarSesion extends AppCompatActivity {
    private TextView tvRestablecer;
    private EditText et1, et2;
    private Button btn1, btn2;
    public static Activity act;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        act = this;
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(Constantes.URL_INSTANCE);
        tvRestablecer = findViewById(R.id.tvRestablecer);
        btn2 = findViewById(R.id.btn2);
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        btn1 = findViewById(R.id.btn1);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecursosStatic.lanzarIntent(IniciarSesion.this, CrearSesion.class);
                finish();
            }
        });
        tvRestablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecursosStatic.lanzarIntent(IniciarSesion.this, RecuperarSesion.class);

            }
        });
        et2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

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

    private void attemptLogin() {
        et1.setError(null);
        et2.setError(null);
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(et1.getText().toString().trim()) || !isEmailValid(et1.getText().toString().trim())) {
            et1.setError("Este correo electrónico no es válido");
            if (focusView == null)
                focusView = et1;
            cancel = true;
        }
        if (TextUtils.isEmpty(et2.getText().toString().trim()) || !isPasswordValid(et2.getText().toString().trim())) {
            et2.setError("Esta contraseña no es válida");
            if (focusView == null)
                focusView = et2;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            Usuario user = new Usuario();
            user.setCorreo(et1.getText().toString());
            user.setPass(et2.getText().toString());
            load(user);
        }

    }

    private void load(Usuario user) {
        AlertDialog pDialog = new ProgressDialog(IniciarSesion.this);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);
        pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        pDialog.show();
        mAuth.signInWithEmailAndPassword(user.getCorreo(), user.getPass())
                .addOnCompleteListener(IniciarSesion.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("LOG IN", "FIREBASE");
                            UsuarioDAO.getInstancia().obtenerInformacionDeUsuarioPorLLave(mAuth.getCurrentUser().getUid(), new UsuarioDAO.IDevolverUsuario() {
                                @Override
                                public void devolverUsuario(LUsuario lUsuario) {
                                    RecursosStatic.user = lUsuario.getUsuario();
                                    DatabaseReference reference = database.getReference(Constantes.NODO_USUARIOS + RecursosStatic.user.getID());
                                    pDialog.dismiss();
                                    RecursosStatic.IntentUserRegister(act);
                                }

                                @Override
                                public void devolverError(String error) {
                                    Toast.makeText(IniciarSesion.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                                    //startForegroundService(new Intent(IniciarSesion.this, MessageService.class));RecursosStatic.IntentUserRegister(IniciarSesion.this);
                                    pDialog.dismiss();
                                }
                            });
                        } else {
                            Log.e("LOG OFF", "FIREBASE");
                            et2.setText("");
                            et1.setError("Las credenciales usadas son incorrectas");
                            et1.requestFocus();
                            pDialog.dismiss();
                        }
                    }
                });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) ==
                PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }

}