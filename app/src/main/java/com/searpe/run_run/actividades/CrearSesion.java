package com.searpe.run_run.actividades;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.searpe.run_run.Entidades.Firebase.Usuario;
import com.searpe.run_run.Persistencia.UsuarioDAO;
import com.searpe.run_run.R;
import com.searpe.run_run.recursos.Constantes;
import com.searpe.run_run.recursos.RecursosStatic;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class CrearSesion extends AppCompatActivity {
    private EditText et1, et2, et5, et6;
    private Button btn1;
    private CircleImageView fotoPerfil;
    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;
    private String pickerPath;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    public static Activity act;
    public RadioButton rb1, rb2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_sesion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        act = this;
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(Constantes.URL_INSTANCE);
        fotoPerfil = findViewById(R.id.fotoPerfil);
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et5 = findViewById(R.id.et5);
        et6 = findViewById(R.id.et6);
        btn1 = findViewById(R.id.btn1);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);


        et6.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        imagePicker = new ImagePicker(this);
        cameraPicker = new CameraImagePicker(this);
        cameraPicker.setCacheLocation(CacheLocation.EXTERNAL_STORAGE_APP_DIR);
        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                if (!list.isEmpty()) {
                    String path = list.get(0).getOriginalPath();
                    RecursosStatic.fotoPerfilUri = Uri.parse(path);
                    fotoPerfil.setImageURI(RecursosStatic.fotoPerfilUri);
                }
            }

            @Override
            public void onError(String s) {
                Toast.makeText(CrearSesion.this, "Error: " + s, Toast.LENGTH_SHORT).show();
            }
        });

        cameraPicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                String path = list.get(0).getOriginalPath();
                RecursosStatic.fotoPerfilUri = Uri.fromFile(new File(path));
                fotoPerfil.setImageURI(RecursosStatic.fotoPerfilUri);
            }

            @Override
            public void onError(String s) {
                Toast.makeText(CrearSesion.this, "Error: " + s, Toast.LENGTH_SHORT).show();
            }
        });

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(CrearSesion.this);
                dialog.setTitle("Foto de perfil");
                String[] items = {"Galeria", "Camara"};
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                imagePicker.pickImage();
                                break;
                            case 1:
                                pickerPath = cameraPicker.pickImage();
                                break;
                        }
                    }
                });
                AlertDialog dialogConstruido = dialog.create();
                dialogConstruido.show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Picker.PICK_IMAGE_DEVICE && resultCode == RESULT_OK) {
            imagePicker.submit(data);
        } else if (requestCode == Picker.PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {
            cameraPicker.reinitialize(pickerPath);
            cameraPicker.submit(data);
        }
    }

    private void attemptLogin() {
        et1.setError(null);
        et2.setError(null);
        et5.setError(null);
        et6.setError(null);
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(et1.getText().toString().trim())) {
            et1.setError("Este dato debe estar relleno");
            if (focusView == null)
                focusView = et1;
            cancel = true;
        }
        if (TextUtils.isEmpty(et2.getText().toString().trim())) {
            et2.setError("Este dato debe estar relleno");
            if (focusView == null)
                focusView = et2;
            cancel = true;
        }
        if (TextUtils.isEmpty(et5.getText().toString().trim()) || !isEmailValid(et5.getText().toString().trim())) {
            et5.setError("Este correo electrónico no es válido");
            if (focusView == null)
                focusView = et5;
            cancel = true;
        }
        if (TextUtils.isEmpty(et6.getText().toString().trim()) || !isPasswordValid(et6.getText().toString().trim())) {
            et6.setError("Esta contraseña no es válida");
            if (focusView == null)
                focusView = et6;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Usuario user = new Usuario();
            user.setNombre(et1.getText().toString());
            user.setApellido(et2.getText().toString());
            user.setCorreo(et5.getText().toString());
            user.setPass(et6.getText().toString());
            if (rb1.isChecked()){
                user.setSexo("Hombre");
            }else {
                user.setSexo("Mujer");
            }
            user.setNombreUser("Nick"+new Date().getTime());
            forceCreateAccountFireBase(user);
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

    private void forceCreateAccountFireBase(Usuario user) {
        AlertDialog pDialog = new ProgressDialog(CrearSesion.this);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);
        pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                return;
            }
        });
        pDialog.show();
        mAuth.createUserWithEmailAndPassword(user.getCorreo(), user.getPass())
                .addOnCompleteListener(CrearSesion.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (RecursosStatic.fotoPerfilUri != null) {
                                UsuarioDAO.getInstancia().subirFotoUri(RecursosStatic.fotoPerfilUri, new UsuarioDAO.IDevolverUrlFoto() {
                                    @Override
                                    public void devolerUrlString(String url) {
                                        try {
                                            Toast.makeText(CrearSesion.this, "Se registro correctamente.", Toast.LENGTH_SHORT).show();
                                            user.setFotoPerfilURL(url);
                                            user.setID(mAuth.getCurrentUser().getUid());
                                            DatabaseReference reference = database.getReference(Constantes.NODO_USUARIOS + user.getID());
                                            RecursosStatic.user = user;
                                            reference.setValue(RecursosStatic.user);
                                            //startForegroundService(new Intent(CrearSesion.this, MessageService.class));
                                            pDialog.dismiss();
                                            RecursosStatic.IntentUserRegister(act);
                                        }catch (Exception e){
                                            e.getStackTrace();
                                        }
                                    }
                                });

                            } else {
                                try {
                                    Toast.makeText(CrearSesion.this, "Se registro correctamente.", Toast.LENGTH_SHORT).show();
                                    user.setID(mAuth.getCurrentUser().getUid());
                                    user.setFotoPerfilURL(Constantes.URL_FOTO_POR_DEFECTO_USUARIOS);
                                    DatabaseReference reference = database.getReference(Constantes.NODO_USUARIOS + user.getID());
                                    RecursosStatic.user = user;
                                    reference.setValue(RecursosStatic.user);
                                    //startForegroundService(new Intent(CrearSesion.this, MessageService.class));
                                    pDialog.dismiss();
                                    RecursosStatic.IntentUserRegister(act);
                                }catch (Exception e){
                                    e.getStackTrace();
                                }
                            }
                        } else {
                            pDialog.dismiss();
                            Toast.makeText(CrearSesion.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EnableRuntimePermission();
    }

    public void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(CrearSesion.this, Manifest.permission.CAMERA)) {
        } else {
            ActivityCompat.requestPermissions(CrearSesion.this, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        super.onRequestPermissionsResult(RC, per, PResult);
        switch (RC) {
            case 1:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                break;
        }
    }
}