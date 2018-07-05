package com.ghostwording.chatbot.chatbot;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
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
import com.ghostwording.chatbot.model.intentions.Intention;
import com.ghostwording.chatbot.model.intentions.IntentionAreaComparator;
import com.ghostwording.chatbot.model.intentions.IntentionSelectedListener;
import com.ghostwording.chatbot.model.intentions.IntentionsPopularComparator;
import com.ghostwording.chatbot.model.recipients.Recipient;
import com.ghostwording.chatbot.utils.AppConfiguration;
import com.ghostwording.chatbot.utils.Logger;
import com.ghostwording.chatbot.widget.RoundedCornersTransformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    @DrawableRes
    private Integer botAvatarResource = R.drawable.ic_huggy_avatar;

    public BotCommandsView(final Activity activity, final FrameLayout container) {
        this.activity = activity;
        this.container = container;
        this.layoutInflater = LayoutInflater.from(activity);
    }

    public void setAvatarImage(@DrawableRes Integer imageResource) {
        botAvatarResource = imageResource;
    }

    public void showLoadingView() {
        clearCommand();
        View menuItem = layoutInflater.inflate(R.layout.item_bot_typing, null);
        try {
            ((ImageView) menuItem.findViewById(R.id.iv_avatar)).setImageResource(botAvatarResource);
        } catch (Exception ex) {
            Logger.e(ex.toString());
        }
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
            if (command.getCarouselElements() != null) {
                addCarouselItem(llCommands, command, view -> commandListener.onCommandChoose(command));
            } else if (command.getCommandPicture() != null) {
                addMenuItemWithImage(llCommands, command, view -> commandListener.onCommandChoose(command));
            } else {
                addMenuItem(llCommands, command.getLabel(), view -> commandListener.onCommandChoose(command));
            }
        }
        container.addView(commandsContainerView);
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
        Glide.with(activity)
                .load(command.getCarouselElements().getPicturePath())
                .bitmapTransform(new CenterCrop(activity), new RoundedCornersTransformation(activity, 13, 0, RoundedCornersTransformation.CornerType.TOP))
                .into(ivIntentionImage);
        ((TextView) menuItem.findViewById(R.id.intention_title)).setText(command.getCarouselElements().getTitle());
        TextView tvSubtitle = menuItem.findViewById(R.id.intention_subtitle);
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
                .load(command.getCommandPicture())
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