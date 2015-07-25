package com.geotechpy.geostock;

import android.os.SystemClock;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.geotechpy.geostock.rules.ActivityRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Any activity resumed after a kill from OS must redirect to MainActivity
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class RespawnLoginTest {

    @Rule
    public final ActivityRule<StockTypeActivity> main = new ActivityRule<>(StockTypeActivity.class);

    @Test
    public void stockTypeRespawn() throws InterruptedException {
        //Toast text trying to open an activity with no userName set
        /*onView(withText(R.string.session_expired)
        ).inRoot(withDecorView(
                not(main.get().getWindow().getDecorView())
        )).check(matches(isDisplayed()));*/
        Thread.sleep(3000);
        onView(withText(R.string.username)).check(matches(isDisplayed()));
        onView(withText(R.string.password)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_reset)).check(matches(withText(R.string.reset)));
        onView(withId(R.id.btn_sync)).check(matches(withText(R.string.sync)));
        onView(withId(R.id.btn_login)).check(matches(withText(R.string.login)));
    }
}
