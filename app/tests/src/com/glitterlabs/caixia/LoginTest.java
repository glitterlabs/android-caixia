
package com.glitterlabs.caixia;
/**
 * Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
 * All rights reserved. Patents pending.
 * Responsible: Abhay Bhusari
 *
 */

import android.test.ActivityInstrumentationTestCase2;


public class LoginTest extends ActivityInstrumentationTestCase2<LoginActivity> {



    public LoginTest() {
        super(LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        LoginActivity loginActivity = getActivity();
        loginActivity.login();

    }

    public void testPreconditions() {

    }




}