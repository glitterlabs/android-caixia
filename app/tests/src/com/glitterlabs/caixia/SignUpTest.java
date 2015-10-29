package com.glitterlabs.caixia;


/**
 * Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
 * All rights reserved. Patents pending.
 * Responsible: Abhay Bhusari
 *
 */

import android.test.ActivityInstrumentationTestCase2;


public class SignUpTest extends ActivityInstrumentationTestCase2<SignUpActivity> {



    public SignUpTest() {
        super(SignUpActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SignUpActivity signUpActivity = getActivity();
        signUpActivity.signUp();


    }

    public void testPreconditions() {

    }




}