package org.sagebionetworks.bridge.researchstack;

import android.content.Context;

import com.google.common.collect.Lists;

import org.researchstack.backbone.ResourcePathManager;
import org.researchstack.backbone.storage.file.EncryptionProvider;
import org.researchstack.backbone.storage.file.FileAccess;
import org.researchstack.backbone.storage.file.PinCodeConfig;
import org.researchstack.skin.AppPrefs;
import org.researchstack.skin.DataProvider;
import org.researchstack.skin.DataResponse;
import org.sagebionetworks.bridge.researchstack.wrapper.StorageAccessWrapper;
import org.sagebionetworks.bridge.sdk.rest.BridgeService;
import org.sagebionetworks.bridge.sdk.rest.UserSessionInfo;
import org.sagebionetworks.bridge.sdk.rest.model.BridgeMessageResponse;
import org.sagebionetworks.bridge.sdk.rest.model.SignInBody;

import retrofit2.Response;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;

/**
 * Created by liujoshua on 9/19/16.
 */
public class BridgeDataProviderTestBase {

    protected ResourcePathManager.Resource publicKeyRes;
    protected ResourcePathManager.Resource tasksAndSchedulesRes;
    protected AppPrefs appPrefs;
    protected DataProvider dataProvider;
    protected BridgeService bridgeService;
    protected StorageAccessWrapper storageAccess;
    protected Context context;
    protected PinCodeConfig pinCodeConfig;
    protected EncryptionProvider encryptionProvider;
    protected FileAccess fileAccess;
    protected BridgeEncryptedDatabase appDatabase;
    protected ConsentLocalStorage consentLocalStorage;
    protected UserLocalStorage userLocalStorage;
    protected BridgeDependencyLoader bridgeDependencyLoader;

    protected  TestSubscriber<DataResponse> signOut(BridgeMessageResponse response) {
        Observable<Response> bridgeResponse = Observable.just(Response.success(null));
        when(bridgeService.signOut()).thenReturn(bridgeResponse);

        Observable<DataResponse> dataResponseObservable = dataProvider.signOut(context);

        TestSubscriber<DataResponse> testSubscriber = new TestSubscriber<>();
        dataResponseObservable.subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();

        return testSubscriber;
    }

    protected TestSubscriber<DataResponse> signIn(String email, String password, UserSessionInfo session) {
        Observable<Response<UserSessionInfo>> bridgeResponse = Observable.just(Response.success(session));
        when(bridgeService.signIn(isA(SignInBody.class))).thenReturn(bridgeResponse);
        when(appDatabase.loadUploadRequests()).thenReturn(Lists.newArrayList());

        Observable<DataResponse> dataResponseObservable = dataProvider.signIn(context, email, password);
        TestSubscriber<DataResponse> testSubscriber = new TestSubscriber<>();
        dataResponseObservable.subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();

        return testSubscriber;
    }


    protected void waitForInitialize() {
        TestSubscriber<DataResponse> testSubscriber = new TestSubscriber<>();

        dataProvider.initialize(context).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
    }

    protected void assertExactlyOneSuccessfulEvent(TestSubscriber<DataResponse> testSubscriber) {
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        testSubscriber.assertUnsubscribed();
        testSubscriber.assertValueCount(1);
    }
}
