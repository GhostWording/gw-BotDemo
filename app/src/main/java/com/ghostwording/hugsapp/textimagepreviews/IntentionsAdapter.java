package com.ghostwording.hugsapp.textimagepreviews;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.ghostwording.hugsapp.R;
import com.ghostwording.hugsapp.databinding.ItemIntentionBinding;
import com.ghostwording.hugsapp.model.MoodMenuItem;
import com.ghostwording.hugsapp.widget.RoundedCornersTransformation;

import java.util.ArrayList;
import java.util.List;

public class IntentionsAdapter extends RecyclerView.Adapter<IntentionsAdapter.BindingHolder> {

    public interface MoodMenuItemSelectedListener {
        void onMoodMenuItemSelected(MoodMenuItem moodMenuItem);
    }

    private List<MoodMenuItem> itemsList = new ArrayList<>();
    private MoodMenuItemSelectedListener itemSelectedListener;

    public IntentionsAdapter(List<MoodMenuItem> intentionList, MoodMenuItemSelectedListener intentionsListener) {
        this.itemsList = intentionList;
        this.itemSelectedListener = intentionsListener;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_intention, parent, false));
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {
        final ItemIntentionBinding binding = holder.getBinding();
        final MoodMenuItem moodMenuItem = itemsList.get(position);
        Context context = binding.intentionImage.getContext();
        Glide.with(context)
                .load(moodMenuItem.getImageAsset())
                .bitmapTransform(new CenterCrop(context), new RoundedCornersTransformation(context, 15, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(binding.intentionImage);
        binding.intentionTitle.setText(moodMenuItem.getLabel());
        binding.container.setOnClickListener(v -> itemSelectedListener.onMoodMenuItemSelected(moodMenuItem));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemIntentionBinding binding;

        public BindingHolder(View rowView) {
            super(rowView);
            binding = DataBindingUtil.bind(rowView);
        }

        public ItemIntentionBinding getBinding() {
            return binding;
        }
    }

}
