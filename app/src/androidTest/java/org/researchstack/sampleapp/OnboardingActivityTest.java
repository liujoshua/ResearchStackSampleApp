package org.researchstack.sampleapp;

import android.content.ComponentName;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.researchstack.skin.ui.OnboardingActivity;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by liujoshua on 9/19/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class OnboardingActivityTest {

    @Rule
    public IntentsTestRule<OnboardingActivity> activityRule = new IntentsTestRule<>(OnboardingActivity.class);

    @Before
    public void setup() {
        closeSoftKeyboard();
    }

    @Test
    public void testClickSignUp() {
        ViewInteraction introSignupButton = onView(withId(R.id.intro_sign_up));

        introSignupButton.perform(scrollTo());

        introSignupButton.perform(click());

        intended(hasComponent(new ComponentName("org.researchstack.skin", "org.researchstack.skin.ui.SignUpTaskActivity")));
    }
}
