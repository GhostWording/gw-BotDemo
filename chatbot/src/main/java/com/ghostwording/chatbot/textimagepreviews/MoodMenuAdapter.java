package com.ghostwording.chatbot.textimagepreviews;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.databinding.ItemMoodMenuBinding;
import com.ghostwording.chatbot.model.MoodMenuItem;
import com.ghostwording.chatbot.viewmodel.MoodMenuItemViewModel;

import java.util.List;

public class MoodMenuAdapter extends RecyclerView.Adapter<MoodMenuAdapter.BindingHolder> {
    private List<MoodMenuItem> items;
    private MoodMenuItemViewModel.ItemSelectedListener itemSelectedListener;

    public MoodMenuAdapter(List<MoodMenuItem> items, MoodMenuItemViewModel.ItemSelectedListener itemSelectedListener) {
        this.items = items;
        this.itemSelectedListener = itemSelectedListener;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mood_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ItemMoodMenuBinding intentionBinding = holder.getBinding();
        MoodMenuItem item = items.get(position);
        intentionBinding.setViewModel(new MoodMenuItemViewModel(item));
        intentionBinding.btnItem.setOnClickListener(view -> {
            if (itemSelectedListener != null) {
                itemSelectedListener.onItemSelected(item);
            }
        });
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemMoodMenuBinding binding;

        public BindingHolder(View rowView) {
            super(rowView);
            binding = DataBindingUtil.bind(rowView);
        }

        public ItemMoodMenuBinding getBinding() {
            return binding;
        }
    }
}
