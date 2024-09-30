package com.searpe.run_run.actividades;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    private TextView tv2;
    private EditText et1, et2, et3, et4;
    private Button btn1;
    private CircleImageView fotoPerfil;
    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;
    private String pickerPath;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    public static Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        act = this;
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(Constantes.URL_INSTANCE);
        fotoPerfil = findViewById(R.id.logo);
        tv2 = findViewById(R.id.tv2);
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        btn1 = findViewById(R.id.btn1);
        tv2.setPaintFlags(tv2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                RecursosStatic.lanzarIntent(Profile.this, Splash.class);
                RecursosStatic.cerrarTodasActivities();
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
                Toast.makeText(Profile.this, "Error: " + s, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Profile.this, "Error: " + s, Toast.LENGTH_SHORT).show();
            }
        });

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Profile.this);
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
        Glide.with(this).load(RecursosStatic.user.getFotoPerfilURL()).error(R.drawable.ic_outline_no_photography_24).into(fotoPerfil);
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
        et3.setError(null);
        et4.setError(null);
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
        if (TextUtils.isEmpty(et3.getText().toString().trim())) {
            et3.setError("Este dato debe estar relleno");
            if (focusView == null)
                focusView = et3;
            cancel = true;
        }
        if (TextUtils.isEmpty(et4.getText().toString().trim())) {
            et4.setError("Este dato debe estar relleno");
            if (focusView == null)
                focusView = et4;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Usuario user = RecursosStatic.user;
            user.setNombre(et1.getText().toString());
            user.setApellido(et2.getText().toString());
            user.setTelefono(et4.getText().toString());
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
        AlertDialog pDialog = new ProgressDialog(Profile.this);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);
        pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        pDialog.show();

        if (RecursosStatic.fotoPerfilUri != null) {
            UsuarioDAO.getInstancia().subirFotoUri(RecursosStatic.fotoPerfilUri, new UsuarioDAO.IDevolverUrlFoto() {
                @Override
                public void devolerUrlString(String url) {
                    user.setFotoPerfilURL(url);
                    user.setID(mAuth.getCurrentUser().getUid());
                    DatabaseReference reference = database.getReference(Constantes.NODO_USUARIOS + user.getID());
                    RecursosStatic.user = user;
                    reference.setValue(RecursosStatic.user);
                    pDialog.dismiss();
                    finish();
                }
            });

        } else {
            user.setID(mAuth.getCurrentUser().getUid());
            //user.setFotoPerfilURL(Constantes.URL_FOTO_POR_DEFECTO_USUARIOS);
            DatabaseReference reference = database.getReference(Constantes.NODO_USUARIOS + user.getID());
             RecursosStatic.user = user;
            reference.setValue(RecursosStatic.user);
            pDialog.dismiss();
            finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        EnableRuntimePermission();
        asignarDatos();
    }

    private void asignarDatos() {
        et1.setText(RecursosStatic.user.getNombre());
        et2.setText(RecursosStatic.user.getApellido());
        et4.setText(RecursosStatic.user.getTelefono());
    }

    public void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Profile.this, Manifest.permission.CAMERA)) {
        } else {
            ActivityCompat.requestPermissions(Profile.this, new String[]{Manifest.permission.CAMERA}, 1);
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