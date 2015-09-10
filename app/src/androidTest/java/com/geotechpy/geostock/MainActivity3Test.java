package com.geotechpy.geostock;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.geotechpy.geostock.app.GeotechpyStockApp;
import com.geotechpy.geostock.models.Stock;
import com.geotechpy.geostock.resources.VolleyIdlingResource;
import com.geotechpy.geostock.rules.ActivityRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.geotechpy.geostock.app.GeotechpyStockApp.getLastToastMessage;
import static com.geotechpy.geostock.matchers.CustomMatchers.withAdaptedData;
import static com.geotechpy.geostock.matchers.CustomMatchers.withStockSerNr;
import static com.geotechpy.geostock.matchers.CustomMatchers.withStockStatus;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivity3Test {

    @Rule
    public final ActivityRule<MainActivity> main = new ActivityRule<>(MainActivity.class);

    static VolleyIdlingResource volleyIdlingResource;
    Context ctx;

    @BeforeClass
    public static void registerIntentServiceIdlingResource() {
        GeotechpyStockApp.setFakeHttpConnections(true);
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
    public void setUp(){
        ctx = main.instrumentation().getTargetContext();
        GeotechpyStockApp.setLastToastMessage("");
    }

    @Test
    public void test1_mustInsertDataToSync() throws InterruptedException {
        onView(withId(R.id.et_user)).perform(clearText(), typeText("ancho"), closeSoftKeyboard());
        Thread.sleep(2000);
        onView(withId(R.id.et_password)).perform(clearText(), typeText("666"), closeSoftKeyboard());
        Thread.sleep(2000);
        onView(withText(R.string.login)).perform(click());
        onView(withId(R.id.btn_deposit)).perform(click());
        onData(allOf(is(instanceOf(Stock.class)), withStockSerNr(1)))
                .onChildView(withId(R.id.ib_edit))
                .atPosition(0)
                .perform(click());
        onView(withId(R.id.btn_new)).perform(click());
        onView(withId(R.id.et_item_code))
                .perform(clearText(), typeText("100100"), closeSoftKeyboard());
        Thread.sleep(2000);
        onView(withId(R.id.et_item_qty))
                .perform(clearText(), typeText("100"), closeSoftKeyboard());
        Thread.sleep(2000);
        onView(withId(R.id.btn_ok)).perform(click());
        //onView(withId(R.id.btn_cancel)).perform(click());
        pressBack();
        pressBack();
        pressBack();
        pressBack();
        Thread.sleep(2000);
        onView(withText(R.string.confirm_exit)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
    }

    @Test
    public void test2_shouldSendDataToServer() throws InterruptedException{
        onView(withId(R.id.et_user)).perform(clearText(), typeText("ancho"), closeSoftKeyboard());
        Thread.sleep(2000);
        onView(withId(R.id.et_password)).perform(clearText(), typeText("666"), closeSoftKeyboard());
        Thread.sleep(2000);
        onView(withText(R.string.login)).perform(click());
        onView(withId(R.id.btn_deposit)).perform(click());
        onData(allOf(is(instanceOf(Stock.class)), withStockSerNr(1)))
                .onChildView(withId(R.id.ib_sync)) //resource id of first column from xml layout
                .perform(click());
        Thread.sleep(2000);
        onView(withText(R.string.save_stock)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button2)).perform(click()); //cancel sync
        onData(allOf(is(instanceOf(Stock.class)), withStockSerNr(1)))
                .onChildView(withId(R.id.ib_sync)) //resource id of first column from xml layout
                .perform(click());
        Thread.sleep(2000);
        onView(withText(R.string.save_stock)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click()); //confirm sync
        assertThat(getLastToastMessage(), equalTo(ctx.getString(R.string.db_sync) + ". ok: " + "Sincronizacion exitosa"));
        String confirmed = ctx.getString(R.string.stock_confirmed);
        onView(withId(R.id.lv_stocks)).check(matches(withAdaptedData(withStockStatus(confirmed))));
        onData(allOf(is(instanceOf(Stock.class)), withStockStatus(confirmed)))
                .onChildView(withId(R.id.ib_sync)) //resource id of first column from xml layout
                .perform(click());
        assertThat(getLastToastMessage(), is(equalTo(ctx.getString(R.string.stock_already_sync))));
        Thread.sleep(2000);
        onData(allOf(is(instanceOf(Stock.class)), withStockSerNr(1)))
                .onChildView(withId(R.id.ib_delete)) //resource id of third column from xml layout
                .perform(click());
        onView(withText(R.string.sure_delete)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.lv_stocks)).check(matches(not(withAdaptedData(withStockSerNr(1))))); //there is nothing
    }
}
