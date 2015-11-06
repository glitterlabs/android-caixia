
package com.glitterlabs.caixia;
/**
 * Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
 * All rights reserved. Patents pending.
 * Responsible: Abhay Bhusari
 */

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;


public class LoginTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private LoginActivity mloginActivity;
    private EditText mUserName, mPassword;


    public LoginTest() {
        super(LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mloginActivity = getActivity();
        mUserName = (EditText) mloginActivity.findViewById(R.id.etUsername);
        mPassword = (EditText) mloginActivity.findViewById(R.id.etPassword);

    }

    public void testPreconditions() {

       // assertNotNull("mUserName is null", mUserName);
      //  assertNotNull("mPassword is null", mPassword);

    }

    public void testMyUserLogin_login() throws InterruptedException {

        mloginActivity.login("abhay", "123");
        Thread.sleep(10000);
        boolean status = mloginActivity.getLoginStatus();
        assertEquals(true,status);

    }


}