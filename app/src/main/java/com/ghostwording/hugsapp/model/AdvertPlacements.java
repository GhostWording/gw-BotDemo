package com.ghostwording.hugsapp.model;

import com.ghostwording.hugsapp.utils.PrefManager;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AdvertPlacements {

    @SerializedName("FirstLaunch")
    @Expose
    private Placement firstLaunch;
    @SerializedName("FirstLaunchAlternative")
    @Expose
    private Placement firstLaunchAlternative;
    @SerializedName("FirstResumeActivity")
    @Expose
    private Placement firstActivityResume;
    @SerializedName("FirstResumeActivityAlternative")
    @Expose
    private Placement firstActivityResumeAlternative;
    @SerializedName("OtherResumeActivity")
    @Expose
    private Placement otherActivityResume;
    @SerializedName("AfterSending")
    @Expose
    private Placement afterSending;
    @SerializedName("OtherResumeActivityAlternative")
    @Expose
    private Placement otherResumeActivityAlternative;
    @SerializedName("GalleryNative")
    @Expose
    private Placement galleryNative;
    @SerializedName("BeforePreview")
    @Expose
    private Placement beforePreview;

    public Placement getFirstLaunch() {
        if (firstLaunchAlternative != null) {
            if (firstLaunchAlternative.getCountries().contains(PrefManager.instance().getUserCountry())) {
                return firstLaunchAlternative;
            }
        }
        return firstLaunch;
    }

    public Placement getAfterSending() {
        return afterSending;
    }

    public Placement getBeforePreview() {
        return beforePreview;
    }

    public Placement getFirstActivityResume() {
        if (firstActivityResumeAlternative != null) {
            if (firstActivityResumeAlternative.getCountries().contains(PrefManager.instance().getUserCountry())) {
                return firstActivityResumeAlternative;
            }
        }
        return firstActivityResume;
    }

    public Placement getOtherActivityResume() {
        if (otherResumeActivityAlternative != null) {
            if (otherResumeActivityAlternative.getCountries().contains(PrefManager.instance().getUserCountry())) {
                return otherResumeActivityAlternative;
            }
        }
        return otherActivityResume;
    }

    public Placement getGalleryNative() {
        return galleryNative;
    }

    public static class Placement {

        @SerializedName("Provider")
        @Expose
        private String provider;
        @SerializedName("Countries")
        @Expose
        private String countries;
        @SerializedName("ID")
        @Expose
        private String iD;
        @SerializedName("Fallback")
        @Expose
        private Placement fallback;
        @SerializedName("ProportionUsers")
        @Expose
        private Integer proportionUsers;

        public Integer getProportionUsers() {
            return proportionUsers;
        }

        public String getCountries() {
            return countries;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getID() {
            return iD;
        }

        public void setID(String iD) {
            this.iD = iD;
        }

        public Placement getFallback() {
            return fallback;
        }

        public void setFallback(Placement fallback) {
            this.fallback = fallback;
        }

    }

}