package com.ghostwording.hugsapp.widget;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.ghostwording.hugsapp.R;
import com.ghostwording.hugsapp.analytics.AnalyticsHelper;
import com.ghostwording.hugsapp.databinding.ItemSideMenuPromotionBinding;
import com.ghostwording.hugsapp.model.Promotions;
import com.ghostwording.hugsapp.utils.AppConfiguration;

import java.util.List;

public class PromotionsView {

    public PromotionsView(LinearLayout container) {
        final Context context = container.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        List<Promotions.App> appList = AppConfiguration.getAppPromos();
        if (appList.size() > 0) {
            for (final Promotions.App item : appList) {
                View itemView = layoutInflater.inflate(R.layout.item_side_menu_promotion, null);
                ItemSideMenuPromotionBinding binding = DataBindingUtil.bind(itemView);

                Glide.with(context).load(item.getIconUrl()).into(binding.promotionIcon);
                binding.promotionTitle.setText(item.getTitle());
                binding.container.setOnClickListener(v -> {
                    AnalyticsHelper.sendEvent(AnalyticsHelper.Categories.APP, AnalyticsHelper.Events.PROMOTE_APP, item.getTitle());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(item.getStoreUrl()));
                    context.startActivity(intent);
                });
                container.addView(itemView);
            }
        } else {
            container.setVisibility(View.GONE);
        }
    }

}