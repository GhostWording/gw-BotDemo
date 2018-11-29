package com.ghostwording.chatbot.chatbot;

import android.app.Activity;

import com.ghostwording.chatbot.chatbot.model.BotSequence;

public interface RedirectionManager {
    void handleRedirectAction(Activity activity, BotSequence.Step step);
}
