package com.shoppament.ui.activity.registration;

import android.app.TimePickerDialog;
import android.view.View;
import android.widget.TimePicker;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shoppament.R;
import com.shoppament.data.models.PictureModel;
import com.shoppament.data.models.SlotTimingModel;
import com.shoppament.databinding.ActivityRegistrationBinding;
import com.shoppament.ui.adapters.PicturesRecyclerAdapter;
import com.shoppament.ui.adapters.SlotsTimingRecyclerAdapter;
import com.shoppament.ui.base.BaseActivity;
import com.shoppament.utils.callbacks.OnObjectChangedListener;
import com.shoppament.utils.view.dialogs.LocationMapDialog;
import com.shoppament.utils.view.dialogs.UploadOptionsDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegistrationActivity extends BaseActivity {
    private RegistrationViewModel registrationViewModel;
    private ActivityRegistrationBinding activityRegistrationBinding;

    private PicturesRecyclerAdapter picturesRecyclerAdapter;
    private SlotsTimingRecyclerAdapter slotsTimingRecyclerAdapter;

    @Override
    protected void initViews() {
        activityRegistrationBinding = DataBindingUtil.setContentView(this,R.layout.activity_registration);
        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        activityRegistrationBinding.picturesRecycler.setLayoutManager(layoutManager);
        picturesRecyclerAdapter = new PicturesRecyclerAdapter(registrationViewModel.getPictureModels(),this);
        activityRegistrationBinding.picturesRecycler.setAdapter(picturesRecyclerAdapter);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        activityRegistrationBinding.availableSlotTimingsRecycler.setLayoutManager(layoutManager2);
        slotsTimingRecyclerAdapter = new SlotsTimingRecyclerAdapter(registrationViewModel.getSlotTimingModels(),this);
        activityRegistrationBinding.availableSlotTimingsRecycler.setAdapter(slotsTimingRecyclerAdapter);

        initActions();
    }

    private void initActions() {
        activityRegistrationBinding.uploadPicturesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UploadOptionsDialog(RegistrationActivity.this, new OnObjectChangedListener() {
                    @Override
                    public void onObjectChanged(int id, int position, Object object) {
                        registrationViewModel.uploadNewPicture(new PictureModel()).observe(
                                RegistrationActivity.this, new Observer<List<PictureModel>>() {
                            @Override
                            public void onChanged(List<PictureModel> pictureModels) {
                                if(pictureModels != null) {
                                    picturesRecyclerAdapter.setPictureModels(pictureModels);
                                }

                            }
                        });
                    }
                });
            }
        });

        activityRegistrationBinding.selectLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LocationMapDialog(RegistrationActivity.this,getSupportFragmentManager(), new OnObjectChangedListener() {
                    @Override
                    public void onObjectChanged(int id, int position, Object object) {
                        activityRegistrationBinding.selectLocationBtn.setText(getResources().getString(R.string.change_location_btn));
                    }
                });
            }
        });

        activityRegistrationBinding.averageTimeHhEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });
        activityRegistrationBinding.averageTimeMmEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                activityRegistrationBinding.averageTimeHhEt.setText(String.valueOf(selectedHour));
                activityRegistrationBinding.averageTimeMmEt.setText(String.valueOf(selectedMinute));
            }
        }, hour, minute, true);//Yes 24 hour time
//        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    @Override
    protected void doCreate() {
        registrationViewModel.fetchData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
//                ((TextView)activityRegistrationBinding.contentLayout.findViewById(R.id.body_txt)).setText(s);
            }
        });
    }
}
