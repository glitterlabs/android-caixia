package com.glitterlabs.caixia;


/**
 * Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
 * All rights reserved. Patents pending.
 * Responsible: Abhay Bhusari
 *
 */

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class SignUpTest extends ActivityInstrumentationTestCase2<SignUpActivity> {
    private EditText mName, mUsername,mUsermailId,mUserPhone,mUserpassword;
    private Button mBtnSignUp;
    private TextView textGoToLogin;


private  SignUpActivity mSignUpActivity;
    public SignUpTest() {
        super(SignUpActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSignUpActivity = getActivity();

        mName= (EditText) mSignUpActivity.findViewById(R.id.etName);
        mUsername= (EditText) mSignUpActivity.findViewById(R.id.etUserName_SignUp);
        mUsermailId= (EditText) mSignUpActivity.findViewById(R.id.etEmailId_SignUp);
        mUserPhone= (EditText) mSignUpActivity.findViewById(R.id.etPhone_SignUp);
        mUserpassword= (EditText) mSignUpActivity.findViewById(R.id.etPassword_SignUp);

        mBtnSignUp = (Button) mSignUpActivity.findViewById(R.id.btnSignUp_SignUp);
        textGoToLogin= (TextView) mSignUpActivity.findViewById(R.id.tvLogin_SignUp);
    }

    public void testPreconditions() {

        assertNotNull("mName is null", mName);
        assertNotNull("mUsername is null", mUsername);
        assertNotNull("mUsermailId is null", mUsermailId);
        assertNotNull("mUserPhone is null", mUserPhone);
        assertNotNull("mUserpassword is null", mUserpassword);
        assertNotNull("mBtnSignUp is null", mBtnSignUp);
        assertNotNull("textGoToLogin is null", textGoToLogin);

    }
   /* @MediumTest
    public void testInfoTextViewTextIsEmpty() {
        //Verify that the mInfoTextView is initialized with the correct default value
        assertEquals("", textGoToLogin.getText());

    }*/
    public void testUserSignUp() throws InterruptedException {

        mSignUpActivity.signUp("Ramesh Yadav", "ramesh","ramesh.yadav@gmail.com","9423456456","123");
        Thread.sleep(10000);
        boolean status = mSignUpActivity.getSignUpStatus();
        assertEquals(true, status);

    }


}