package com.example.newentryclear.ui.gallery;

import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.newentryclear.R;

public class GalleryFragment extends Fragment {
EditText nombre,contasenya;
Button sigIn;
ImageView userImage;
    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        nombre= root.findViewById(R.id.edt_nameUser);
        userImage=root.findViewById(R.id.imageView);


        contasenya= root.findViewById(R.id.edt_passwortUser);
        sigIn= root.findViewById(R.id.btn_logIn);
        sigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sig In",nombre.getText().toString()+" ,"+contasenya.getText().toString());
                //userImage.setImageResource(R.drawable.ic_person_black_24dp);
            }
        });

        return root;
    }
}