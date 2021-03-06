package com.glassbyte.film_quiz2;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.TouchUtils;
import android.util.Log;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest
//import android.support.test.runner.AndroidJUnitRunner;
//import android.support.test.runner.AndroidJUnit4;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;

import com.glassbyte.film_quiz2.Intro;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dalvik.annotation.TestTarget;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withID;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@MediumTest


//To test that the menu is called correctly
public class StartAgain extends TestCase {
    final Button sendToReceiverButton = (start_again)
            mSenderActivity.findViewById(R.id.button);

    Instrumentation.ActivityMonitor receiverActivityMonitor =
            getInstrumentation().addMonitor(ReceiverActivity.class.getName(), null, false);

    public Button getSendToReceiverButton() {
        return sendToReceiverButton;
    }

    TouchUtils.clickView(this, sendToReceiverButton);
    ReceiverActivity receiverActivity = (ReceiverActivity)
            receiverActivityMonitor.waitForActivityWithTimeout(5000);
    assertNotNull ("ReceiverActivity is null", receiverActivity);
    assertEquals("Monitor for ReceiverActivity has not been called", 1, receiverActivityMonitor.getHits);
    assertEquals("Activity is of wrong type", ReceiverActivity.class, receiverActivity.getClass());

    getInstrumentation().removeMonitor(receiverActivityMonitor);


}