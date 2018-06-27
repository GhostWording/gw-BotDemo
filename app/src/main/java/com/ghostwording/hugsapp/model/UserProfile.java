package com.ghostwording.hugsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserProfile {

    public static final String FIRST_NAME = "FacebookFirstName";
    public static final String USER_ANIMAL = "UserAnimal";
    public static final String USER_FLOWER = "UserFlower";
    public static final String USER_LANDSCAPE = "UserLandscape";
    public static final String USER_DESCRIPTION = "UserDescriptionPrototypeId";
    public static final String USER_MBTI_SELECTED = "MBTISelected";
    public static final String GENDER = "Gender";

    @SerializedName("UserId")
    @Expose
    private Object userId;
    @SerializedName("DeviceId")
    @Expose
    private String deviceId;
    @SerializedName("FacebookId")
    @Expose
    private String facebookId;
    @SerializedName("Properties")
    @Expose
    private Map<String, String> properties;
    @SerializedName("Identifier")
    @Expose
    private Identifier identifier;
    @SerializedName("FacebookFirstName")
    private String facebookFirstName;
    @SerializedName("ApplicationName")
    private String applicationName = "";

    public String getApplicationName() {
        return applicationName;
    }

    public void setFacebookFirstName(String facebookFirstName) {
        this.facebookFirstName = facebookFirstName;
    }

    public String getFacebookFirstName() {
        if (facebookFirstName == null && properties != null) {
            return properties.get(UserProfile.FIRST_NAME);
        }
        return facebookFirstName;
    }

    public class Identifier {
        @SerializedName("DeviceId")
        @Expose
        private String deviceId;
        @SerializedName("FacebookId")
        @Expose
        private String facebookId;
    }

    /**
     * @return The userId
     */
    public Object getUserId() {
        return userId;
    }

    /**
     * @param userId The UserId
     */
    public void setUserId(Object userId) {
        this.userId = userId;
    }

    /**
     * @return The deviceId
     */
    public String getDeviceId() {
        if (deviceId == null) {
            return identifier.deviceId;
        }
        return deviceId;
    }

    /**
     * @param deviceId The DeviceId
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @return The facebookId
     */
    public String getFacebookId() {
        if (facebookId == null && identifier != null) {
            return identifier.facebookId;
        }
        return facebookId;
    }

    /**
     * @param facebookId The FacebookId
     */
    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    /**
     * @return The properties
     */
    public Map<String, String> getProperties() {
        return properties;
    }

    public class Properties {

        @SerializedName("Age")
        @Expose
        private String age;
        @SerializedName("ConjugalSituation")
        @Expose
        private String conjugalSituation;
        @SerializedName("Country")
        @Expose
        private String country;
        @SerializedName("FacebookFirstName")
        @Expose
        private String facebookFirstName;
        @SerializedName("Gender")
        @Expose
        private String gender;
        @SerializedName("InitialMBTIKnowledge")
        @Expose
        private String initialMBTIKnowledge;
        @SerializedName("MBTISelected")
        @Expose
        private String mBTISelected;
        @SerializedName("MBTIYesOrNo")
        @Expose
        private String mBTIYesOrNo;
        @SerializedName("SetLanguage")
        @Expose
        private String setLanguage;
        @SerializedName("UserAnimal")
        @Expose
        private String userAnimal;
        @SerializedName("UserFlower")
        @Expose
        private String userFlower;
        @SerializedName("UserLandscape")
        @Expose
        private String userLandscape;


        @SerializedName("UserDescriptionPrototypeId")
        @Expose
        private String userDescriptionPrototypeId;

        public String getUserDescriptionPrototypeId() {
            return userDescriptionPrototypeId;
        }

        /**
         * @return The age
         */
        public String getAge() {
            return age;
        }

        /**
         * @param age The Age
         */
        public void setAge(String age) {
            this.age = age;
        }

        /**
         * @return The conjugalSituation
         */
        public String getConjugalSituation() {
            return conjugalSituation;
        }

        /**
         * @param conjugalSituation The ConjugalSituation
         */
        public void setConjugalSituation(String conjugalSituation) {
            this.conjugalSituation = conjugalSituation;
        }

        /**
         * @return The country
         */
        public String getCountry() {
            return country;
        }

        /**
         * @param country The Country
         */
        public void setCountry(String country) {
            this.country = country;
        }

        /**
         * @return The facebookFirstName
         */
        public String getFacebookFirstName() {
            return facebookFirstName;
        }

        /**
         * @param facebookFirstName The FacebookFirstName
         */
        public void setFacebookFirstName(String facebookFirstName) {
            this.facebookFirstName = facebookFirstName;
        }

        /**
         * @return The gender
         */
        public String getGender() {
            return gender;
        }

        /**
         * @param gender The Gender
         */
        public void setGender(String gender) {
            this.gender = gender;
        }

        /**
         * @return The initialMBTIKnowledge
         */
        public String getInitialMBTIKnowledge() {
            return initialMBTIKnowledge;
        }

        /**
         * @param initialMBTIKnowledge The InitialMBTIKnowledge
         */
        public void setInitialMBTIKnowledge(String initialMBTIKnowledge) {
            this.initialMBTIKnowledge = initialMBTIKnowledge;
        }

        /**
         * @return The mBTISelected
         */
        public String getMBTISelected() {
            return mBTISelected;
        }

        /**
         * @param mBTISelected The MBTISelected
         */
        public void setMBTISelected(String mBTISelected) {
            this.mBTISelected = mBTISelected;
        }

        /**
         * @return The mBTIYesOrNo
         */
        public String getMBTIYesOrNo() {
            return mBTIYesOrNo;
        }

        /**
         * @param mBTIYesOrNo The MBTIYesOrNo
         */
        public void setMBTIYesOrNo(String mBTIYesOrNo) {
            this.mBTIYesOrNo = mBTIYesOrNo;
        }

        /**
         * @return The setLanguage
         */
        public String getSetLanguage() {
            return setLanguage;
        }

        /**
         * @param setLanguage The SetLanguage
         */
        public void setSetLanguage(String setLanguage) {
            this.setLanguage = setLanguage;
        }

        /**
         * @return The userAnimal
         */
        public String getUserAnimal() {
            return userAnimal;
        }

        /**
         * @param userAnimal The UserAnimal
         */
        public void setUserAnimal(String userAnimal) {
            this.userAnimal = userAnimal;
        }

        /**
         * @return The userFlower
         */
        public String getUserFlower() {
            return userFlower;
        }

        /**
         * @param userFlower The UserFlower
         */
        public void setUserFlower(String userFlower) {
            this.userFlower = userFlower;
        }

        /**
         * @return The userLandscape
         */
        public String getUserLandscape() {
            return userLandscape;
        }

        /**
         * @param userLandscape The UserLandscape
         */
        public void setUserLandscape(String userLandscape) {
            this.userLandscape = userLandscape;
        }
    }

    public static List<UserProfile> removeDuplicates(List<UserProfile> items) {
        List<UserProfile> result = new ArrayList<>();
        for (UserProfile userProfile : items) {
            if (!isContainsItem(result, userProfile)) {
                result.add(userProfile);
            }
        }
        return result;
    }

    private static boolean isContainsItem(List<UserProfile> items, UserProfile item) {
        for (UserProfile userProfile : items) {
            if (userProfile.getDeviceId().equals(item.getDeviceId())) {
                return true;
            }
        }
        return false;
    }

}
