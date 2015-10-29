package com.glitterlabs.caixia.Fragments;

/**
 * Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
 * All rights reserved. Patents pending.
 * Responsible: Abhay Bhusari
 *
 */

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;


public class TimePickerFragment extends DialogFragment {

    OnTimeSetListener onTimeSet;

    public TimePickerFragment() {

    }
    public void setCallBack(OnTimeSetListener ontime) {
        onTimeSet = ontime;
    }
    @SuppressLint("NewApi")
    private int hour, minute;
    public void setArguments(Bundle args) {
        super.setArguments(args);
        hour = args.getInt("hour");
        minute = args.getInt("minute");

    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), onTimeSet, hour, minute, false);
    }
/*
    OnTimeSetListener ontime = new OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            Toast.makeText(getActivity(), String.valueOf(hourOfDay) + ":" + String.valueOf(minute), Toast.LENGTH_LONG).show();
            // txt_time.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
        }
    };*/

}
