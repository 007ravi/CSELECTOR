package com.ravi.cslogin;

public class User {
    private static final User ourInstance = new User();

    public static User getInstance() {
        return ourInstance;
    }

    private User() {
    }

    private String mUserName;
    private String mUserEmail;
    private String mUserPhone;

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public void setUserEmail(String userEmail) {
        mUserEmail = userEmail;
    }

    public String getUserPhone() {
        return mUserPhone;
    }

    public void setUserPhone(String userPhone) {
        mUserPhone = userPhone;
    }
}
