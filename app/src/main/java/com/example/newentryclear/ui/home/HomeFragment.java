package com.example.newentryclear.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.newentryclear.AlarmasMedic;
import com.example.newentryclear.BatteryWarnings;
import com.example.newentryclear.DeviceManager;
import com.example.newentryclear.MainActivity;
import com.example.newentryclear.MyService;
import com.example.newentryclear.R;
import com.example.newentryclear.webdb;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import static android.content.Context.BATTERY_SERVICE;
import static com.example.newentryclear.UtilityClass.isPlugged;
import static com.example.newentryclear.UtilityClass.timeDisplay;
import static com.example.newentryclear.UtilityClass.timeDisplayDay;
import static com.example.newentryclear.UtilityClass.timeDisplayHours;
import static java.lang.String.valueOf;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;

    DatabaseReference reff;
    DatabaseReference reffDevices;
    DatabaseReference reffDevicesWar;

    AlarmasMedic alarmasMedic;
    DeviceManager deviceManager;
    BatteryWarnings batteryWarnings;

    String tabletName;
    String username;
    String idDevice;
    webdb db_action;
    SharedPreferences sharedPreferences;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        root.findViewById(R.id.btn_blue).setOnClickListener(this::onClick);
        root.findViewById(R.id.btn_green).setOnClickListener(this::onClick);
        root.findViewById(R.id.btn_yellow).setOnClickListener(this::onClick);
        root.findViewById(R.id.btn_red).setOnClickListener(this::onClick);

        db_action.pruebas(getContext());
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alarmasMedic = new AlarmasMedic();
        deviceManager = new DeviceManager();
        batteryWarnings = new BatteryWarnings();
        db_action = new webdb();
    }

    @Override
    public void onStart() {
        super.onStart();

        changeStatus("Aplicación Abierta");
        //db_action.create_entry_d("onStart");

    }

    @Override
    public void onStop() {
        super.onStop();

        changeStatus("Aplicación Pausada");
        //db_action.update_entry_d("onStop");

    }

    //ssssss
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_red:
                //getActivity().startService(new Intent(getActivity(), MyService.class));
                //funciona sendWwarningToFirebase("warning_rojo");
                db_action.create_entry("red");
                break;

            case R.id.btn_blue:
                //getActivity().stopService(new Intent(getActivity(), MyService.class));
                //funciona sendWwarningToFirebase("warning_azul");
                db_action.update_entry("blue");
                break;

            case R.id.btn_green:
                //sendWwarningToFirebase("warning_verde"); //funciona
                db_action.update_entry("green");
                break;

            case R.id.btn_yellow:
                //funciona sendWwarningToFirebase("warning_amarillo");
                db_action.create_entry("amarillo");
                Log.i("click", "el boton yellow funciona");

                break;

            default:
                Log.i("click", "somos los powerangers !! ;)");
        }
    }

    public void CheckingBattery(int battPercentage) {
        if (battPercentage <= 30) {
            reffDevicesWar = FirebaseDatabase.getInstance().getReference().child("Other Warnings").child(tabletName);
            batteryWarnings.setBattery_lvl(battPercentage);
            batteryWarnings.setId_tablet(idDevice);
            batteryWarnings.setLast_check(timeDisplay());
            batteryWarnings.setNom_tablet(tabletName);
            batteryWarnings.setWarning_type("Low Battery");
            reffDevicesWar.setValue(batteryWarnings);
        } else {
            reffDevicesWar = FirebaseDatabase.getInstance().getReference().child("Other Warnings").child(tabletName);
            reffDevicesWar.setValue(null);
        }
    }
    public void changeStatus(String status) {
        SharedPreferences prefs = getContext().getSharedPreferences(
                "com.example.newentryclear", Context.MODE_PRIVATE);
        if (isPlugged(getContext())) {
            prefs.edit().putString("chargerConnected", "Conectado").apply();
        } else if (!isPlugged(getContext())) {
            prefs.edit().putString("chargerConnected", "Desconectado").apply();
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        tabletName = sharedPreferences.getString("tabletName", "Adrian");
        idDevice = sharedPreferences.getString("tabletID", "00c");

        String latestAction = sharedPreferences.getString("latestAction", null);
        String batteryConnected = prefs.getString("chargerConnected", "defaultStringIfNothingFound");

        BatteryManager bm = (BatteryManager) getActivity().getSystemService(BATTERY_SERVICE);
        int percentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        reffDevices = FirebaseDatabase.getInstance().getReference().child("Devices Status").child(tabletName);
        deviceManager.setNom_tablet(tabletName);
        deviceManager.setID_tablet(idDevice);
        deviceManager.setUltima_Accion(latestAction);
        deviceManager.setApp_status(status);
        deviceManager.setLast_check(timeDisplay());
        deviceManager.setBattery_lvl(percentage);
        deviceManager.setDevice_charger(batteryConnected);
        CheckingBattery(percentage);


        reffDevices.setValue(deviceManager);
    }

    private void sendWwarningToFirebase(String typeOfWarning) {

        SharedPreferences prefs = getContext().getSharedPreferences("com.example.newentryclear", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String database = sharedPreferences.getString("name_db", "Database");
        int actualBattery = prefs.getInt("percentageBattery", -1);
        String batteryConnected = prefs.getString("chargerConnected", "defaultStringIfNothingFound");
        tabletName = sharedPreferences.getString("tabletName", "Tablet 10");
        idDevice = sharedPreferences.getString("tabletID", "007");
        username = sharedPreferences.getString("user_name", "menda");

        reff = FirebaseDatabase.getInstance().getReference().child(database).child(timeDisplayDay()).child("Log " + timeDisplayHours());

        //crea objeto alarma medica
        alarmasMedic.setNom_tablet(tabletName);
        alarmasMedic.setID_tablet(idDevice);
        alarmasMedic.setTipo_Alarma(typeOfWarning);
        alarmasMedic.setTime(timeDisplay());
        alarmasMedic.setNom_user(username);

        //crea objeto device manager
        deviceManager.setDevice_charger(batteryConnected);
        deviceManager.setLast_check(timeDisplay());
        deviceManager.setApp_status("Aplicación Abierta");
        //stopLockTask();

        reffDevicesWar = FirebaseDatabase.getInstance().getReference().child("Other Warnings").child(tabletName);
        reffDevicesWar.setValue(null);

        BatteryManager bm = (BatteryManager) getActivity().getSystemService(BATTERY_SERVICE);
        int percentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        CheckingBattery(percentage);

        reffDevices = FirebaseDatabase.getInstance().getReference().child("Devices Status").child(tabletName);

        reffDevices.setValue(deviceManager);
        reff.setValue(alarmasMedic);
    }
}