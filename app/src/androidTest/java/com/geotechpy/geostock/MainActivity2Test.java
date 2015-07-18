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
import static android.support.test.espresso.Espresso.pressBack;
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
import static org.hamcrest.Matchers.not;

/**
 * Volley network sync test
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivity2Test {

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
    public void shouldDatabaseSync() throws InterruptedException{
        Thread.sleep(5000);
        onView(withId(R.id.btn_sync)).perform(click());
        onView(withText(R.string.confirm_action)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText(R.string.db_sync)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldDisplayStockTypeDepositOnUserLogin() throws InterruptedException{
        onView(withId(R.id.et_user)).perform(clearText(), typeText("ancho"), closeSoftKeyboard());
        Thread.sleep(2000);
        onView(withId(R.id.et_password)).perform(clearText(), typeText("777"), closeSoftKeyboard());
        Thread.sleep(2000);
        onView(withText(R.string.login)).perform(click());
        onView(withText(R.string.invalid_password)).check(matches(isDisplayed()));

        onView(withId(R.id.et_password)).perform(clearText(), typeText("666"), closeSoftKeyboard());
        Thread.sleep(2000);
        onView(withText(R.string.login)).perform(click());
        onView(withId(R.id.btn_deposit)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_laboratory)).check(matches(not(isDisplayed())));
        onView(withText("ancho")).check(matches(isDisplayed()));

        onView(withId(R.id.btn_deposit)).perform(click());
        onView(withId(R.id.tv_stocklist_username)).check(matches(isDisplayed()));

        pressBack();
        pressBack();
        onView(withText(R.string.confirm_exit)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());

    }

    @Test
    public void shouldDisplayStockTypeLaboratoryOnUserLogin() throws InterruptedException{
        onView(withId(R.id.et_user)).perform(clearText(), typeText("alex"), closeSoftKeyboard());
        Thread.sleep(2000);
        onView(withId(R.id.et_password)).perform(clearText(), typeText("777"), closeSoftKeyboard());
        Thread.sleep(2000);
        onView(withText(R.string.login)).perform(click());
        onView(withId(R.id.btn_laboratory)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_deposit)).check(matches(not(isDisplayed())));
        onView(withText("alex")).check(matches(isDisplayed()));

        pressBack();
        onView(withText(R.string.confirm_exit)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button2)).perform(click());
        onView(withId(R.id.tv_stocktype_username)).check(matches(isDisplayed()));
    }
}
