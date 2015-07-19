package com.geotechpy.geostock.matchers;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.geotechpy.geostock.models.Stock;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.android.support.test.deps.guava.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;

/**
 * Matchers for espresso
 */
public class CustomMatchers {


    /**
     * Finds the AdapterView and let another Matcher interrogate the data within it.
     */
    public static Matcher<View> withAdaptedData(final Matcher<Object> dataMatcher) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("with class name: ");
                dataMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof AdapterView)) {
                    return false;
                }
                @SuppressWarnings("rawtypes")
                Adapter adapter = ((AdapterView) view).getAdapter();
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (dataMatcher.matches(adapter.getItem(i))) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    /**
     * Matches an item from an AdapterView with a specific String.
     * (The items in AdapterView should be strings)
     */
    public static Matcher<Object> withItemContent(String expectedText) {
        checkNotNull(expectedText);
        return withItemContent(equalTo(expectedText));
    }

    public static Matcher<Object> withItemContent(final Matcher<String> itemTextMatcher) {
        checkNotNull(itemTextMatcher);
        return new BoundedMatcher<Object, String>(String.class) {
            @Override
            public boolean matchesSafely(String text) {
                return itemTextMatcher.matches(text);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with item content: ");
                itemTextMatcher.describeTo(description);
            }
        };
    }

    /**
     * Matches a Stock with a specific SerNr
     */
    public static Matcher<Object> withStockSerNr(final int stockSerNr) {
        return new BoundedMatcher<Object, Stock>(Stock.class) {
            @Override
            protected boolean matchesSafely(Stock stock) {
                return stockSerNr == stock.getSernr();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with id: " + stockSerNr);
            }
        };
    }

    /**
     * Matches a Stock with a specific status
     */
    public static Matcher<Object> withStockStatus(final String stockStatus) {
        return new BoundedMatcher<Object, Stock>(Stock.class) {
            @Override
            protected boolean matchesSafely(Stock stock) {
                return stockStatus.equals(stock.getStatus());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with id: " + stockStatus);
            }
        };
    }

    /**
     * Matches a Stock with a specific type
     */
    public static Matcher<Object> withStockType(final String stockType) {
        return new BoundedMatcher<Object, Stock>(Stock.class) {
            @Override
            protected boolean matchesSafely(Stock stock) {
                return stockType.equals(stock.getType());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with id: " + stockType);
            }
        };
    }

}
