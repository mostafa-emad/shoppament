package com.shoppament.ui.activity.registration;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.shoppament.data.models.PictureModel;
import com.shoppament.data.models.SlotTimingModel;
import com.shoppament.data.repo.RegistrationRepository;
import com.shoppament.ui.base.BaseViewModel;
import com.shoppament.utils.TimeFormatManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegistrationViewModel extends BaseViewModel {
    private RegistrationRepository registrationRepository;

    private List<PictureModel> pictureModels = new ArrayList<>();
    private List<SlotTimingModel> slotTimingModels = new ArrayList<>();
    private MutableLiveData<Double> perSlotTimeLiveData = new MutableLiveData<>();

    public RegistrationViewModel(@NonNull Application application) {
        super(application);
        registrationRepository = new RegistrationRepository(application);
    }

    MutableLiveData<List<PictureModel>> uploadNewPicture(PictureModel pictureModel){
        MutableLiveData<List<PictureModel>> uploadNewPictureLiveData = new MutableLiveData<>();
        if(pictureModel != null) {
            pictureModels.add(pictureModel);
            uploadNewPictureLiveData.setValue(pictureModels);
        }
        return uploadNewPictureLiveData;
    }

    MutableLiveData<Boolean> deletePicture(int position){
        MutableLiveData<Boolean> deletePictureLiveData = new MutableLiveData<>();
        try {
            pictureModels.remove(position);
            deletePictureLiveData.setValue(true);
        }catch (Exception e){
            e.printStackTrace();
            deletePictureLiveData.setValue(false);
        }
        return deletePictureLiveData;
    }

    boolean isUploadNewPictureEnabled() {
        return pictureModels.size() < 5;
    }

    List<PictureModel> getPictureModels() {
        return pictureModels;
    }

    List<SlotTimingModel> getSlotTimingModels() {
        return slotTimingModels;
    }

    double getTotalCapacity(String insideCapacityValue,String outsideCapacityValue) {
        double insideCapacity = 0;
        if(!insideCapacityValue.isEmpty()){
            insideCapacity = Double.parseDouble(insideCapacityValue);
        }
        double outsideCapacity = 0;
        if(!outsideCapacityValue.isEmpty()){
            outsideCapacity = Double.parseDouble(outsideCapacityValue);
        }
        return insideCapacity + outsideCapacity;
    }

    MutableLiveData<Double> getPerSlotTime(double totalCapacity,String averageHours,String averageMinutes) {
        perSlotTimeLiveData.setValue(totalCapacity * TimeFormatManager.getInstance().getMinutesFromHhMm(averageHours, averageMinutes));
        return perSlotTimeLiveData;
    }

    double getPerSlotTimeValue() {
        return perSlotTimeLiveData.getValue();
    }

    MutableLiveData<List<SlotTimingModel>> setSlotsAndTimings(int startingTimeMinutes,int endingTimeMinutes) {
        MutableLiveData<List<SlotTimingModel>> slotsAndTimingsLiveData = new MutableLiveData<>();
        int perSlotTimeMinutes = (int) getPerSlotTimeValue();
        if(perSlotTimeMinutes == 0 || startingTimeMinutes == 0 || endingTimeMinutes == 0)
            return slotsAndTimingsLiveData;

        int operationalTimeMinutes = endingTimeMinutes - startingTimeMinutes;

        List<SlotTimingModel> slotTimingModels = new ArrayList<>();
        SlotTimingModel slotTimingModel = new SlotTimingModel();
        slotTimingModel.setFromDate(TimeFormatManager.getInstance().format12Hours(startingTimeMinutes));
        if(operationalTimeMinutes / perSlotTimeMinutes <= 1){
            slotTimingModel.setToDate(TimeFormatManager.getInstance().format12Hours(endingTimeMinutes));
            slotTimingModels.add(slotTimingModel);
            slotsAndTimingsLiveData.setValue(slotTimingModels);
            return slotsAndTimingsLiveData;
        }
        int slotEndTimeMinutes = startingTimeMinutes;
        String slotFormat12Hours;
        while (operationalTimeMinutes > perSlotTimeMinutes){
            slotEndTimeMinutes += perSlotTimeMinutes;
            operationalTimeMinutes -= perSlotTimeMinutes;

            slotFormat12Hours = TimeFormatManager.getInstance().format12Hours(slotEndTimeMinutes);
            slotTimingModel.setToDate(slotFormat12Hours);

            slotTimingModels.add(slotTimingModel);

            slotTimingModel = new SlotTimingModel();
            slotTimingModel.setFromDate(slotFormat12Hours);
        }
        slotTimingModel.setToDate(TimeFormatManager.getInstance().format12Hours(endingTimeMinutes));
        slotsAndTimingsLiveData.setValue(slotTimingModels);

        return slotsAndTimingsLiveData;
    }

    private void deleteSelectedSlot() {

    }

    private void sendOtp() {

    }

    MutableLiveData<ArrayList<String>> showShopTypeList(){
        MutableLiveData<ArrayList<String>> dataListLiveData = new MutableLiveData<>();
        dataListLiveData.setValue(registrationRepository.getShopTypes());
        return dataListLiveData;
    }

    public MutableLiveData<String> fetchData(){
        return registrationRepository.fetchData();
    }

}
