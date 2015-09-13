package com.glitterlabs.caixia.Models;

/**
 * Created by mohinish on 8/21/15.
 */
public class Friend {
    public  String friendName;
    public String userName;
    public String userID;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
