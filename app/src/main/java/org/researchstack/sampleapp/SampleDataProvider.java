package org.researchstack.sampleapp;

import android.content.Context;
import android.os.Build;

import org.researchstack.backbone.ResourcePathManager;
import org.researchstack.backbone.result.TaskResult;
import org.sagebionetworks.bridge.researchstack.BridgeDataProvider;
import org.researchstack.skin.ResourceManager;
import org.sagebionetworks.bridge.researchstack.BridgeIdentifiers;


public class SampleDataProvider extends BridgeDataProvider {
    public SampleDataProvider() {
        super(BuildConfig.STUDY_BASE_URL, BuildConfig.STUDY_ID,
                new BridgeIdentifiers(BuildConfig.STUDY_NAME, BuildConfig.VERSION_CODE).getUserAgent());
    }

    @Override
    public void processInitialTaskResult(Context context, TaskResult taskResult) {
        // handle result from initial task (save profile info to disk, upload to your server, etc)
    }

    @Override
    protected ResourcePathManager.Resource getPublicKeyResId() {
        return new SampleResourceManager.PemResource("bridge_key");
    }

    @Override
    protected ResourcePathManager.Resource getTasksAndSchedules() {
        return ResourceManager.getInstance().getTasksAndSchedules();
    }
}
