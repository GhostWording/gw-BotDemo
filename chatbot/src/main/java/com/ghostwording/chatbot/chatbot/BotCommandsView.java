package com.ghostwording.chatbot.chatbot;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.analytics.AnalyticsHelper;
import com.ghostwording.chatbot.chatbot.model.BotSequence;
import com.ghostwording.chatbot.io.DataLoader;
import com.ghostwording.chatbot.model.intentions.Intention;
import com.ghostwording.chatbot.model.intentions.IntentionAreaComparator;
import com.ghostwording.chatbot.model.intentions.IntentionSelectedListener;
import com.ghostwording.chatbot.model.intentions.IntentionsPopularComparator;
import com.ghostwording.chatbot.model.recipients.Recipient;
import com.ghostwording.chatbot.utils.AppConfiguration;
import com.ghostwording.chatbot.utils.Logger;
import com.ghostwording.chatbot.utils.UtilsUI;
import com.ghostwording.chatbot.widget.RoundedCornersTransformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.DrawableRes;

public class BotCommandsView {

    public interface MessageCommandsListener {
        void onShowAnotherMessageClicked();

        void onSendMessageClicked();

        void onChangeTopicClicked();

        void onShowQuestionClicked();
    }

    public interface GifCommandsListener {
        void showAnother();

        void send();
    }

    private LayoutInflater layoutInflater;
    private FrameLayout container;
    private Activity activity;

    public BotCommandsView(final Activity activity, final FrameLayout container) {
        this.activity = activity;
        this.container = container;
        this.layoutInflater = LayoutInflater.from(activity);
    }

    public void showLoadingView() {
        clearCommand();
        View menuItem = layoutInflater.inflate(R.layout.item_bot_typing, null);
        UtilsUI.showBotAvatar(menuItem);
        container.addView(menuItem);
    }

    public void showIntentionsCommandForRecipient(Recipient recipient, final IntentionSelectedListener intentionSelectedListener) {
        List<Intention> intentions = new ArrayList<>();
        for (Intention intention : AppConfiguration.getIntentions()) {
            if (intention.getRecurring() == null) {
                continue;
            }
            if (recipient.getRelationTypeTag() == null || intention.getRelationTypesString().contains(recipient.getRelationTypeTag())) {
                intentions.add(intention);
            }
        }

        Collections.sort(intentions, new IntentionAreaComparator());
        Collections.sort(intentions, new IntentionsPopularComparator());

        clearCommand();
        View commandsContainerView = layoutInflater.inflate(R.layout.item_bot_commands, null);
        LinearLayout llCommands = commandsContainerView.findViewById(R.id.container_commands);
        for (final Intention intention : intentions) {
            addMenuItem(llCommands, intention.getLabel(), view -> {
                intentionSelectedListener.onSelected(intention);
                AnalyticsHelper.sendEvent(AnalyticsHelper.Events.HUGGY_COMMAND_SELECTED, "IntentionSelected");
            });
        }
        container.addView(commandsContainerView);
    }

    public void showMessageCommands(final MessageCommandsListener messageCommandsListener, boolean addTopicCommands) {
        clearCommand();
        View commandsContainerView = layoutInflater.inflate(R.layout.item_bot_commands, null);
        LinearLayout llCommands = commandsContainerView.findViewById(R.id.container_commands);
        addMenuItem(llCommands, activity.getString(R.string.send), view -> {
            messageCommandsListener.onSendMessageClicked();
            AnalyticsHelper.sendEvent(AnalyticsHelper.Events.HUGGY_COMMAND_SELECTED, "SendMessage");
        });

        addMenuItem(llCommands, activity.getString(R.string.show_another), view -> {
            messageCommandsListener.onShowAnotherMessageClicked();
            AnalyticsHelper.sendEvent(AnalyticsHelper.Events.HUGGY_COMMAND_SELECTED, "ShowAnotherMessage");
        });

        if (addTopicCommands) {
            addMenuItem(llCommands, activity.getString(R.string.show_something_else), view -> {
                messageCommandsListener.onChangeTopicClicked();
                AnalyticsHelper.sendEvent(AnalyticsHelper.Events.HUGGY_COMMAND_SELECTED, "ChangeTopic");
            });
        }

        addMenuItem(llCommands, activity.getString(R.string.talk_to_me), view -> {
            messageCommandsListener.onShowQuestionClicked();
            AnalyticsHelper.sendEvent(AnalyticsHelper.Events.HUGGY_COMMAND_SELECTED, "TalkToMe");
        });

        container.addView(commandsContainerView);
    }

    public void showGifCommands(final GifCommandsListener gifCommandsListener) {
        clearCommand();
        View commandsContainerView = layoutInflater.inflate(R.layout.item_bot_commands_centered, null);
        LinearLayout llCommands = commandsContainerView.findViewById(R.id.container_commands);
        addMenuItem(llCommands, activity.getString(R.string.send), view -> {
            AnalyticsHelper.sendEvent(AnalyticsHelper.Events.HUGGY_COMMAND_SELECTED, "SendGif");
            gifCommandsListener.send();
        });
        addMenuItem(llCommands, activity.getString(R.string.show_another), view -> {
            AnalyticsHelper.sendEvent(AnalyticsHelper.Events.HUGGY_COMMAND_SELECTED, "ShowAnotherGif");
            gifCommandsListener.showAnother();
        });
        container.addView(commandsContainerView);
    }

