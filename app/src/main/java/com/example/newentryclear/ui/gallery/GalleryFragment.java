package com.example.newentryclear.ui.gallery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.newentryclear.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {
    EditText nombre, contrasenya;
    Button signIn;
    ImageView userImage;
    DatabaseReference reff;
    String passwordDB = "" , usercheck;
    boolean correctUser = false;
    boolean correctPassword = false;
    List<String> userList=new ArrayList<String>();
    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        nombre = root.findViewById(R.id.edit_username);
        userImage = root.findViewById(R.id.imageView);


        contrasenya = root.findViewById(R.id.edit_passwordUser);
        signIn = root.findViewById(R.id.btn_logIn);

        SharedPreferences prefs = getContext().getSharedPreferences("com.example.newentry", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String database = sharedPreferences.getString("name_db", "Database");

        reff = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        usercheck = reff.getKey();

        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference().child("Usuarios");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    userList.add(snap.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for (String s : userList){
                    if (s.equals(nombre.getText().toString())){
                        correctUser = true;
                    }
                }
                if (!correctUser){
                    Toast.makeText(getContext(), "Nombre de usuario incorrecto", Toast.LENGTH_LONG).show();
                } else {
                    reff.child(nombre.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            passwordDB = dataSnapshot.child("password").getValue().toString();
                            if (passwordDB.equals(contrasenya.getText().toString())){
                                Toast.makeText(getContext(), "Log-in completado !", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(getContext(), "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Log.d("sig In", nombre.getText().toString() + " ," + contrasenya.getText().toString());
                    //userImage.setImageResource(R.drawable.ic_person_black_24dp);

                }
            }
        });
        return root;
    }

}