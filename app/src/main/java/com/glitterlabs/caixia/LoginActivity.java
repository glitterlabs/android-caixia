package com.glitterlabs.caixia;
/**
 * Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
 * All rights reserved. Patents pending.
 * Responsible: Abhay Bhusari
 *
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.glitterlabs.caixia.util.Helper;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;


public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText etEmailId, etPassword;
    private TextView tvSignUp;
    static final String GOOGLE_SENDER_ID = "1043488337868";  // Place here your Google project id
    private static boolean loginStatus = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmailId = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String userNametext = etEmailId.getText().toString().trim();
                String passwordtext = etPassword.getText().toString().trim();
                if (Helper.isNetworkConnected(LoginActivity.this)) {
                      login(userNametext,passwordtext);
                } else {
                    Helper.showDialog("Network Error","Please check your internet connection and try again.",LoginActivity.this );
                }
            }
        });


        goToSignUpPage();

    }

    public void login(String userNametext,String passwordtext) {

        final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, null,
                getString(R.string.alert_wait));
        // Send data to Parse.com for verification
        ParseUser.logInInBackground(userNametext, passwordtext,
               new LogInCallback() {
                   public void done(ParseUser user, ParseException e) {
                       progressDialog.dismiss();

                       if (user != null) {

                           // If user exist and authenticated, send user to MainActivity.class

                           String userName = user.getString("username");
                           String userId = user.getObjectId();
                           loginStatus = true;
                           //save installation state for push notificatipon
                           //   saveInstallationState(userId);
                           //start main activity
                           Intent intent = new Intent(
                                   LoginActivity.this,
                                   MainActivity.class);
                           startActivity(intent);
                          /* Toast.makeText(getApplicationContext(),
                                   "Login Success",
                                   Toast.LENGTH_LONG).show();*/

                           finish();


                       } else {
                            loginStatus = false;
                           Toast.makeText(
                                   getApplicationContext(),
                                   "User not found",
                                   Toast.LENGTH_LONG).show();

                       }
                   }
               });
    }

    public boolean getLoginStatus ()
    {
        return loginStatus;
    }

public void saveInstallationState(String currentUserId){

    ParseInstallation installation= ParseInstallation.getCurrentInstallation();
    installation.put("userId",currentUserId);
    installation.put("GCMSenderId",GOOGLE_SENDER_ID);
    installation.saveInBackground();
}

    public void goToSignUpPage() {
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
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
