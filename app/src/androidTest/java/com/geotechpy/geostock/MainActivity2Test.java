package com.geotechpy.geostock;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import com.geotechpy.geostock.models.Stock;
import com.geotechpy.geostock.models.StockDetail;
import com.geotechpy.geostock.models.Zone;
import com.geotechpy.geostock.rules.ActivityRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.geotechpy.geostock.matchers.CustomMatchers.editTextNotEditable;
import static com.geotechpy.geostock.matchers.CustomMatchers.withAdaptedData;
import static com.geotechpy.geostock.matchers.CustomMatchers.withStockDetailLineNumber;
import static com.geotechpy.geostock.matchers.CustomMatchers.withStockSerNr;
import static com.geotechpy.geostock.matchers.CustomMatchers.withStockStatus;
import static com.geotechpy.geostock.matchers.CustomMatchers.withStockType;
import static com.geotechpy.geostock.matchers.CustomMatchers.withZoneSerNr;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Volley network sync test
 */

@RunWith(AndroidJUnit4.class)
@MediumTest
public class MainActivity2Test {

    Context ctx;

    @Rule
    public final ActivityRule<MainActivity> main = new ActivityRule<>(MainActivity.class);

    @Before
    public void setUp(){
        ctx = main.instrumentation().getTargetContext();
    }


    @Test
    public void shouldDisplayStockTypeDepositOnUserLogin() throws InterruptedException {
        onView(withId(R.id.et_user)).perform(clearText(), typeText("ancho"), closeSoftKeyboard());
        Thread.sleep(2000);
        onView(withId(R.id.et_password)).perform(clearText(), typeText("777"), closeSoftKeyboard());
        Thread.sleep(2000);
        onView(withText(R.string.login)).perform(click());
        onView(withText(R.string.invalid_password)).check(matches(isDisplayed()));

        onView(withId(R.id.et_password)).perform(clearText(), typeText("666"), closeSoftKeyboard());
        Thread.sleep(2000);
        onView(withText(R.string.login)).perform(click());

        //StockTypeActivity
        onView(withId(R.id.btn_deposit)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_laboratory)).check(matches(not(isDisplayed())));
        onView(withText("ancho")).check(matches(isDisplayed()));
        onView(withId(R.id.btn_deposit)).perform(click());

        //StockListActivity
        onView(withId(R.id.tv_stocklist_username)).check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(Stock.class)), withStockSerNr(1))).check(matches(isDisplayed()));

        String active = ctx.getString(R.string.stock_active);
        onData(allOf(is(instanceOf(Stock.class)), withStockStatus(active)))
                .onChildView(withId(R.id.ib_delete)) //resource id of third column from xml layout
                .perform(click());
        onView(withText(R.string.confirm_action)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
        onData(allOf(is(instanceOf(Stock.class)), withStockSerNr(2)))
                .onChildView(withId(R.id.ib_sync)) //resource id of first column from xml layout
                .perform(click());
        onView(withId(R.id.btn_new_stock)).perform(click());

        //StockZoneListActivity
        onView(withId(R.id.btn_ok)).perform(click()); //must select one first
        onData(allOf(is(instanceOf(Zone.class)), withZoneSerNr(1)))
                .perform(click());
        onData(allOf(is(instanceOf(Zone.class)), withZoneSerNr(1)))
                .check(matches(isChecked()));
        onView(withId(R.id.btn_cancel)).perform(click());
        onView(withId(R.id.btn_new_stock)).perform(click());
        onData(allOf(is(instanceOf(Zone.class)), withZoneSerNr(3)))
                .perform(click());
        onView(withId(R.id.btn_ok)).perform(click());
        onData(allOf(is(instanceOf(Stock.class)), withStockSerNr(4)))
                .check(matches(isDisplayed())); //created stock appears
        String deposit = ctx.getString(R.string.zone_deposit);
        onData(allOf(is(instanceOf(Stock.class)), withStockType(deposit)))
                .onChildView(withId(R.id.ib_edit)) //resource id of second column from xml layout
                .atPosition(1)
                .perform(click());

        //ItemListActivity
        onView(withText("ancho")).check(matches(isDisplayed()));
        onView(withId(R.id.tv_itemlist_stock_sernr)).check(matches(withText("4")));
        onView(withId(R.id.tv_itemlist_stock_zone_code)).check(matches(withText("3")));
        onView(withId(R.id.lv_itemlist_items)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_new)).perform(click());
        onView(withId(R.id.btn_cancel)).perform(click());
        onView(withId(R.id.btn_new)).perform(click());

        //ItemActivity
        onView(withId(R.id.et_item_code)).check(matches(not(editTextNotEditable())));
        onView(withId(R.id.btn_ok)).perform(click()); //no code toast
        onView(withId(R.id.et_item_code))
                .perform(clearText(), typeText("1010"), closeSoftKeyboard());
        onView(withId(R.id.btn_ok)).perform(click()); // no name toast
        onView(withId(R.id.et_item_name))
                .perform(clearText(), typeText("testItem"), closeSoftKeyboard());
        onView(withId(R.id.btn_ok)).perform(click()); // no qty toast
        onView(withId(R.id.et_item_qty))
                .perform(clearText(), typeText("33"), closeSoftKeyboard());
        onView(withId(R.id.btn_ok)).perform(click()); //item created toast

        //ItemListActivity
        onData(allOf(is(instanceOf(StockDetail.class)), withStockDetailLineNumber(1)))
                .onChildView(withId(R.id.ib_items_edit))
                .perform(click());

        //ItemActivity
        onView(withId(R.id.et_item_code)).check(matches(editTextNotEditable()));
        onView(withId(R.id.et_item_name))
                .perform(clearText(), typeText("testItemUpdated"), closeSoftKeyboard());
        onView(withId(R.id.et_item_qty))
                .perform(clearText(), typeText("34"), closeSoftKeyboard());
        onView(withId(R.id.btn_ok)).perform(click()); //item updated toast

        //ItemListActivity
        onData(allOf(is(instanceOf(StockDetail.class)), withStockDetailLineNumber(1)))
                .onChildView(withId(R.id.ib_items_delete))
                .perform(click());
        onView(withText(R.string.confirm_action)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.lv_itemlist_items))
                .check(matches(not(withAdaptedData(is(withStockDetailLineNumber(1))))));

        pressBack(); //back to StockActivity
        pressBack(); //back to StockTypeActivity
        pressBack(); //Try to go back to MainActivity (Login) and exit
        onView(withText(R.string.confirm_exit)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
    }

    @Test
    public void shouldDisplayStockTypeLaboratoryOnUserLogin() throws InterruptedException {
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
