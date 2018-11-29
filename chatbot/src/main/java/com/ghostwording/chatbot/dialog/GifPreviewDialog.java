package com.ghostwording.chatbot.dialog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.databinding.DialogGifPreviewBinding;
import com.ghostwording.chatbot.utils.PostCardRenderer;
import com.ghostwording.chatbot.utils.Utils;
import com.ghostwording.chatbot.utils.UtilsUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class GifPreviewDialog extends BaseDialog {

    private int imageHeight;

    public static void show(AppCompatActivity appCompatActivity, String gifUrl, int height) {
        final ProgressDialog progressDialog = UtilsUI.showProgressDialog(appCompatActivity);
        progressDialog.setMessage(appCompatActivity.getString(R.string.processing_image));
        PostCardRenderer.requestDownloadGif(appCompatActivity, gifUrl, Utils.GIF_FILENAME).subscribe(success -> {
            progressDialog.dismiss();
            if (success) {
                GifPreviewDialog gifPreviewDialog = new GifPreviewDialog();
                gifPreviewDialog.imageHeight = height;
                gifPreviewDialog.show(appCompatActivity.getSupportFragmentManager(), GifPreviewDialog.class.getSimpleName());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_gif_preview, null);
        DialogGifPreviewBinding binding = DataBindingUtil.bind(v);
        binding.ivGifImage.getLayoutParams().height = imageHeight;
        binding.ivGifImage.setImageURI(PostCardRenderer.getShareGifUri(getContext()));
        binding.btnClose.setOnClickListener(view -> dismiss());
        return v;
    }

}

