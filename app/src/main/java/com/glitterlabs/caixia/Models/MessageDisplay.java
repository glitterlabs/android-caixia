package com.glitterlabs.caixia.Models;

import android.graphics.Bitmap;

/**
 * Created by mohinish on 8/19/15.
 */
public class MessageDisplay {

    public    String text;
    public    int startTimer;
    public    Bitmap bmp;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getStartTimer() {
        return startTimer;
    }

    public void setStartTimer(int startTimer) {
        this.startTimer = startTimer;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }
}
