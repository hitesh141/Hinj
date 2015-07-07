package com.hinj.app.social;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;

/**************************************************************************************************
 * The calling class MUST call the following methods for this helper to work correctly:
 *
 * In the caller's onStart method you must invoke GooglePlusSignInHelper.handleOnStart
 * In the caller's onStop method you must invoke GooglePlusSignInHelper.handleOnStop
 * In the caller's onActivityResult method you must invoke GooglePlusSignInHelper.handleActivityResult
 * When the user presses a Google+ Sign in button you should call GooglePlusSignInHelper.signInWithGplus
 *
 * This class will then call finishGoogleLogin on the calling activity to complete the login
 * sequence. This means that the calling class must expose a method named finishGoogleLogin
 * with the appropriate parameters.
 *
 * TODO: In the future it would be nice to make this class more user friendly and reinforce
 * the above rules with interfaces.
 */
public class GooglePlusSignInHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, PlusClient.OnAccessRevokedListener {

    // We can store the connection result from a failed connect()
    // attempt in order to make the application feel a bit more
    // responsive for the user.
    private ConnectionResult mConnectionResult;

    private GoogleApiClient mGoogleApiClient;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress = false;
    private static final int RC_SIGN_IN = 0;
    private boolean mSignInClicked = false;
    private Activity activity;
    private GooglePlusSignInReceiver loginReceiver;

    public GooglePlusSignInHelper(GooglePlusSignInReceiver receiver, Activity activity) {
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        this.activity = activity;
        this.loginReceiver = receiver;
    }

    /**
     * Method to resolve any signin errors
     * */
    public void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                activity.startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.e("StormPins", "GooglePlusSignInHelper::resolveSignInError", e);
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress) {
            mConnectionResult = result;
            if (mSignInClicked) {
                resolveSignInError();
            }
        }
    }

    public void handleActivityResult(int requestCode, int responseCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != Activity.RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(mSignInClicked){
            mSignInClicked = false;
            getProfileInformation();
        }
    }

    /**
     * Fetching user's information name, email, profile pic
     * */
    public void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {

                Person currentPerson = Plus.PeopleApi .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();

                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                Log.e("StormPins", "Name: " + personName +  ", email: " + email  );

                // Once we have completed the sign in process disconnect from the Google+ API.
                signOutFromGplus();

                loginReceiver.finishGoogleLogin(currentPerson.getId(), email, personName);

            } else {
                Toast.makeText(activity.getApplicationContext(), "Person information is null", Toast.LENGTH_LONG).show();
              //  FlurryEventLogger.logEvent(FlurryEventLogger.FlurryEvent.INVALID_GOOGLE_LOGIN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sign-in into google
     * */
    public  void signInWithGplus() {

        // No matter what, the user clicked the sign in button here.
        // Set our user action to true.
        mSignInClicked = true;

        if (mGoogleApiClient.isConnected()) {
            getProfileInformation();
        }
        else {
            if (!mGoogleApiClient.isConnecting()) {
                resolveSignInError();
            }
        }
    }

    /**
     * Sign-out from google
     * */
    public void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }

    public void handleOnStart() {
        mGoogleApiClient.connect();
    }

    public void handleOnStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    /**
     * MKD: I'm not sure why we have to override this method. It was from the
     * original code and class implements list requires it.
     */
    @Override
    public void onDisconnected() {
        Log.d("StormPins", "GooglePlusSignInHelper::onDisconnected");
    }

    /**
     * MKD: I'm not sure why we have to override this method. It was form the
     * original code and class implements list requires it.
     */
    @Override
    public void onAccessRevoked(ConnectionResult arg0) {
        Log.d("StormPins", "GooglePlusInHelper::onAccessRevoked");
    }
}
