package com.geotechpy.geostock;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        createApplication();
    }

    public void test_getAppName() {
        Application app = getApplication();
        assertNotNull(app);
        assertEquals("com.geotechpy.geostock.debug", app.getPackageName());
    }
}