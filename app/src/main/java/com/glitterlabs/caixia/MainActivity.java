package com.glitterlabs.caixia;
/**
 * Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
 * All rights reserved. Patents pending.
 * Responsible: Abhay Bhusari
 *
 */

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.glitterlabs.caixia.Adapters.PagerAdapter;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintwo);
        // Track app opens.
		ParseAnalytics.trackAppOpened(getIntent());
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(1);
        saveUserProfile();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void saveUserProfile(){


        ParseInstallation installation= ParseInstallation.getCurrentInstallation();
        installation.put("userId", ParseUser.getCurrentUser().getObjectId());
        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                   /* Toast toast = Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT);
                    toast.show();*/
                } else {
                    e.printStackTrace();
/*
                    Toast toast = Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT);
                    toast.show();*/
                }
            }
        });
    }

	


}
