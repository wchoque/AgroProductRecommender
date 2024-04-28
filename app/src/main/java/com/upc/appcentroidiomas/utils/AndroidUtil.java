package com.upc.appcentroidiomas.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.upc.appcentroidiomas.data.model.UserInformationModel;

public class AndroidUtil {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static UserInformationModel getUserModelFromIntent(Intent intent){
        UserInformationModel userInformationModel = new UserInformationModel();
        userInformationModel.id = intent.getIntExtra("userIdTo", 0);
        userInformationModel.displayName = (intent.getStringExtra("displayNameTo"));
        userInformationModel.imageUrl = (intent.getStringExtra("imageUrl"));
        return userInformationModel;
    }

    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView) {
        Glide.with(context)
                .load(imageUri)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .apply(new RequestOptions().skipMemoryCache(true))
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);

    }
}
