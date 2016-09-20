package org.sagebionetworks.bridge.researchstack;

import android.content.Context;

import com.google.gson.Gson;

import org.researchstack.backbone.StorageAccess;
import org.researchstack.skin.AppPrefs;
import org.sagebionetworks.bridge.researchstack.wrapper.StorageAccessWrapper;
import org.sagebionetworks.bridge.sdk.rest.BridgeService;

/**
 * Created by liujoshua on 9/19/16.
 */
public class BridgeDependencyLoader {

    private final Context applicationContext;
    private final Gson gson = new Gson();

    public BridgeDependencyLoader(Context applicationContext) {
        // make sure what we hold on to is really the application's context
        this.applicationContext = applicationContext.getApplicationContext();
    }

    public AppPrefs getAppPrefs() {
        return AppPrefs.getInstance(applicationContext);
    }

    public StorageAccessWrapper getStorageAccess() {
        return new StorageAccessWrapper();
    }

    public ConsentLocalStorage getConsentLocalStorage() {
        return new ConsentLocalStorage(applicationContext, gson, getStorageAccess().getFileAccess());
    }

    public UserLocalStorage getUserLocalStorage() {
        return new UserLocalStorage(applicationContext, gson, getStorageAccess().getFileAccess());
    }
}
