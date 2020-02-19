package com.example.newentryclear.ui.share;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.newentryclear.R;

import java.util.Random;

public class ShareFragment extends Fragment implements View.OnClickListener{
    TextView cajaHex;
    private ShareViewModel shareViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);
        final TextView textView = root.findViewById(R.id.text_share);
        shareViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        cajaHex = root.findViewById(R.id.txtHex);

        root.findViewById(R.id.btnHex).setOnClickListener(this::onClick);
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHex:

                Log.i("click", "el boton blue funciona");
                crearHexadecimal();
                break;

            default:
                Log.i("click", "id no registrado en onclick");
                break;

        }
    }

    private void crearHexadecimal() {
        Random rand = new Random();
        int myRandomNumber = rand.nextInt(0xfff) + 0xfff; // Generates a random number between 0x10 and 0x20
        //System.out.printf("%x\n",myRandomNumber); // Prints it in hex, such as "0x14" or....
        String result = Integer.toHexString(myRandomNumber); // Random hex number in result
        cajaHex.setText(result);
    }
}