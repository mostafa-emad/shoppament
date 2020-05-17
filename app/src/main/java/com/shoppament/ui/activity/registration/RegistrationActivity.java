package com.shoppament.ui.activity.registration;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shoppament.R;
import com.shoppament.data.models.PictureModel;
import com.shoppament.databinding.ActivityRegistrationBinding;
import com.shoppament.ui.adapters.PicturesRecyclerAdapter;
import com.shoppament.ui.adapters.SlotsTimingRecyclerAdapter;
import com.shoppament.ui.base.BaseActivity;
import com.shoppament.utils.callbacks.IPictureListener;
import com.shoppament.utils.callbacks.OnObjectChangedListener;
import com.shoppament.utils.view.UploadFileController;
import com.shoppament.utils.view.dialogs.LocationMapDialog;
import com.shoppament.utils.view.dialogs.PictureViewDialog;
import com.shoppament.utils.view.dialogs.UploadOptionsDialog;

import java.util.Calendar;
import java.util.List;

public class RegistrationActivity extends BaseActivity implements IPictureListener {
    private RegistrationViewModel registrationViewModel;
    private ActivityRegistrationBinding activityRegistrationBinding;

    private PicturesRecyclerAdapter picturesRecyclerAdapter;
    private SlotsTimingRecyclerAdapter slotsTimingRecyclerAdapter;

    TextWatcher totalCapacityWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String insideCapacityValue = activityRegistrationBinding.insideCapacityEt.getText().toString();
            double insideCapacity = 0;
            if(!insideCapacityValue.isEmpty()){
                insideCapacity = Double.parseDouble(insideCapacityValue);
            }
            String outsideCapacityValue = activityRegistrationBinding.insideCapacityEt.getText().toString();
            double outsideCapacity = 0;
            if(!insideCapacityValue.isEmpty()){
                outsideCapacity = Double.parseDouble(outsideCapacityValue);
            }
            //Fixed value for now
            int averageTime = 2;
            double totalCapacity = insideCapacity + outsideCapacity;
            double perSlotTime = totalCapacity * averageTime;
            activityRegistrationBinding.totalCapacityTxt.setText(String.valueOf(totalCapacity));
            activityRegistrationBinding.perSlotTimeTxt.setText(String.valueOf(perSlotTime));
        }
    };

    @Override
    protected void initViews() {
        activityRegistrationBinding = DataBindingUtil.setContentView(this,R.layout.activity_registration);
        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activityRegistrationBinding.picturesRecycler.setLayoutManager(new LinearLayoutManager(this));
        picturesRecyclerAdapter = new PicturesRecyclerAdapter(registrationViewModel.getPictureModels(),this,this);
        activityRegistrationBinding.picturesRecycler.setAdapter(picturesRecyclerAdapter);

        activityRegistrationBinding.availableSlotTimingsRecycler.setLayoutManager(new LinearLayoutManager(this));
        slotsTimingRecyclerAdapter = new SlotsTimingRecyclerAdapter(registrationViewModel.getSlotTimingModels(),this);
        activityRegistrationBinding.availableSlotTimingsRecycler.setAdapter(slotsTimingRecyclerAdapter);
    }

    @Override
    protected void doCreate() {
        activityRegistrationBinding.insideCapacityEt.addTextChangedListener(totalCapacityWatcher);
        activityRegistrationBinding.outsideCapacityEt.addTextChangedListener(totalCapacityWatcher);
    }

    public void clickToUploadPics(View view) {
        if(registrationViewModel.isUploadNewPictureEnabled()) {
            new UploadOptionsDialog(RegistrationActivity.this);
        }else{
            showUploadPicsError();
        }
    }

    public void showUploadPicsError() {
        Toast.makeText(activity,getResources().getString(R.string.msg_upload_pictures_msg_error),
                Toast.LENGTH_LONG).show();
//                    ViewController.getInstance().showDialog(activity,getResources().getString(R.string.msg_upload_pictures_msg_error));
    }

    @Override
    public void showSelectedPic(PictureModel pictureModel) {
        new PictureViewDialog(activity,pictureModel.getPath());
    }

    @Override
    public void deleteSelectedPic(final int position) {
        registrationViewModel.deletePicture(position).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDeleted) {
                if(isDeleted){
                    picturesRecyclerAdapter.notifyItemRemoved(position);
                    picturesRecyclerAdapter.notifyItemRangeChanged(position,1);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UploadFileController.CAPTURE_PHOTO_ID){
            registrationViewModel.uploadNewPicture(UploadFileController.getInstance().getCameraPicture()).observe(
                    RegistrationActivity.this, new Observer<List<PictureModel>>() {
                        @Override
                        public void onChanged(List<PictureModel> pictureModels) {
                            if(pictureModels != null) {
                                picturesRecyclerAdapter.setPictureModels(pictureModels);
                            }
                        }
                    });
        }else if(requestCode == UploadFileController.UPLOAD_PHOTO_ID && data != null && data.getData() != null){
            registrationViewModel.uploadNewPicture(UploadFileController.getInstance().getDevicePicture(data,activity)).observe(
                    RegistrationActivity.this, new Observer<List<PictureModel>>() {
                        @Override
                        public void onChanged(List<PictureModel> pictureModels) {
                            if(pictureModels != null) {
                                picturesRecyclerAdapter.setPictureModels(pictureModels);
                            }
                        }
                    });
        }
    }

    public void getLocation(View view) {
        new LocationMapDialog(RegistrationActivity.this,getSupportFragmentManager(), new OnObjectChangedListener() {
            @Override
            public void onObjectChanged(int id, int position, Object object) {
                activityRegistrationBinding.selectLocationBtn.setText(getResources().getString(R.string.change_location_btn));
            }
        });
    }

    private void showLocationErrors() {

    }

    public void getTime(View view) {
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


    private void getTotalCapacity() {

    }

    private void getPerSlotTime() {

    }

    private void setSlotsAndTimings() {

    }

    private void deleteSelectedSlot() {

    }

    private void sendOtp() {

    }

    public void submitTheRegistration(View view) {
    }
}
