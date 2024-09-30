package com.searpe.run_run.recursos;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.searpe.run_run.BBDD.BBDD;
import com.searpe.run_run.Entidades.Firebase.Usuario;
import com.searpe.run_run.R;
import com.searpe.run_run.actividades.CrearSesion;
import com.searpe.run_run.actividades.IniciarSesion;
import com.searpe.run_run.actividades.MainActivity;
import com.searpe.run_run.actividades.Profile;
import com.searpe.run_run.actividades.RecuperarSesion;
import com.searpe.run_run.actividades.Splash;
import com.searpe.run_run.actividades.ui.competencias.CompetenciaParticipar;
import com.searpe.run_run.actividades.ui.run.RunEvento;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


//emilio de sapisa, bloqueado el programa, la pantalla de inicio de de clientes por otra, 679424321.

/**
 * Created by Sariza on 01/09/2016.
 */
public class RecursosStatic {
    public static GoogleMap MAP = null;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String iso = "es";
    public static Locale locale;
    public static BBDD bbdd;
    public static Usuario user;
    public static Uri fotoPerfilUri;
    public static SQLiteDatabase db;
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat dateFormatCompleto = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static DecimalFormat formatter = new DecimalFormat("#0.00");
    public static DecimalFormat formatterHora = new DecimalFormat("00");
    public static boolean ACTIVO = false;
    public static String CHAT_ABIERTO = "";
    public static LocationService servicio;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static ArrayList<String> IDs = new ArrayList<>();
    public static float TASA1 = 0;
    public static float TASA2 = 0;
    public static String NOTAS = "";

    public static void initialize(Context c) {
        bbdd = new BBDD(c, "MB", null, 1);
        db = bbdd.getWritableDatabase();
        locale = new Locale(iso);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator(' ');
        String pattern = "###,##0.##";
        formatter = new DecimalFormat(pattern, symbols);
        /*user = new Usuario();
        user.setNombre("Diego Alberto");
        user.setApellido("Bermúdez Zuleta");
        user.setCorreo("searpe1@gmail.com");
        user.setPass("mugiwara");
        user.setDocumento("123123123");
        user.setTipo("Admin");
        user.setTelefono("622753719");
        user.setTipoDoc("CC");
        user.setFotoPerfilURL(Constantes.URL_FOTO_POR_DEFECTO_USUARIOS);*/
    }

    public static void close() {
        bbdd.close();
        db.close();
    }


    public static void open(Context c) {
        bbdd = new BBDD(c, "MB", null, 1);
        db = bbdd.getWritableDatabase();

    }

    public static void mostrarAlerta(String titulo, String mensaje, Activity activity) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(activity);
        dialogo.setTitle(titulo);
        dialogo.setMessage(mensaje);
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Aceptar", null);

        dialogo.show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isNullOrBlank(String param) {
        return param == null || param.trim().length() == 0;
    }

    public static Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            Log.e("daw", "failed getViewBitmap(" + v + ")", new RuntimeException());
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        cacheBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));


        return decoded;
    }

    public static int getIndex(Spinner spinner, String myString) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().contains(myString)) {
                index = i;
            }
        }
        return index;
    }


    public static Boolean conectadoWifi(Context ct) {
        ConnectivityManager connectivity = (ConnectivityManager) ct
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo infoMobile = connectivity
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (info != null && info.isConnected()) {
                return true;
            } else if (infoMobile != null && infoMobile.isConnected()) {
                return true;
            } else {
                toast(ct, "No hay conexión a Internet");
                return false;
            }
        }
        return false;
    }

    public static void toast(Context c, String cadena) {
        Toast toast1 = Toast.makeText(c, cadena,
                Toast.LENGTH_LONG);
        toast1.show();
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static void comprobarAct(Activity act) {
        if (act != null) {
            act.finish();
        }
    }

    public static void comprobarActAPP() {
        //comprobarAct(VerPedido.act);
    }

    public static void lanzarIntent(Activity actDesde, Class actHasta) {
        Intent mainIntent = new Intent().setClass(actDesde,
                actHasta);
        actDesde.startActivityForResult(mainIntent, 1);
        actDesde.overridePendingTransition(R.anim.pull_in_right, R.anim.pull_out_left);
    }

    public static void lanzarIntent(Activity actDesde, Class actHasta, Object object) {
        Intent mainIntent = new Intent().setClass(actDesde,
                actHasta);
        mainIntent.putExtra("object", (Serializable) object);
        actDesde.startActivityForResult(mainIntent, 1);
        actDesde.overridePendingTransition(R.anim.pull_in_right, R.anim.pull_out_left);
    }

    public static void cerrarIntent(Activity act) {
        try {
            act.setResult(0);
            act.overridePendingTransition(R.anim.pull_in_left, R.anim.pull_out_right);
            act.onBackPressed();
        } catch (Exception e) {

        }
    }

    public static int comprobarBoolean(boolean destacado) {
        int num = 0;
        if (destacado) {
            num = 1;
        }
        return num;
    }

    public static boolean comprobarBoolean(int destacado) {
        boolean num = false;
        if (destacado == 1) {
            num = true;
        }
        return num;
    }

    public static void cerrarTodasActivities() {
        try {
            try {
                Splash.act.finish();
            } catch (Exception e) {
            }

            try {
                IniciarSesion.act.finish();
            } catch (Exception e) {
            }
            try {
                CrearSesion.act.finish();
            } catch (Exception e) {
            }
            try {
                RecuperarSesion.act.finish();
            } catch (Exception e) {
            }
            try {
                MainActivity.act.finish();
            } catch (Exception e) {
            }

            try {
                Profile.act.finish();
            } catch (Exception e) {
            }
        } catch (Exception e) {

        }
    }

    public static void IntentVerResultados() {
        try {
            RunEvento.act.finish();
            CompetenciaParticipar.act.finish();
        } catch (Exception e) {
        }
        try {

            Intent intent = MainActivity.act.getIntent();
            MainActivity.act.overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            MainActivity.act.finish();
            MainActivity.act.overridePendingTransition(0, 0);
            MainActivity.act.startActivity(intent);

        } catch (Exception e) {
        }

    }

    public static void IntentUserRegister(Activity a) {
        try {
            Splash.act.finish();
        } catch (Exception e) {
        }
        try {
            IniciarSesion.act.finish();
        } catch (Exception e) {
        }
        try {
            CrearSesion.act.finish();
        } catch (Exception e) {
        }
        try {
            RecuperarSesion.act.finish();
        } catch (Exception e) {
        }
        try {
            lanzarIntent(a, MainActivity.class);
        } catch (Exception e) {
        }
    }

    private int comprobarInt(String orden) {
        int num = 0;
        try {
            num = Integer.parseInt(orden);
        } catch (Exception e) {

        }
        return num;
    }

    private String controladorDatos(String dato) {

        if (dato == "null" || dato.equals("")) {

            dato = new String("");
        } else {

        }
        return dato.replaceAll("'", "´").trim();
    }

    public static String getSizeName(Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return "small";
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return "normal";
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return "large";
            case 4: // Configuration.SCREENLAYOUT_SIZE_XLARGE is API >= 9
                return "xlarge";
            default:
                return "undefined";
        }
    }
    /*
    *
    * LECTOR BUTTON
    * IntentIntegrator integrator = new IntentIntegrator(VentaAlbaranAnadirLinea.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Lector");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setBeepEnabled(true);
                integrator.setOrientationLocked(true);
                integrator.initiateScan();*/

}
