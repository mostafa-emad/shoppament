package com.shoppament.ui.activity.registration;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.shoppament.data.models.PictureModel;
import com.shoppament.data.models.SlotTimingModel;
import com.shoppament.data.repo.RegistrationRepository;
import com.shoppament.ui.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class RegistrationViewModel extends BaseViewModel {
    private RegistrationRepository registrationRepository;

    private List<PictureModel> pictureModels = new ArrayList<>();
    private List<SlotTimingModel> slotTimingModels = new ArrayList<>();

    public RegistrationViewModel(@NonNull Application application) {
        super(application);
        registrationRepository = new RegistrationRepository(application);
    }

    public MutableLiveData<String> fetchData(){
        return registrationRepository.fetchData();
    }

//    MutableLiveData<List<PictureModel>> uploadNewPicture(PictureModel pictureModel){
//        MutableLiveData<List<PictureModel>> picListMutableLiveData = new MutableLiveData<>();
//        int size = pictureModels.size();
//        if(size == 5) {
//            picListMutableLiveData.setValue(null);
//        }else {
//            int index = size + 1;
//            pictureModel.setName("image" + index + ".jpeg");
//            pictureModels.add(pictureModel);
//            picListMutableLiveData.setValue(pictureModels);
//        }
//        return picListMutableLiveData;
//    }

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
        slotTimingModels.add(new SlotTimingModel("Slot 1 - 9:00am to 9:05am"));
        slotTimingModels.add(new SlotTimingModel("Slot 2 - 9:00am to 9:05am"));
        slotTimingModels.add(new SlotTimingModel("Slot 3 - 9:00am to 9:05am"));
        return slotTimingModels;
    }
}
