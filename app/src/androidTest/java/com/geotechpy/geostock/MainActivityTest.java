package com.geotechpy.geostock;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Login activity test case
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public final ActivityRule<MainActivity> main = new ActivityRule<>(MainActivity.class);

    @Test
    public void shouldBeAbleToLaunchMainScreen() {
        onView(withText(R.string.username)).check(matches(isDisplayed()));
        onView(withText(R.string.password)).check(matches(isDisplayed()));

    }

    @Test
    public void shouldButtonDeleteDatabase() {
        onView(withId(R.id.btn_reset)).perform(click());
        onView(withText(R.string.confirm_title)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());

        //Toast text
        onView(withText(R.string.db_reset)
            ).inRoot(withDecorView(
                not(main.get().getWindow().getDecorView())
                )).check(matches(isDisplayed()));
    }

}
