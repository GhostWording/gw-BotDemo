package com.ghostwording.hugsapp.textimagepreviews;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghostwording.hugsapp.R;
import com.ghostwording.hugsapp.databinding.ItemPictureRecommendationBinding;
import com.ghostwording.hugsapp.model.PopularImages;
import com.ghostwording.hugsapp.viewmodel.PictureViewModel;

import java.util.List;

public class PicturesRecommendationAdapter extends RecyclerView.Adapter<PicturesRecommendationAdapter.BindingHolder> {

    private PictureSelectedListener pictureSelectedListener;
    private List<PopularImages.Image> pictures;

    public PicturesRecommendationAdapter(List<PopularImages.Image> pictures, PictureSelectedListener pictureSelectedListener) {
        this.pictures = pictures;
        this.pictureSelectedListener = pictureSelectedListener;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture_recommendation, parent, false));
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {
        ItemPictureRecommendationBinding quoteBinding = holder.getBinding();
        quoteBinding.setViewModel(new PictureViewModel(pictures.get(position), position + 1));
        quoteBinding.container.setOnClickListener(v -> {
            if (pictureSelectedListener != null) {
                pictureSelectedListener.onPictureSelected(pictures.get(position).getImageLink());
            }
        });
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemPictureRecommendationBinding binding;

        public BindingHolder(View rowView) {
            super(rowView);
            binding = DataBindingUtil.bind(rowView);
        }

        public ItemPictureRecommendationBinding getBinding() {
            return binding;
        }
    }

    public interface PictureSelectedListener {
        void onPictureSelected(String imageUrl);
    }

}
