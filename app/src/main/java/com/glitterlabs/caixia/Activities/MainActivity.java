package com.glitterlabs.caixia.Activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.glitterlabs.caixia.Adapters.PagerAdapter;
import com.glitterlabs.caixia.R;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.RefreshCallback;
import com.parse.SaveCallback;

/*
    Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
    All rights reserved. Patents pending.

    Responsible: Abhay Bhusari
 */

//Refrence link for pagerView https://guides.codepath.com/android/Sliding-Tabs-with-PagerSlidingTabStrip
public class MainActivity extends AppCompatActivity {

    static final String GOOGLE_SENDER_ID = "9432966778899";  // Place here your Google project id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

        refreshUserProfile();
    }
    public void saveUserProfile(){

        ParseInstallation installation= ParseInstallation.getCurrentInstallation();
        installation.put("userId", ParseUser.getCurrentUser().getObjectId());
        installation.put("GCMSenderId", GOOGLE_SENDER_ID);
        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast toast = Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    e.printStackTrace();

                    Toast toast = Toast.makeText(getApplicationContext(),"fail", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }


    // Get the latest values from the ParseInstallation object.
    private void refreshUserProfile() {
        ParseInstallation.getCurrentInstallation().refreshInBackground(new RefreshCallback() {

            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {

                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
