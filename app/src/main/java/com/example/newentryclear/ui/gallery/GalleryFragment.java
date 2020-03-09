package com.example.newentryclear.ui.gallery;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.newentryclear.MainActivity;
import com.example.newentryclear.R;
import com.example.newentryclear.webdb;

public class GalleryFragment extends Fragment {
EditText nombre,contasenya;
Button sigIn;
    webdb db_action;

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        nombre= root.findViewById(R.id.edt_nameUser);
        contasenya= root.findViewById(R.id.edt_passwortUser);

        sigIn= root.findViewById(R.id.btn_logIn);
        sigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sig In","boton logIn click");
                 MainActivity.setImageView();

            }
        });

        db_action = new webdb();
        db_action.pruebas(getContext());

        return root;
    }

}