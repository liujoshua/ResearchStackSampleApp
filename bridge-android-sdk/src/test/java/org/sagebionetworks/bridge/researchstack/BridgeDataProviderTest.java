package org.sagebionetworks.bridge.researchstack;

import android.content.Context;

import com.google.common.collect.Lists;

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
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by liujoshua on 9/12/16.
 */
public class BridgeDataProviderTest extends BridgeDataProviderTestBase {

    @Before
    public void beforeTest() {
        publicKeyRes = mock(ResourcePathManager.Resource.class);
        tasksAndSchedulesRes = mock(ResourcePathManager.Resource.class);
        bridgeService = mock(BridgeService.class);
        appPrefs = mock(AppPrefs.class);


        pinCodeConfig = mock(PinCodeConfig.class);
        encryptionProvider = mock(EncryptionProvider.class);
        fileAccess = mock(FileAccess.class);
        appDatabase = mock(BridgeEncryptedDatabase.class);

        storageAccess = mock(StorageAccessWrapper.class);
        when(storageAccess.getPinCodeConfig()).thenReturn(pinCodeConfig);
        when(storageAccess.getAppDatabase()).thenReturn(appDatabase);
        when(storageAccess.getFileAccess()).thenReturn(fileAccess);

        consentLocalStorage = mock(ConsentLocalStorage.class);
        userLocalStorage = mock(UserLocalStorage.class);

        bridgeDependencyLoader = mock(BridgeDependencyLoader.class);
        when(bridgeDependencyLoader.getAppPrefs()).thenReturn(appPrefs);
        when(bridgeDependencyLoader.getConsentLocalStorage()).thenReturn(consentLocalStorage);
        when(bridgeDependencyLoader.getStorageAccess()).thenReturn(storageAccess);
        when(bridgeDependencyLoader.getUserLocalStorage()).thenReturn(userLocalStorage);

        dataProvider = new TestBridgeDataProvider(publicKeyRes, tasksAndSchedulesRes, bridgeDependencyLoader, bridgeService);
        context = mock(Context.class);
    }

    @Test
    public void testInitialize() {
        Observable<DataResponse> dataResponseObservable = dataProvider.initialize(context);

        TestSubscriber<DataResponse> testSubscriber = new TestSubscriber<>();
        dataResponseObservable.subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();

        assertExactlyOneSuccessfulEvent(testSubscriber);
    }

    @Test
    public void testSignUp() {
        when(bridgeService.signUp(isA(SignUpBody.class))).thenReturn(Observable.just(new BridgeMessageResponse()));

        Observable<DataResponse> dataResponseObservable = dataProvider.signUp(context, "email", "username", "password");

        TestSubscriber<DataResponse> testSubscriber = new TestSubscriber<>();
        dataResponseObservable.subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();

        verify(bridgeService).signUp(isA(SignUpBody.class));

        assertExactlyOneSuccessfulEvent(testSubscriber);
    }

    @Test
    public void testSignIn() {
        UserSessionInfo session = new UserSessionInfo();

        Observable<Response<UserSessionInfo>> bridgeResponse = Observable.just(Response.success(session));
        when(bridgeService.signIn(isA(SignInBody.class))).thenReturn(bridgeResponse);
        when(appDatabase.loadUploadRequests()).thenReturn(Lists.newArrayList());

        TestSubscriber<DataResponse> testSubscriber = signIn("email", "password", session);

        verify(bridgeService).signIn(isA(SignInBody.class));
        verify(userLocalStorage).saveUserSession(eq(session));
        verify(appDatabase).loadUploadRequests();

        assertExactlyOneSuccessfulEvent(testSubscriber);
    }

    @Test
    public void testSignOut() {
        BridgeMessageResponse bridgeResponse = new BridgeMessageResponse();

        TestSubscriber<DataResponse> testSubscriber = signOut(bridgeResponse);

        verify(bridgeService).signOut();

        assertExactlyOneSuccessfulEvent(testSubscriber);
    }

    @Test
    public void testResendEmailVerification() {
        Observable bridgeResponse = Observable.just(new BridgeMessageResponse());

        when(bridgeService.resendEmailVerification(isA(EmailBody.class))).thenReturn(bridgeResponse);

        Observable<DataResponse> dataResponseObservable = dataProvider.resendEmailVerification(context, "email");

        TestSubscriber<DataResponse> testSubscriber = new TestSubscriber<>();
        dataResponseObservable.subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();

        verify(bridgeService).resendEmailVerification(isA(EmailBody.class));

        assertExactlyOneSuccessfulEvent(testSubscriber);
    }

    @Test
    public void testIsSignedUp() {
        waitForInitialize();
        boolean isSignedUp = dataProvider.isSignedUp(context);
        verify(userLocalStorage).loadUser(context);

        assertFalse(isSignedUp);
    }

    @Test
    public void testIsSignedIn() {
        waitForInitialize();

        boolean isSignedIn = dataProvider.isSignedIn(context);
        verify(userLocalStorage).loadUserSession(context);

        assertFalse(isSignedIn);
    }

    @Test
    public void testIsConsented() {
        waitForInitialize();

        when(userLocalStorage.loadUserSession(context)).thenReturn(mock(UserSessionInfo.class));
        boolean isConsented = dataProvider.isConsented(context);
        verify(userLocalStorage).loadUserSession(context);
        verify(consentLocalStorage).hasConsent();

        assertFalse(isConsented);
    }

    @Ignore
    @Test
    public void testWithdrawConsent() {
        String reasonString = "reason";
        Observable<DataResponse> dataResponseObservable = dataProvider.withdrawConsent(context, reasonString);
    }

    @Ignore
    @Test
    public void testUploadConsent() {
        TaskResult consentResult = mock(TaskResult.class);
        dataProvider.uploadConsent(context, consentResult);
    }

    @Ignore
    @Test
    public void testSaveConsent() {
        TaskResult consentResult = mock(TaskResult.class);
        dataProvider.saveConsent(context, consentResult);
    }

    @Ignore
    @Test
    public void testGetUser() {
        User user = dataProvider.getUser(context);
    }

    @Ignore
    @Test
    public void testGetUserSharingScope() {
        String scope = dataProvider.getUserSharingScope(context);
    }

    @Ignore
    @Test
    public void testGetUserEmail() {
        String email = dataProvider.getUserEmail(context);
    }

    @Ignore
    @Test
    public void testUploadTaskResult() {
        TaskResult taskResult = mock(TaskResult.class);
        dataProvider.uploadTaskResult(context, taskResult);
    }

    @Ignore
    @Test
    public void testLoadTasksAndSchedules() {
        SchedulesAndTasksModel schedulesAndTasksModel = dataProvider.loadTasksAndSchedules(context);
    }

    @Ignore
    @Test
    public void testLoadTask() {
        SchedulesAndTasksModel.TaskScheduleModel taskScheduleModel = mock(SchedulesAndTasksModel.TaskScheduleModel.class);
        Task task = dataProvider.loadTask(context, taskScheduleModel);
    }

    @Ignore
    @Test
    public void testProcessInitialTaskResult() {
        TaskResult taskResult = mock(TaskResult.class);
        dataProvider.processInitialTaskResult(context, taskResult);
    }

    @Ignore
    @Test
    public void testForgotPassword() {
        Observable<DataResponse> dataResponseObservable = dataProvider.forgotPassword(context, "email");
    }
}