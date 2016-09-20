package org.sagebionetworks.bridge.researchstack;

import android.content.Context;

import com.google.common.collect.Lists;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.researchstack.backbone.ResourcePathManager;
import org.researchstack.backbone.result.TaskResult;
import org.researchstack.backbone.storage.file.EncryptionProvider;
import org.researchstack.backbone.storage.file.FileAccess;
import org.researchstack.backbone.storage.file.PinCodeConfig;
import org.researchstack.backbone.task.Task;
import org.researchstack.skin.AppPrefs;
import org.researchstack.skin.DataProvider;
import org.researchstack.skin.DataResponse;
import org.researchstack.skin.model.SchedulesAndTasksModel;
import org.researchstack.skin.model.User;
import org.sagebionetworks.bridge.researchstack.wrapper.StorageAccessWrapper;
import org.sagebionetworks.bridge.sdk.rest.BridgeService;
import org.sagebionetworks.bridge.sdk.rest.UserSessionInfo;
import org.sagebionetworks.bridge.sdk.rest.model.BridgeMessageResponse;
import org.sagebionetworks.bridge.sdk.rest.model.EmailBody;
import org.sagebionetworks.bridge.sdk.rest.model.SignInBody;
import org.sagebionetworks.bridge.sdk.rest.model.SignUpBody;

import retrofit2.Response;
import rx.Observable;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by liujoshua on 9/12/16.
 */
public class BridgeDataProviderUserStateTest extends BridgeDataProviderTestBase {

    protected Gson gson = new Gson();

    @Before
    public void beforeTest() {
        publicKeyRes = mock(ResourcePathManager.Resource.class);
        tasksAndSchedulesRes = mock(ResourcePathManager.Resource.class);
        bridgeService = mock(BridgeService.class);
        appPrefs = mock(AppPrefs.class);


        pinCodeConfig = mock(PinCodeConfig.class);
        encryptionProvider = mock(EncryptionProvider.class);
        fileAccess = new MockFileAccess();
        appDatabase = mock(BridgeEncryptedDatabase.class);

        gson = new Gson();

        storageAccess = mock(StorageAccessWrapper.class);
        when(storageAccess.getPinCodeConfig()).thenReturn(pinCodeConfig);
        when(storageAccess.getAppDatabase()).thenReturn(appDatabase);
        when(storageAccess.getFileAccess()).thenReturn(fileAccess);
        consentLocalStorage = new ConsentLocalStorage(context, gson, fileAccess);
        userLocalStorage = new UserLocalStorage(context, gson, fileAccess);

        bridgeDependencyLoader = mock(BridgeDependencyLoader.class);
        when(bridgeDependencyLoader.getAppPrefs()).thenReturn(appPrefs);
        when(bridgeDependencyLoader.getConsentLocalStorage()).thenReturn(consentLocalStorage);
        when(bridgeDependencyLoader.getStorageAccess()).thenReturn(storageAccess);
        when(bridgeDependencyLoader.getUserLocalStorage()).thenReturn(userLocalStorage);

        dataProvider = new TestBridgeDataProvider(publicKeyRes, tasksAndSchedulesRes, bridgeDependencyLoader, bridgeService);
        context = mock(Context.class);
    }

    @Test
    public void testSignIn() {
        String email = "email";
        String password = "password";
        UserSessionInfo session = new UserSessionInfo();

        waitForInitialize();

        assertFalse(dataProvider.isSignedIn(context));
        assertFalse(dataProvider.isSignedUp(context));

        signIn(email, password, session);

        verify(bridgeService).signIn(isA(SignInBody.class));

        assertTrue(dataProvider.isSignedIn(context));
        assertTrue(dataProvider.isSignedUp(context));

        signOut(new BridgeMessageResponse());

        verify(bridgeService).signOut();

        assertFalse(dataProvider.isSignedIn(context));
    }

}