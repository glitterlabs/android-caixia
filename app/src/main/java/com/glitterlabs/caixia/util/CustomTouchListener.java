package com.glitterlabs.caixia.util;
/**
 * Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
 * All rights reserved. Patents pending.
 * Responsible: Abhay Bhusari
 *
 */

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


/**
 * docs ?
 */
public class CustomTouchListener implements View.OnTouchListener {
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                ((TextView)view).setTextColor(0xFF000000); //black

                break;
            case MotionEvent.ACTION_CANCEL:

            case MotionEvent.ACTION_UP:
                ((TextView)view).setTextColor(0xFFFFFFFF); //white
                break;

        }
        return false;
    }
}