    public void setCommands(final BotSequence sequence, final SequenceHandler.CommandListener commandListener) {
        clearCommand();
        List<BotSequence> commands = sequence.getCommands();
        if (sequence.isRandomizeCommands()) {
            Collections.shuffle(commands);
        }

        View commandsContainerView;
        if (commands.size() < 3 && commands.get(0).getCarouselElements() == null) {
            commandsContainerView = layoutInflater.inflate(R.layout.item_bot_commands_centered, null);
        } else {
            commandsContainerView = layoutInflater.inflate(R.layout.item_bot_commands, null);
        }
        LinearLayout llCommands = commandsContainerView.findViewById(R.id.container_commands);

        for (final BotSequence command : commands) {
            if (command != null) {
                if (command.getCarouselElements() != null) {
                    if (commands.size() > 2) {
                        commandsContainerView.findViewById(R.id.tv_scroll_for_more).setVisibility(View.VISIBLE);
                    }
                    addCarouselItem(llCommands, command, view -> commandListener.onCommandChoose(command));
                } else if (command.getCommandPicture() != null) {
                    addMenuItemWithImage(llCommands, command, view -> commandListener.onCommandChoose(command));
                } else {
                    addMenuItem(llCommands, command.getLabel(), view -> commandListener.onCommandChoose(command));
                }
            }
        }

        if (AppConfiguration.isAnimateButtons()) {
            animateButtons(llCommands);
        }
        container.addView(commandsContainerView);
    }

    private void animateButtons(ViewGroup buttonsLayout) {
        for (int i = 0; i < buttonsLayout.getChildCount(); i++) {
            View view = buttonsLayout.getChildAt(i);
            startViewAnimation(view, (i + 1) * 1000);
        }
        buttonsLayout.postDelayed(() -> animateButtons(buttonsLayout), 5000);
    }

    private void startViewAnimation(View view, long offset) {
        Animation anim1 = new ScaleAnimation(
                1f, 0.75f,
                1f, 0.75f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(500);
        Animation anim2 = new ScaleAnimation(
                1f, 1.25f,
                1f, 1.25f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim2.setStartOffset(500);
        anim2.setDuration(500);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setStartOffset(offset);
        animationSet.addAnimation(anim1);
        animationSet.addAnimation(anim2);
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        view.startAnimation(animationSet);
    }

    public void clearCommand() {
        container.removeAllViews();
        if (container.getParent() instanceof HorizontalScrollView) {
            container.post(() -> ((HorizontalScrollView) container.getParent()).fullScroll(HorizontalScrollView.FOCUS_LEFT));
        }
    }

    private View addCarouselItem(LinearLayout llCommands, BotSequence command, View.OnClickListener onClickListener) {
        View menuItem = layoutInflater.inflate(R.layout.item_command_carousel, null);
        ImageView ivIntentionImage = menuItem.findViewById(R.id.intention_image);
        TextView tvTitle = menuItem.findViewById(R.id.intention_title);
        TextView tvSubtitle = menuItem.findViewById(R.id.intention_subtitle);
        if (command.getCarouselElements().getTitle() != null && command.getCarouselElements().getTitle().equals(".")) {
            tvTitle.setVisibility(View.GONE);
        }
        Glide.with(activity)
                .load(DataLoader.getImageUrl(activity, command.getCarouselElements().getPicturePath()))
                .bitmapTransform(new CenterCrop(activity), new RoundedCornersTransformation(activity, 13, 0, RoundedCornersTransformation.CornerType.TOP))
                .into(ivIntentionImage);
        tvTitle.setText(command.getCarouselElements().getTitle());
        if (command.getCarouselElements().getSubtitle() != null) {
            tvSubtitle.setVisibility(View.VISIBLE);
            tvSubtitle.setText(command.getCarouselElements().getSubtitle());
        } else {
            tvSubtitle.setVisibility(View.GONE);
        }
        ((TextView) menuItem.findViewById(R.id.tv_command_label)).setText(command.getLabel());
        menuItem.setOnClickListener(onClickListener);
        llCommands.addView(menuItem);
        return menuItem;
    }

    private View addMenuItemWithImage(LinearLayout llContainer, BotSequence command, View.OnClickListener onClickListener) {
        View menuItem = layoutInflater.inflate(R.layout.item_command_card_view, null);
        ImageView ivIntentionImage = menuItem.findViewById(R.id.intention_image);
        Glide.with(activity)
                .load(DataLoader.getImageUrl(activity, command.getCommandPicture()))
                .bitmapTransform(new CenterCrop(activity), new RoundedCornersTransformation(activity, 13, 0, RoundedCornersTransformation.CornerType.TOP))
                .into(ivIntentionImage);
        ((TextView) menuItem.findViewById(R.id.intention_title)).setText(command.getLabel());
        TextView tvSubtitle = menuItem.findViewById(R.id.intention_subtitle);
        if (command.getTitle() != null) {
            tvSubtitle.setVisibility(View.VISIBLE);
            tvSubtitle.setText(command.getTitle());
        } else {
            tvSubtitle.setVisibility(View.GONE);
        }
        menuItem.setOnClickListener(onClickListener);
        llContainer.addView(menuItem);
        return menuItem;
    }

    private View addMenuItem(LinearLayout llContainer, String label, final View.OnClickListener onClickListener) {
        View menuItem = layoutInflater.inflate(R.layout.item_command_view, null);
        ((TextView) menuItem.findViewById(R.id.tv_command)).setText(label);
        menuItem.setOnClickListener(onClickListener);
        llContainer.addView(menuItem);
        return menuItem;
    }

}