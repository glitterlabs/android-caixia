package com.glitterlabs.caixia;

/**
 * Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
 * All rights reserved. Patents pending.
 * Responsible: Abhay Bhusari
 *
 */

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;


public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mediaPlayer= new MediaPlayer().create(this,R.raw.caixia);
        mediaPlayer.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Determine whether the current user is an anonymous user
                if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
                    // If user is anonymous, send the user to LoginSignupActivity.class
                    Intent intent = new Intent(SplashActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // If current user is NOT anonymous user
                    // Get current user data from Parse.com
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser != null) {
                        // Send logged in users to Welcome.class
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Send user to LoginSignupActivity.class
                        Intent intent = new Intent(SplashActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }


}


