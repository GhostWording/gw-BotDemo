package com.ghostwording.chatbot.model;

import com.ghostwording.chatbot.utils.PrefManager;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MBTIProfiles {

    public static boolean isKnownUserProfile() {
        return false;
    }

    @SerializedName("PsychologicalProfiles")
    @Expose
    private PsychologicalProfiles psychologicalProfiles;

    public PsychologicalProfiles getPsychologicalProfiles() {
        return psychologicalProfiles;
    }

    public void setPsychologicalProfiles(PsychologicalProfiles psychologicalProfiles) {
        this.psychologicalProfiles = psychologicalProfiles;
    }

    public String getProfileDescription() {
        String stringProfile = getStringProfile();
        if (stringProfile == null) {
            return null;
        }
        switch (stringProfile) {
            case "ENFJ":
                return psychologicalProfiles.eNFJ.getLabel();
            case "ENFP":
                return psychologicalProfiles.eNFP.getLabel();
            case "ENTJ":
                return psychologicalProfiles.eNTJ.getLabel();
            case "ENTP":
                return psychologicalProfiles.eNTP.getLabel();
            case "ESFJ":
                return psychologicalProfiles.eSFJ.getLabel();
            case "ESFP":
                return psychologicalProfiles.eSFP.getLabel();
            case "ESTJ":
                return psychologicalProfiles.eSTJ.getLabel();
            case "ESTP":
                return psychologicalProfiles.eSTP.getLabel();
            case "INFJ":
                return psychologicalProfiles.iNFJ.getLabel();
            case "INFP":
                return psychologicalProfiles.iNFP.getLabel();
            case "INTJ":
                return psychologicalProfiles.iNTJ.getLabel();
            case "INTP":
                return psychologicalProfiles.iNTP.getLabel();
            case "ISFJ":
                return psychologicalProfiles.iSFJ.getLabel();
            case "ISFP":
                return psychologicalProfiles.iSFP.getLabel();
            case "ISTJ":
                return psychologicalProfiles.iSTJ.getLabel();
            case "ISTP":
                return psychologicalProfiles.iSTP.getLabel();
        }
        return "";
    }

    public static String getStringProfile() {
        StringBuilder stringProfile = new StringBuilder();
        String answer = PrefManager.instance().getUserAnswer("ppEnergeticAfter");
        if ("WithPeople".equals(answer)) {
            stringProfile.append("E");
        } else {
            stringProfile.append("I");
        }
        answer = PrefManager.instance().getUserAnswer("ppDecisionsAriseFrom");
        if ("Feelings".equals(answer)) {
            stringProfile.append("S");
        } else {
            stringProfile.append("N");
        }
        answer = PrefManager.instance().getUserAnswer("ppReceptiveTo");
        if ("Ideas".equals(answer)) {
            stringProfile.append("T");
        } else {
            stringProfile.append("F");
        }
        answer = PrefManager.instance().getUserAnswer("ppOpenedOrSettled");
        if ("Opened".equals(answer)) {
            stringProfile.append("P");
        } else {
            stringProfile.append("J");
        }
        return stringProfile.toString();
    }

    public class PsychologicalProfiles {

        @SerializedName("ENFJ")
        @Expose
        private Profile eNFJ;
        @SerializedName("ENFP")
        @Expose
        private Profile eNFP;
        @SerializedName("ENTJ")
        @Expose
        private Profile eNTJ;
        @SerializedName("ENTP")
        @Expose
        private Profile eNTP;
        @SerializedName("ESFJ")
        @Expose
        private Profile eSFJ;
        @SerializedName("ESFP")
        @Expose
        private Profile eSFP;
        @SerializedName("ESTJ")
        @Expose
        private Profile eSTJ;
        @SerializedName("ESTP")
        @Expose
        private Profile eSTP;
        @SerializedName("INFJ")
        @Expose
        private Profile iNFJ;
        @SerializedName("INFP")
        @Expose
        private Profile iNFP;
        @SerializedName("INTJ")
        @Expose
        private Profile iNTJ;
        @SerializedName("INTP")
        @Expose
        private Profile iNTP;
        @SerializedName("ISFJ")
        @Expose
        private Profile iSFJ;
        @SerializedName("ISFP")
        @Expose
        private Profile iSFP;
        @SerializedName("ISTJ")
        @Expose
        private Profile iSTJ;
        @SerializedName("ISTP")
        @Expose
        private Profile iSTP;
    }

    public class Profile {
        @SerializedName("en")
        @Expose
        private String en;
        @SerializedName("fr")
        @Expose
        private String fr;
        @SerializedName("es")
        @Expose
        private String es;

        public String getLabel() {
            return en;
        }

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }

        public String getFr() {
            return fr;
        }

        public void setFr(String fr) {
            this.fr = fr;
        }

        public String getEs() {
            return es;
        }

        public void setEs(String es) {
            this.es = es;
        }
    }

}
