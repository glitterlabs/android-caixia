package com.glitterlabs.caixia;
/**
 * Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
 * All rights reserved. Patents pending.
 * Responsible: Abhay Bhusari
 *
 */

import android.app.ProgressDialog;
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
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName, etUserName, etEmailId, etPhone, etPassword;
    private Button btnSignUp;
    private TextView tvLogin;
    private int i = 0;
    private boolean signUpStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etName = (EditText) findViewById(R.id.etName);
        etUserName = (EditText) findViewById(R.id.etUserName_SignUp);
        etEmailId = (EditText) findViewById(R.id.etEmailId_SignUp);
        etPhone = (EditText) findViewById(R.id.etPhone_SignUp);
        etPassword = (EditText) findViewById(R.id.etPassword_SignUp);
        btnSignUp = (Button) findViewById(R.id.btnSignUp_SignUp);
        tvLogin = (TextView) findViewById(R.id.tvLogin_SignUp);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (Helper.isNetworkConnected(SignUpActivity.this)) {
                        // Retrieve the text entered from the EditText
                        String nametext = etName.getText().toString().trim();
                        String userNametext = etUserName.getText().toString().trim();
                        String emailtext = etEmailId.getText().toString().trim();
                        String phonetext = etPhone.getText().toString().trim();
                        String passwordtext = etPassword.getText().toString();

                        signUp(nametext,userNametext,emailtext,phonetext,passwordtext);

                    } else {
                        Helper.showDialog("Network Error", "Please check your internet connection and try again.", SignUpActivity.this);

                    }

            }
        });

    }

    public void signUp(String nametext,String userNametext,String emailtext, String phonetext,String passwordtext) {


        // Force user to fill up the form
        if (nametext.equalsIgnoreCase("") && userNametext.equalsIgnoreCase("") && emailtext.equalsIgnoreCase("") && phonetext.equalsIgnoreCase("") && passwordtext.equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(),
                    "Please complete the sign up form",
                    Toast.LENGTH_LONG).show();

        } else {
            // Save new user data into Parse.com Data Storage
            ParseUser user = new ParseUser();
            user.setUsername(userNametext);
            user.setEmail(emailtext);
            user.put("Name", nametext);
            user.put("phone", phonetext);
            user.setPassword(passwordtext);

            final ProgressDialog progressDialog = ProgressDialog.show(SignUpActivity.this, null,
                    getString(R.string.alert_wait));

            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    progressDialog.dismiss();
                    if (e == null) {
                        signUpStatus=true;
                        // Show a simple Toast message upon successful registration
                        /*Toast.makeText(getApplicationContext(),
                                "Successfully Signed up",
                                Toast.LENGTH_LONG).show();*/
                        finish();
                    } else {
                        signUpStatus=false;
                        Toast.makeText(getApplicationContext(),
                                "Sign up Error", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });

        }


    }
    public boolean getSignUpStatus ()
    {
        return signUpStatus;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
