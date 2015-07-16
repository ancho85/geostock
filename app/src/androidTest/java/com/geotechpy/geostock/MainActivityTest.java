package com.geotechpy.geostock;


import android.support.test.runner.AndroidJUnit4;

import com.geotechpy.geostock.others.SystemAnimations;
import com.geotechpy.geostock.resources.VolleyIdlingResource;
import com.geotechpy.geostock.rules.ActivityRule;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Login activity test case
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public final ActivityRule<MainActivity> main = new ActivityRule<>(MainActivity.class);

    static VolleyIdlingResource volleyIdlingResource; //test3 uses an idling resource
    SystemAnimations systemAnimations;

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

    @Before
    public void setUp() {
        systemAnimations = new SystemAnimations(main.get().getApplicationContext());
        systemAnimations.disableAll();
    }

    @After
    public void tearDown() {
        systemAnimations.enableAll();
    }

    @Test
    public void test1_shouldBeAbleToLaunchMainScreen() {
        onView(withText(R.string.username)).check(matches(isDisplayed()));
        onView(withText(R.string.password)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_reset)).check(matches(withText(R.string.reset)));
        onView(withId(R.id.btn_sync)).check(matches(withText(R.string.sync)));
        onView(withId(R.id.btn_login)).check(matches(withText(R.string.login)));
        onView(withId(R.id.btn_login)).perform(click());

    }

    //@Test
    public void test2_shouldButtonDeleteDatabase() {
        onView(withId(R.id.btn_reset)).perform(click());
        onView(withText(R.string.confirm_title)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());

        //Toast text
        onView(withText(R.string.db_reset)
            ).inRoot(withDecorView(
                not(main.get().getWindow().getDecorView())
        )).check(matches(isDisplayed()));
    }

    //@Test
    public void test3_shouldDatabaseSync() throws InterruptedException {
        onView(withId(R.id.btn_sync)).perform(click());
        onView(withText(R.string.confirm_action)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText(R.string.db_sync)).check(matches(isDisplayed()));
    }

    //@Test
    public void test4_shouldButtonLoginUser(){
        onView(withId(R.id.btn_login)).check(matches(withText(R.string.login)));
        onView(withId(R.id.btn_login)).perform(click());
        onView(withText(R.string.empty_field_user)).check(matches(isDisplayed()));

        onView(withId(R.id.et_user)).perform(clearText(), typeText("no_user"));
        onView(withId(R.id.btn_login)).perform(click());
        onView(withText(R.string.empty_field_password)).check(matches(isDisplayed()));

        onView(withId(R.id.et_password)).perform(clearText(), typeText("no_pass"));
        onView(withId(R.id.btn_login)).perform(click());
        onView(withText(R.string.invalid_user)).check(matches(isDisplayed()));
    }

}
