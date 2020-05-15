package com.shoppament.ui.activity.registration;

import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.shoppament.R;
import com.shoppament.databinding.ActivityRegistrationBinding;
import com.shoppament.ui.base.BaseActivity;

public class RegistrationActivity extends BaseActivity {
    private RegistrationViewModel registrationViewModel;
    private ActivityRegistrationBinding activityRegistrationBinding;

    @Override
    protected void initViews() {
        activityRegistrationBinding = DataBindingUtil.setContentView(this,R.layout.activity_registration);
        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void doCreate() {
        registrationViewModel.fetchData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ((TextView)activityRegistrationBinding.contentLayout.findViewById(R.id.body_txt)).setText(s);
            }
        });
    }
}
