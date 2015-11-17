package com.glitterlabs.caixia.suite;

import com.glitterlabs.caixia.LoginTest;
import com.glitterlabs.caixia.SignUpTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by mohinish on 11/5/15.
 */


// Runs all unit tests.
@RunWith(Suite.class)

@Suite.SuiteClasses({
            SignUpTest.class,
            LoginTest.class
        })

public class CaixiaTestSuite {


}
