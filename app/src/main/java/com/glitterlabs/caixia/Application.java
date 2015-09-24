package com.glitterlabs.caixia;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.parse.PushService;

public class Application extends android.app.Application {


    public static final String PARSE_APPLICATION_ID = "2AklFdxjzpM2q596mQ55aBsOsYWwE6IjOK45xyfy";
    public static final String PARSE_CLIENT_KEY = "rCTyrb0XBe4PISh2p37V5DJW2pQMxU7bYQLejHk6";

  public Application() {
  }

  @Override
  public void onCreate() {
    super.onCreate();

      // Initialize the Parse SDK.
      Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);

      // Specify an Activity to handle all pushes by default.
      PushService.setDefaultPushCallback(this, MainActivity.class);

      //ParseInstallation.getCurrentInstallation().saveInBackground();
     ParseUser.enableAutomaticUser();
      ParseACL defaultACL = new ParseACL();
      // If you would like all objects to be private by default, remove this
      // line.
      defaultACL.setPublicReadAccess(true);
      ParseACL.setDefaultACL(defaultACL, true);

  }
}