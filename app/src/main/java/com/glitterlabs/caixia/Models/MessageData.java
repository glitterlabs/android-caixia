package com.glitterlabs.caixia.Models;
/**
 * Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
 * All rights reserved. Patents pending.
 * Responsible: Abhay Bhusari
 *
 */

import com.parse.ParseGeoPoint;

import java.io.File;
import java.io.Serializable;


public class MessageData implements Serializable {

    public String textMessage;
    public String userId;
    public int timeForDisplay;
    public String scheduldDate;
    public ParseGeoPoint geoPoint;
    public byte[] byteImage;
    public File imageFile;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public ParseGeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(ParseGeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public String getScheduldDate() {
        return scheduldDate;
    }

    public void setScheduldDate(String scheduldDate) {
        this.scheduldDate = scheduldDate;
    }

    public int getTimeForDisplay() {
        return timeForDisplay;
    }

    public void setTimeForDisplay(int timeForDisplay) {
        this.timeForDisplay = timeForDisplay;
    }

    public byte[] getByteImage() {
        return byteImage;
    }

    public void setByteImage(byte[] byteImage) {
        this.byteImage = byteImage;
    }

    public File getImageFile() { return imageFile; }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }
}
