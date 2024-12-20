package me.t3sl4.upcortex.Model.User;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String userID;
    private String accessToken;

    private String identityNumber;
    private String eMail;
    private String nameSurname;
    private String phoneNumber;

    private String userState;

    //Payment Info
    private String name;
    private String surname;
    private String address;
    private String dialCode;
    private String zipCode;
    private String city;
    private String addressId;

    public User(String userID, String accessToken, String identityNumber, String eMail, String nameSurname, String phoneNumber, String userState) {
        this.userID = userID;
        this.accessToken = accessToken;

        this.identityNumber = identityNumber;
        this.eMail = eMail;
        this.nameSurname = nameSurname;
        this.phoneNumber = phoneNumber;

        this.userState = userState;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
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

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDialCode() {
        return dialCode;
    }

    public void setDialCode(String dialCode) {
        this.dialCode = dialCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userID);

        dest.writeString(identityNumber);
        dest.writeString(eMail);
        dest.writeString(nameSurname);
        dest.writeString(phoneNumber);

        dest.writeString(userState);
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
        userID = in.readString();

        identityNumber = in.readString();
        eMail = in.readString();
        nameSurname = in.readString();
        phoneNumber = in.readString();

        userState = in.readString();
    }
}
