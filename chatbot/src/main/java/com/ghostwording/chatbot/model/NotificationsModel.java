package com.ghostwording.chatbot.model;

import com.ghostwording.chatbot.model.texts.Quote;
import com.ghostwording.chatbot.utils.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NotificationsModel {

    @SerializedName("fr-FR")
    @Expose
    private List<String> frFR = new ArrayList<>();
    @SerializedName("en-EN")
    @Expose
    private List<String> enEN = new ArrayList<>();
    @SerializedName("es-ES")
    @Expose
    private List<String> esES = new ArrayList<>();

    public void setMessages(List<Quote> quotes, int language) {
        List<String> messages = new ArrayList<>();
        for (Quote quote : quotes) {
            messages.add(quote.getContent());
        }
        switch (language) {
            case Utils.ENGLISH:
                enEN = messages;
                break;
            case Utils.FRENCH:
                frFR = messages;
                break;
            case Utils.SPANISH:
                esES = messages;
                break;
        }
    }

    public List<String> getMessages(int language) {
        switch (language) {
            case Utils.ENGLISH:
                return enEN;
            case Utils.FRENCH:
                return frFR;
            case Utils.SPANISH:
                return esES;
        }
        return null;
    }

}