package com.glitterlabs.caixia.util;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by mohinish on 9/11/15.
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