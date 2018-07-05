package com.ghostwording.chatbot;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.ghostwording.chatbot.databinding.ActivityImagePreviewBinding;
import com.ghostwording.chatbot.dialog.ShareDialog;

public class ImagePreviewActivity extends BaseActivity {

    private static final String IMAGE_URL = "image_url";

    public static void start(Context context, String imageUrl) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(IMAGE_URL, imageUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String imageUrl = getIntent().getStringExtra(IMAGE_URL);

        ActivityImagePreviewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_image_preview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        binding.btnBack.setOnClickListener(view -> finish());
        binding.btnShare.setOnClickListener(view -> ShareDialog.show(ImagePreviewActivity.this, imageUrl, null, false));
        Glide.with(this).load(imageUrl).into(binding.ivImage);
    }

}
