package org.sagebionetworks.bridge.researchstack;

import android.os.Build;
import android.text.TextUtils;

/**
 * Created by liujoshua on 9/12/16.
 */
public class BridgeIdentifiers {

    public final String studyName;
    public final int appVersion;

    public BridgeIdentifiers(String studyName, int appVersion) {
        this.studyName = studyName;
        this.appVersion = appVersion;
    }

    public final String getUserAgent() {
        return studyName + "/" + appVersion + " (" + getDeviceName() + "; Android " + Build.VERSION.RELEASE + ") BridgeSDK/0";
    }

    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        if (TextUtils.isEmpty(manufacturer)) {
            manufacturer = "Unknown";
        }

        String model = Build.MODEL;
        if (TextUtils.isEmpty(model)) {
            model = "Android";
        }

        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
