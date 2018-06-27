package com.ghostwording.hugsapp.model.requests;

import com.ghostwording.hugsapp.utils.AppConfiguration;
import com.ghostwording.hugsapp.utils.PrefManager;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FragmentRequest {

    @SerializedName("BotName")
    @Expose
    public final String botName;

    @SerializedName("DeviceId")
    @Expose
    public final String deviceId = AppConfiguration.getDeviceId();

    @SerializedName("FacebookId")
    @Expose
    public final String facebookId = PrefManager.instance().getFacebookId();

    @SerializedName("ParentSequencePath")
    @Expose
    public final String parentSequencePath;

    @SerializedName("FragmentId")
    @Expose
    private String fragmentId;

    @SerializedName("Tags")
    @Expose
    private List<String> tags = null;

    public FragmentRequest(String botName, String parentSequencePath, List<String> tags) {
        this.botName = botName;
        this.parentSequencePath = parentSequencePath;
        this.tags = tags;
    }

    public FragmentRequest(String botName, String parentSequencePath, String fragmentId) {
        this.botName = botName;
        this.parentSequencePath = parentSequencePath;
        this.fragmentId = fragmentId;
    }
}
