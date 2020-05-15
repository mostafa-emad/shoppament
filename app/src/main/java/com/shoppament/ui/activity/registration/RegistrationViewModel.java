package com.shoppament.ui.activity.registration;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.shoppament.data.repo.RegistrationRepository;
import com.shoppament.ui.base.BaseViewModel;

public class RegistrationViewModel extends BaseViewModel {
    private RegistrationRepository registrationRepository;

    public RegistrationViewModel(@NonNull Application application) {
        super(application);
        registrationRepository = new RegistrationRepository(application);
    }

    public MutableLiveData<String> fetchData(){
        return registrationRepository.fetchData();
    }
}
