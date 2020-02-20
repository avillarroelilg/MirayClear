package com.example.newentryclear.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.newentryclear.UtilityClass.timeDisplay;
import static com.example.newentryclear.UtilityClass.timeDisplayDay;
import static com.example.newentryclear.UtilityClass.timeDisplayHours;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        root.findViewById(R.id.btn_blue).setOnClickListener(this::onClick);
        root.findViewById(R.id.btn_green).setOnClickListener(this::onClick);
        root.findViewById(R.id.btn_yellow).setOnClickListener(this::onClick);
        root.findViewById(R.id.btn_red).setOnClickListener(this::onClick);

        return root;
    }
//ssssss
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_red:
                getActivity().startService(new Intent(getActivity(), MyService.class));
                Log.i("click", "el boton red funciona");
                //sendWwarningToFirebase("warning_rojo");

                break;
            case R.id.btn_blue:
                getActivity().stopService(new Intent(getActivity(), MyService.class));
                Log.i("click", "el boton blue funciona");
                //sendWwarningToFirebase("warning_azul");
                break;

            case R.id.btn_green:

                Log.i("click", "el boton  green funciona");
                //sendWwarningToFirebase("warning_verde");
                break;

            case R.id.btn_yellow:

                Log.i("click", "el boton yellow funciona");
                //sendWwarningToFirebase("warning_amarillo");
                break;


            default:
                Log.i("click", "somos los powerangers !! ;)");
        }
    }

    DatabaseReference reff;
    DatabaseReference reffDevices;
    DatabaseReference reffDevicesWar;

    AlarmasMedic alarmasMedic;
    DeviceManager deviceManager;
    BatteryWarnings batteryWarnings;

    String tabletName;
    Integer idDevice;

    private void sendWwarningToFirebase(String typeOfWarning) {

        SharedPreferences prefs = getContext().getSharedPreferences("com.example.newentry", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String database = sharedPreferences.getString("name_db", "Database");
        int actualBattery = prefs.getInt("percentageBattery", -1);
        String batteryConnected = prefs.getString("chargerConnected", "defaultStringIfNothingFound");
        tabletName = sharedPreferences.getString("tabletName", "Tablet B1");
        idDevice = Integer.valueOf(sharedPreferences.getString("tabletID", "0"));

        reff = FirebaseDatabase.getInstance().getReference().child(database).child(timeDisplayDay()).child("Log " + timeDisplayHours());

        //crea objeto alarma medica
        alarmasMedic.setNom_tablet(tabletName);
        alarmasMedic.setID_tablet(idDevice);
        alarmasMedic.setTipo_Alarma(typeOfWarning);
        alarmasMedic.setTime(timeDisplay());

        //crea objeto device manager
        deviceManager.setDevice_charger(batteryConnected);
        deviceManager.setLast_check(timeDisplay());
        //stopLockTask();

        reffDevicesWar = FirebaseDatabase.getInstance().getReference().child("Other Warnings").child(tabletName);
        reffDevicesWar.setValue(null);

        reffDevices.setValue(deviceManager);
    }
}