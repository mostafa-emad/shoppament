package com.shoppament.utils.view.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.shoppament.R;

import java.io.File;

public class PictureViewDialog extends BaseCustomDialog{
    private String path;
    private ImageView pictureImg;

    public PictureViewDialog(Activity activity,String path) {
        super(activity,R.layout.layout_picture_dialog, null);
        this.path = path;

        init();
    }

    @Override
    protected void init() {
        super.init();
        pictureImg = rootView.findViewById(R.id.picture_img);
        Button closeBtn = rootView.findViewById(R.id.close_btn);

        manager.windowAnimations = R.style.DialogTheme;

        showImage(path);

        alert.show();
        alert.getWindow().setLayout(pxFromDp(300), ViewGroup.LayoutParams.WRAP_CONTENT);

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                alert.dismiss();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
    }

    private void showImage(String path) {
        File image = new File(path);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
//        bitmap = Bitmap.createScaledBitmap(bitmap,pictureImg.getWidth(),pictureImg.getHeight(),true);
        pictureImg.setImageBitmap(bitmap);
    }

}
