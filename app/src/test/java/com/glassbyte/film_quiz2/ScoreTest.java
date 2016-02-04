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


//To test that the score displays correctly when the game ends
public class ScoreTest extends TestCase {
    public ScoreTest(){
        super(Intro.class);
    }

    Activity myActivity = null;

    @rule
    public ActivityTestRule<Intro> = new ActivityTestRule<>(Intro.class);

    @Test
    public void testPlot(){
        onView(withId(R.id.button)).perform(click());
        sleep(20000);
        String expectedText = "0";
        onView(withID(R.id.point_text)).check(matches(withText(expectedText)));

    }*/


}