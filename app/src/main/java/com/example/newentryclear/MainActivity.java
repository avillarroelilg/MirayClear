package com.example.newentryclear;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import com.example.newentryclear.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;

import static com.example.newentryclear.R.mipmap.ic_launcher_round;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static ImageView imageView;

    webdb db_action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//batery and fix screem
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        registerReceiver(LowBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
// #####
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
//se usa para cambiar la imagen del menu lateral
        View header = navigationView.getHeaderView(0);
        imageView = (ImageView) header.findViewById(R.id.imageLog);
// #####
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_gallery,  R.id.nav_home,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        db_action = new webdb();
        Context context = getApplicationContext();
        db_action.pruebas(context);

    }

     public static void setImageView() {
        imageView.setBackgroundResource(R.drawable.ic_person_black_24dp);
    }

    @Override
    protected void onStart() {
        super.onStart();
        db_action.create_entry_d("onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        db_action.update_entry_d("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db_action.update_entry_d("onDestroy");
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }

    // ########### old
    private BroadcastReceiver LowBatteryReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intent1 = new Intent(Intent.ACTION_CALL);
            intent1.setData(Uri.parse("tel:122"));
            startActivity(intent1);
        }
    };
}
