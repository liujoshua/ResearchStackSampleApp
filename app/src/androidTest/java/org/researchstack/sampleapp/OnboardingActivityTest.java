package org.researchstack.sampleapp;

import android.content.ComponentName;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.researchstack.skin.ui.OnboardingActivity;
import org.researchstack.skin.ui.SignUpTaskActivity;
import org.researchstack.skin.ui.SplashActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by liujoshua on 9/19/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class OnboardingActivityTest {

    @Rule
    public IntentsTestRule<OnboardingActivity> activityRule = new IntentsTestRule<>(OnboardingActivity.class);

    @Test
    public void testClickSignUp() throws InterruptedException {
        onView(withId(R.id.intro_sign_up)).perform(click());

        intended(hasComponent(new ComponentName("org.researchstack.skin", "org.researchstack.skin.ui.SignUpTaskActivity")));
    }
}
