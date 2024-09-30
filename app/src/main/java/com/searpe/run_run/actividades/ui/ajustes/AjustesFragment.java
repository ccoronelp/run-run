package com.searpe.run_run.actividades.ui.ajustes;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
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
import com.searpe.run_run.actividades.Splash;
import com.searpe.run_run.databinding.FragmentAjustesBinding;
import com.searpe.run_run.recursos.Constantes;
import com.searpe.run_run.recursos.RecursosStatic;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AjustesFragment extends Fragment {
    private TextView tv2;
    private EditText et1, et2, et3, et4, et5, et6, et7, et8;
    private RadioButton rb1, rb2;
    private Button btn1;
    private ImageView fotoPerfil;
    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;
    private String pickerPath;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    public static Activity act;

    private FragmentAjustesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAjustesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(Constantes.URL_INSTANCE);
        fotoPerfil = binding.logo;
        tv2 = binding.tv2;
        et1 = binding.et1;
        et2 = binding.et2;
        et3 = binding.et3;
        et4 = binding.et4;
        et5 = binding.et5;
        et6 = binding.et6;
        et7 = binding.et7;
        et8 = binding.et8;
        btn1 = binding.btn1;
        rb1 = binding.rb1;
        rb2 = binding.rb2;
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                RecursosStatic.lanzarIntent(getActivity(), Splash.class);
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
                Toast.makeText(getActivity(), "Error: " + s, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Error: " + s, Toast.LENGTH_SHORT).show();
            }
        });
        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
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
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(200));
        Glide.with(this).load(RecursosStatic.user.getFotoPerfilURL()).apply(requestOptions).error(R.drawable.ic_outline_no_photography_24).into(fotoPerfil);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        asignarDatos();
    }

    @Override
    public void onStop() {
        super.onStop();
        //adapter.stopListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Picker.PICK_IMAGE_DEVICE && resultCode == getActivity().RESULT_OK) {
            imagePicker.submit(data);
        } else if (requestCode == Picker.PICK_IMAGE_CAMERA && resultCode == getActivity().RESULT_OK) {
            cameraPicker.reinitialize(pickerPath);
            cameraPicker.submit(data);
        }
    }

    private void attemptLogin() {
        et1.setError(null);
        et2.setError(null);
        et3.setError(null);
        et4.setError(null);
        et5.setError(null);
        et6.setError(null);
        et7.setError(null);
        et8.setError(null);
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
        if (TextUtils.isEmpty(et5.getText().toString().trim())) {
            et5.setError("Este dato debe estar relleno");
            if (focusView == null)
                focusView = et5;
            cancel = true;
        }
        if (TextUtils.isEmpty(et6.getText().toString().trim())) {
            et6.setError("Este dato debe estar relleno");
            if (focusView == null)
                focusView = et6;
            cancel = true;
        }
        if (TextUtils.isEmpty(et7.getText().toString().trim())) {
            et7.setError("Este dato debe estar relleno");
            if (focusView == null)
                focusView = et7;
            cancel = true;
        }
        if (TextUtils.isEmpty(et8.getText().toString().trim())) {
            et8.setError("Este dato debe estar relleno");
            if (focusView == null)
                focusView = et8;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Usuario user = RecursosStatic.user;
            user.setNombre(et1.getText().toString());
            user.setApellido(et2.getText().toString());
            user.setNombreUser(et3.getText().toString());
            user.setCalle(et4.getText().toString());
            user.setCP(et5.getText().toString());
            user.setCiudad(et6.getText().toString());
            user.setTelefono(et7.getText().toString());
            user.setEquipo(et8.getText().toString());
            if (rb1.isChecked()) {
                user.setSexo("Hombre");
            } else {
                user.setSexo("Mujer");
            }
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
        AlertDialog pDialog = new ProgressDialog(getActivity());
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
                    DatabaseReference reference = database.getReference(Constantes.NODO_USUARIOS + user.getID());
                    RecursosStatic.user = user;
                    reference.setValue(RecursosStatic.user);
                    pDialog.dismiss();
                    Toast.makeText(getActivity(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            user.setID(mAuth.getCurrentUser().getUid());
            DatabaseReference reference = database.getReference(Constantes.NODO_USUARIOS + user.getID());
            RecursosStatic.user = user;
            reference.setValue(RecursosStatic.user);
            pDialog.dismiss();
            Toast.makeText(getActivity(), "Perfil actualizado", Toast.LENGTH_SHORT).show();

        }

    }

    private void asignarDatos() {
        et1.setText(RecursosStatic.user.getNombre());
        et2.setText(RecursosStatic.user.getApellido());
        et3.setText(RecursosStatic.user.getNombreUser());
        et4.setText(RecursosStatic.user.getCalle());
        et5.setText(RecursosStatic.user.getCP());
        et6.setText(RecursosStatic.user.getCiudad());
        et7.setText(RecursosStatic.user.getTelefono());
        et8.setText(RecursosStatic.user.getEquipo());
        if (RecursosStatic.user.getSexo().equals("Hombre")) {
            rb1.setChecked(true);
        } else {
            rb2.setChecked(true);
        }
    }

    public void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
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