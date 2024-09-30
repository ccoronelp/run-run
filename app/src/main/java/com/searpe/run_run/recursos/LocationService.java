package com.searpe.run_run.recursos;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.FirebaseDatabase;
import com.searpe.run_run.Entidades.clases.Evento;
import com.searpe.run_run.Entidades.clases.Participante;
import com.searpe.run_run.R;

public class LocationService extends Service {
    private static final String TAG = "SERVICE";
    FollowMeLocationSource followMeLocationSource;
    Evento evento;
    Participante participante;
    float distancia = 0;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        distancia = intent.getExtras().getFloat("distancia");
        evento = (Evento) intent.getSerializableExtra("evento");
        participante = (Participante) intent.getSerializableExtra("participante");
        LocationInitialize();
        RecursosStatic.servicio = this;
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "1");
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.ic_outline_gps_fixed_24)
                    .setContentText("Se ha iniciado la competición")
                    .setPriority(NotificationManager.IMPORTANCE_NONE)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(1, notification);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String channelName = "My service";
        NotificationChannel chan = new NotificationChannel("2", channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "2");
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_outline_gps_fixed_24)
                .setContentText("Se ha iniciado la competición")
                .setPriority(NotificationManager.IMPORTANCE_NONE)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    public void LocationInitialize() {
        initializeLocationManager();
        try {
            followMeLocationSource.getBestAvailableProvider();
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }


    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (followMeLocationSource == null) {
            followMeLocationSource = new FollowMeLocationSource(evento, participante, distancia, FirebaseDatabase.getInstance(Constantes.URL_INSTANCE).getReference(Constantes.NODO_PARTICIPANTES).child(evento.getUid()).child(RecursosStatic.user.getID()));
        }
    }


}