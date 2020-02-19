package com.example.newentryclear;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.newentryclear.UtilityClass.isPlugged;
import static com.example.newentryclear.UtilityClass.timeDisplay;


public class deviceWarningService extends Service {
    MyTask myTask;


    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Servicio creado!", Toast.LENGTH_SHORT).show();
        myTask = new MyTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myTask.execute();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Servicio destruído!", Toast.LENGTH_SHORT).show();
        myTask.cancel(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class MyTask extends AsyncTask<String, String, String> {

        private DateFormat dateFormat;
        private String date;
        private boolean cent;

        private DatabaseReference reff;
        private DatabaseReference reffDevices;
        private DatabaseReference reffDevicesWar;

        private AlarmasMedic alarmasMedic;
        private DeviceManager deviceManager;
        private BatteryWarnings batteryWarnings;

        private String tabletName;
        private Integer idDevice;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //antes de empezar obtener siertos requerimientos


            SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                    "com.example.newentry", Context.MODE_PRIVATE);
            if (isPlugged(getApplicationContext())) {
                prefs.edit().putString("chargerConnected", "Conectado").apply();
            } else if (!isPlugged(getApplicationContext())) {
                prefs.edit().putString("chargerConnected", "Desconectado").apply();
            }
            //puede que ya no haga falta ###########
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            tabletName = sharedPreferences.getString("tabletName", "Tablet B1");
            idDevice = Integer.valueOf(sharedPreferences.getString("tabletID", "0"));

            String latestAction = sharedPreferences.getString("latestAction", null);
            String batteryConnected = prefs.getString("chargerConnected", "defaultStringIfNothingFound");
            BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
            assert bm != null;
            int percentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

            reffDevices = FirebaseDatabase.getInstance().getReference().child("Devices Status").child(tabletName);
            deviceManager.setNom_tablet(tabletName);
            deviceManager.setID_tablet(idDevice);
            deviceManager.setUltima_Accion(latestAction);
            deviceManager.setApp_status("Aplicación abierta");
            deviceManager.setLast_check(timeDisplay());
            deviceManager.setBattery_lvl(percentage);
            deviceManager.setDevice_charger(batteryConnected);
            if (percentage <= 30) {
                reffDevicesWar = FirebaseDatabase.getInstance().getReference().child("Other Warnings").child(tabletName);
                batteryWarnings.setBattery_lvl(percentage);
                batteryWarnings.setId_tablet(idDevice);
                batteryWarnings.setLast_check(timeDisplay());
                batteryWarnings.setNom_tablet(tabletName);
                batteryWarnings.setWarning_type("Low Battery");
                reffDevicesWar.setValue(batteryWarnings);
            } else if (percentage > 30) {
                reffDevicesWar = FirebaseDatabase.getInstance().getReference().child("Other Warnings").child(tabletName);
                reffDevicesWar.setValue(null);
            }
            reffDevices.setValue(deviceManager);

            dateFormat = new SimpleDateFormat("HH:mm:ss");
            cent = true;
        }

        protected String doInBackground(String... params) {
            //codigo a ejecutar
            while (cent){
                date = dateFormat.format(new Date());
                try {
                    publishProgress(date);
                    // Stop 5s  cada cuando tiempo queremos que lo haga
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(String... values) {
            //aqui es cuando ya quieres pasarle los datos a ...
            Toast.makeText(getApplicationContext(), "Hora actual: " + values[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cent = false;
        }
    }
}
