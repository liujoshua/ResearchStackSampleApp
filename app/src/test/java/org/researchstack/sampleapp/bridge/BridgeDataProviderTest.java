package org.researchstack.sampleapp.bridge;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.researchstack.sampleapp.BuildConfig;
import org.researchstack.sampleapp.SampleDataProvider;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.sagebionetworks.bridge.researchstack.BridgeDataProvider;

import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;


/**
 * Created by liujoshua on 8/10/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class BridgeDataProviderTest {
    /**
     * For example, "App Name/14".
     */
    private static final Pattern SHORT_STRING = Pattern.compile("^([^/]+)\\/(\\d{1,9})($)");
    /**
     * For example, "Unknown Client/14 BridgeJavaSDK/10".
     */
    private static final Pattern MEDIUM_STRING = Pattern.compile("^([^/]+)\\/(\\d{1,9})\\s([^/\\(]*)\\/(\\d{1,9})($)");
    /**
     * For example, "Asthma/26 (Unknown iPhone; iPhone OS 9.1) BridgeSDK/4" or
     * "Asthma/26 (Unknown iPhone; iPhone OS/9.1) BridgeSDK/4"
     */
    private static final Pattern LONG_STRING = Pattern.compile("^([^/]+)\\/(\\d{1,9})\\s\\(([^;]+);([^\\)]*)\\)\\s([^/]*)\\/(\\d{1,9})($)");

    private BridgeDataProvider bridgeDataProvider;

    @Before
    public void setUp() {
        bridgeDataProvider = new SampleDataProvider();
    }

    @Test
    public void testGetUserAgent_MatchesValidFormat() {
        String userAgent = bridgeDataProvider.getUserAgent();

        boolean isMatchForShortString = SHORT_STRING.matcher(userAgent).matches();
        boolean isMatchForMediumString = MEDIUM_STRING.matcher(userAgent).matches();
        boolean isMatchForLongString = LONG_STRING.matcher(userAgent).matches();

        assertTrue(isMatchForShortString || isMatchForMediumString || isMatchForLongString);
    }
}