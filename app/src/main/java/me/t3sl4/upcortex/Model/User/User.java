package me.t3sl4.upcortex.Model.User;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String userID;
    private String accessToken;
    private String refreshToken;

    private String role;
    private String userName;
    private String eMail;
    private String nameSurname;
    private String phoneNumber;
    private String companyName;
    private String ownerName;
    private String createdAt;
    private String isActive;

    private String selectedLanguage;
    private String selectedNightMode;

    public User(String role, String userName, String eMail, String nameSurname, String phoneNumber, String companyName, String createdAt, String isActive, String selectedLanguage, String selectedNightMode) {
        this.role = role;
        this.userName = userName;
        this.eMail = eMail;
        this.nameSurname = nameSurname;
        this.phoneNumber = phoneNumber;
        this.companyName = companyName;
        this.createdAt = createdAt;
        this.isActive = isActive;
        this.selectedLanguage = selectedLanguage;
        this.selectedNightMode = selectedNightMode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public String getSelectedNightMode() {
        return selectedNightMode;
    }

    public void setSelectedNightMode(String selectedNightMode) {
        this.selectedNightMode = selectedNightMode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(role);
        dest.writeString(userName);
        dest.writeString(eMail);
        dest.writeString(nameSurname);
        dest.writeString(phoneNumber);
        dest.writeString(companyName);
        dest.writeString(createdAt);
        dest.writeString(isActive);
        dest.writeString(selectedLanguage);
        dest.writeString(selectedNightMode);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    protected User(Parcel in) {
        role = in.readString();
        userName = in.readString();
        eMail = in.readString();
        nameSurname = in.readString();
        phoneNumber = in.readString();
        companyName = in.readString();
        createdAt = in.readString();
        isActive = in.readString();
        selectedLanguage = in.readString();
        selectedNightMode = in.readString();
    }
}
