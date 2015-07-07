package com.geotechpy.geostock;


import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Login activity test case
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule public final ActivityRule<MainActivity> main = new ActivityRule<>(MainActivity.class);

    @Test
    public void shouldBeAbleToLaunchMainScreen(){
        onView(withText(R.string.username)).check(ViewAssertions.matches(isDisplayed()));
        onView(withText(R.string.password)).check(ViewAssertions.matches(isDisplayed()));

    }

}
