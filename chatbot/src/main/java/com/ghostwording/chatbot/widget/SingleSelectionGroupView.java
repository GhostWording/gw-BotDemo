package com.ghostwording.chatbot.widget;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.databinding.DataBindingUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.databinding.ItemProfileSettingBinding;

import java.util.ArrayList;
import java.util.List;

public class SingleSelectionGroupView {

    public interface SelectionListener {
        void onSelected(int position);
    }

    private int selectedItemIndex = -1;
    private List<ItemProfileSettingBinding> viewBindings = new ArrayList<>();
    private Context context;

    public SingleSelectionGroupView(LinearLayout container, String[] items) {
        this(container, items, R.drawable.ic_done, null);
    }

    public SingleSelectionGroupView(LinearLayout container, String[] items, SelectionListener selectionListener) {
        this(container, items, R.drawable.ic_done, selectionListener);
    }

    public SingleSelectionGroupView(LinearLayout container, String[] items, @DrawableRes int checkIcon, final SelectionListener selectionListener) {
        context = container.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        for (int i = 0; i < items.length; i++) {
            final int position = i;
            String item = items[i];

            View itemView = layoutInflater.inflate(R.layout.item_profile_setting, null);
            ItemProfileSettingBinding binding = DataBindingUtil.bind(itemView);

            binding.tvTitle.setText(item);
            binding.ivCheckIndicator.setImageResource(checkIcon);

            binding.container.setOnClickListener(v -> {
                setSelectedItem(position);
                if (selectionListener != null) {
                    selectionListener.onSelected(position);
                }
            });

            viewBindings.add(binding);
            container.addView(itemView);

            //add divider view
            if (i < items.length - 1) {
                container.addView(layoutInflater.inflate(R.layout.view_divider, null));
            }
        }
    }

    public void setSelectedItem(int position) {
        selectedItemIndex = position;
        for (int i = 0; i < viewBindings.size(); i++) {
            ItemProfileSettingBinding viewItem = viewBindings.get(i);
            viewItem.ivCheckIndicator.setVisibility(i == position ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public int getSelectedItem() {
        return selectedItemIndex;
    }

}
