package com.searpe.run_run.actividades.ui.competencias;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.searpe.run_run.Entidades.clases.Evento;
import com.searpe.run_run.Persistencia.UsuarioDAO;
import com.searpe.run_run.R;
import com.searpe.run_run.recursos.Constantes;
import com.searpe.run_run.recursos.RecursosStatic;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;


public class CompetenciaCrear extends AppCompatActivity {
    Evento Evento;
    public static Activity act;
    EditText et1, et2, et3, et4, et5, et6, et7, et8;
    ImageView img;
    Button btn;
    private FirebaseDatabase database;

    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;
    private String pickerPath, urlPublica = "";
    private Uri foto;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competencia_crear);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        database = FirebaseDatabase.getInstance(Constantes.URL_INSTANCE);
        act = this;
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        et5 = findViewById(R.id.et5);

        et6 = findViewById(R.id.et6);
        et7 = findViewById(R.id.et7);
        et8 = findViewById(R.id.et8);

        img = findViewById(R.id.fotoPerfil);
        btn = findViewById(R.id.btn1);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
        }
        try {
            Evento = (Evento) extras.getSerializable("object");
            if (Evento != null) {
                asignarDatos();
            } else {
                Evento = null;
            }
        } catch (Exception e) {
            Evento = null;
        }
        btn.setOnClickListener(new View.OnClickListener() {
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
                    foto = Uri.parse(path);
                    img.setImageURI(foto);
                    AlertDialog pDialog = new ProgressDialog(act);
                    pDialog.setMessage("Cargando...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    UsuarioDAO.getInstancia().subirFotoUri(foto, new UsuarioDAO.IDevolverUrlFoto() {
                        @Override
                        public void devolerUrlString(String url) {
                            if (url.equals("ERROR")) {
                                pDialog.dismiss();
                                Toast.makeText(CompetenciaCrear.this, "Error al subir la foto", Toast.LENGTH_SHORT).show();

                            } else {
                                urlPublica = url;
                                pDialog.dismiss();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(String s) {
                Toast.makeText(CompetenciaCrear.this, "Error: " + s, Toast.LENGTH_SHORT).show();
            }
        });
        cameraPicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                String path = list.get(0).getOriginalPath();
                foto = Uri.fromFile(new File(path));
                img.setImageURI(foto);
                AlertDialog pDialog = new ProgressDialog(act);
                pDialog.setMessage("Cargando...");
                pDialog.setCancelable(false);
                pDialog.show();
                UsuarioDAO.getInstancia().subirFotoUri(foto, new UsuarioDAO.IDevolverUrlFoto() {
                    @Override
                    public void devolerUrlString(String url) {
                        urlPublica = url;
                        pDialog.dismiss();

                    }
                });
            }

            @Override
            public void onError(String s) {
                Toast.makeText(CompetenciaCrear.this, "Error: " + s, Toast.LENGTH_SHORT).show();
            }
        });
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(CompetenciaCrear.this);
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
        et7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CompetenciaCrear.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        et8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog =
                        new TimePickerDialog(CompetenciaCrear.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                //myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                //myCalendar.set(Calendar.MINUTE, minute);
                                String time = RecursosStatic.formatterHora.format(hourOfDay) + ":" + RecursosStatic.formatterHora.format(minute);
                                et8.setText(time);
                            }
                        }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });
    }

    private void updateLabel() {
        et7.setText(RecursosStatic.dateFormat.format(myCalendar.getTime()));
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

    private void asignarDatos() {
        urlPublica = Evento.getFoto();
        et1.setText(Evento.getNombre());
        et2.setText(Evento.getLocalidad());
        et3.setText(Evento.getPremio());
        et4.setText(Evento.getDescripcion());
        et5.setText(Evento.getCampaña());
        et6.setText(RecursosStatic.formatter.format(Evento.getDistancia()));
        et7.setText(Evento.getFecha());
        et8.setText(Evento.getHora());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
        Glide.with(act).load(Evento.getFoto()).apply(requestOptions).error(R.drawable.ic_outline_no_photography_24).into(img);
    }

    private void attemptLogin() {
        et1.setError(null);
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


        if (cancel) {
            focusView.requestFocus();
        } else {
            String error = "";
            if (Evento != null) {
                Evento.setNombre(et1.getText().toString());
                Evento.setLocalidad(et2.getText().toString());
                Evento.setPremio(et3.getText().toString());
                Evento.setDescripcion(et4.getText().toString());
                Evento.setCampaña(et5.getText().toString());
                try {
                    Evento.setDistancia(RecursosStatic.formatter.parse(et6.getText().toString()).floatValue());
                } catch (Exception e) {
                    Evento.setDistancia(0);
                    e.printStackTrace();
                }
                Evento.setFoto(urlPublica);
                Evento.setId(RecursosStatic.user.getID());
                Evento.setFecha(et7.getText().toString());
                Evento.setHora(et8.getText().toString());
                try {
                    Evento.setDatetime(RecursosStatic.dateFormat.parse(Evento.getFecha()).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (RecursosStatic.conectadoWifi(this)) {
                    DatabaseReference reference = database.getReference(Constantes.NODO_EVENTOS +"/"+ Evento.getUid());
                    reference.setValue(Evento);
                }
            } else {
                Evento = new Evento();
                Evento.setNombre(et1.getText().toString());
                Evento.setLocalidad(et2.getText().toString());
                Evento.setPremio(et3.getText().toString());
                Evento.setDescripcion(et4.getText().toString());
                Evento.setCampaña(et5.getText().toString());
                Evento.setFoto(urlPublica);
                try {
                    Evento.setDistancia(RecursosStatic.formatter.parse(et6.getText().toString()).floatValue());
                } catch (Exception e) {
                    Evento.setDistancia(0);
                    e.printStackTrace();
                }
                Evento.setId(RecursosStatic.user.getID());
                Evento.setUid(UUID.randomUUID().toString());
                Evento.setFecha(et7.getText().toString());
                Evento.setHora(et8.getText().toString());
                try {
                    Evento.setDatetime(RecursosStatic.dateFormat.parse(Evento.getFecha()).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (RecursosStatic.conectadoWifi(this)) {
                    DatabaseReference reference = database.getReference(Constantes.NODO_EVENTOS+"/"+ Evento.getUid());
                    reference.setValue(Evento);
                }
            }
            if (error.equals("")) {
                finish();
            }
        }
    }
}