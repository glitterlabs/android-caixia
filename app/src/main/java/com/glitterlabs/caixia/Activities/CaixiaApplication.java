package com.glitterlabs.caixia.Activities;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.parse.PushService;

/*Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
        All rights reserved. Patents pending.

        Responsible: Abhay Bhusari
        */
public class CaixiaApplication extends android.app.Application {

    public static final String PARSE_APPLICATION_ID = "2AklFdxjzpM2q596mQ55aBsOsYWwE6IjOK45xyfy";
    public static final String PARSE_CLIENT_KEY = "rCTyrb0XBe4PISh2p37V5DJW2pQMxU7bYQLejHk6";
    public static final String REST_API_KEY = "thqfPX0EYh2hNjMkXFBOEXaHY46O2NujCmzTXujq";
    static final String GOOGLE_SENDER_ID = "9432966778899";  // Place here your Google project id
    static final String TAG = "GCM Android Example";

    public CaixiaApplication(){

    }
    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        Parse.initialize(getApplicationContext(), PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
        // Specify an Activity to handle all pushes by default.
        PushService.setDefaultPushCallback(this, MainActivity.class);
     //   ParseInstallation.getCurrentInstallation().saveInBackground();

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);

    }

}