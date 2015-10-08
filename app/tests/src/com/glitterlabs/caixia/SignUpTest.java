package com.glitterlabs.caixia;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by mohinish on 9/24/15.
 */
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