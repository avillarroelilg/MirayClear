package com.example.newentryclear.ui.hexadecimalgen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShareViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ShareViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }
}