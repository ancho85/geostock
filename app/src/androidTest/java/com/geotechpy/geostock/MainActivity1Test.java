package com.geotechpy.geostock;


import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.geotechpy.geostock.resources.VolleyIdlingResource;
import com.geotechpy.geostock.rules.ActivityRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Login activity test case
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivity1Test {

    @Rule
    public final ActivityRule<MainActivity> main = new ActivityRule<>(MainActivity.class);

    static VolleyIdlingResource volleyIdlingResource;

    @BeforeClass
    public static void registerIntentServiceIdlingResource() {
        try {
            volleyIdlingResource = new VolleyIdlingResource("VolleyCalls");
            registerIdlingResources(volleyIdlingResource);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void unregisterIntentServiceIdlingResource() {
        unregisterIdlingResources(volleyIdlingResource);
    }

    @Test
    public void test1_shouldBeAbleToLaunchMainScreen() {
        onView(withText(R.string.username)).check(matches(isDisplayed()));
        onView(withText(R.string.password)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_reset)).check(matches(withText(R.string.reset)));
        onView(withId(R.id.btn_sync)).check(matches(withText(R.string.sync)));
        onView(withId(R.id.btn_login)).check(matches(withText(R.string.login)));
    }

    @Test
    public void test2_shouldButtonDeleteDatabase() {
        onView(withText(R.string.reset)).perform(click());
        onView(withText(R.string.confirm_title)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText(R.string.db_reset)).check(matches(isDisplayed()));
    }


    @Test
    public void test3_shouldDatabaseSync() throws InterruptedException {
        Thread.sleep(5000);
        onView(withId(R.id.btn_sync)).perform(click());
        onView(withText(R.string.confirm_action)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText(R.string.db_sync)).check(matches(isDisplayed()));
    }

    @Test
    public void test4_shouldButtonLoginUser() throws InterruptedException {
        onView(withText(R.string.login)).perform(click());
        onView(withText(R.string.empty_field_user)).check(matches(isDisplayed()));

        onView(withId(R.id.et_user)).perform(clearText(), typeText("no_user"), closeSoftKeyboard());
        Thread.sleep(2000);
        onView(withText(R.string.login)).perform(click());
        onView(withText(R.string.empty_field_password)).check(matches(isDisplayed()));

        onView(withId(R.id.et_password)).perform(clearText(), typeText("no_pass"), closeSoftKeyboard());
        Thread.sleep(2000);
        onView(withText(R.string.login)).perform(click());
        onView(withText(R.string.invalid_user)).check(matches(isDisplayed()));
    }
}
