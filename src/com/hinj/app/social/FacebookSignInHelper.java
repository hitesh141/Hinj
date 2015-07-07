package com.hinj.app.social;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.hinj.app.utils.AppUtils;

public class FacebookSignInHelper {

    private static final List<String> PERMISSIONS = Arrays.asList("email");
    private boolean isLoginWithFBClicked = false;

    private Activity activity;

    // Both static variable use for FB
    public static String FB_Id, FB_EmailId;// Both static variable use for FB

    private String fbName;
    private String first_name, last_name;
    private String facebookID, facebookEmail;

    private FacebookSignInReceiver loginReceiver;

    public FacebookSignInHelper(FacebookSignInReceiver loginReceiver, Activity activity) {
        this.activity = activity;
        this.loginReceiver = loginReceiver;
    }

    public void startLogin() {
        if (!AppUtils.isNetworkAvailable(activity)) {
        	AppUtils.showDialog(activity, "No internet connection available");
        } else {
            isLoginWithFBClicked = true;

            Session session = Session.getActiveSession();

            if (!session.isOpened() && !session.isClosed()) {

                session.openForRead(new Session.OpenRequest(activity).setPermissions(PERMISSIONS).setCallback(callback));

            } else {

                Session.openActiveSession(activity, true, callback);
            }
        }
    }
    
    /**
     * Logout From Facebook 
     */
    public static void callFacebookLogout(Context context) {
        Session session = Session.getActiveSession();
        if (session != null) {

            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
                //clear your preferences if saved
            }
        } else {

            session = new Session(context);
            Session.setActiveSession(session);

            session.closeAndClearTokenInformation();
                //clear your preferences if saved
        }
    }

    
    /**
     * Method for set the permission in facebook
     *
     * @return
     */
    private boolean hasEmailPermission() {

        Session session = Session.getActiveSession();

        return session != null && session.getPermissions().contains("email");

    }

    // Method for face book login
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, final SessionState state, Exception exception) {

            if (session.isOpened()) {

                if (hasEmailPermission()) {

                    Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            if (user != null) {
                                try {

                                    String registrantEmailSocial = response.getGraphObject().getInnerJSONObject().getString("email").toString();

                                    Log.d("registrantEmailSocial", registrantEmailSocial);

                                    if (isLoginWithFBClicked == true) {

                                        FB_EmailId = registrantEmailSocial;
                                        facebookID = user.getId();

                                        fbName = user.getName();
                                        first_name = user.getFirstName();
                                        last_name = user.getLastName();

                                        String stateName = "";

                                        System.out.println("FB_Id FB_EmailId " + FB_Id + "//" + FB_EmailId);

                                        try {

                                           /* StormPinPreferencesUtil.saveUserId(activity, "");
                                            StormPinPreferencesUtil.saveUserName(activity, "");
                                            StormPinPreferencesUtil.savePassword(activity, "");*/

                                            if (!AppUtils.isNetworkAvailable(activity)) {
                                            	AppUtils.showDialog(activity, "Internet Connection Not Available");
                                            } else {
                                                loginReceiver.finishFacebookLogin(facebookID, FB_EmailId, fbName, first_name, last_name, stateName);
                                            }
                                        } catch (Exception e) {
                                        }

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    AppUtils.showDialog(activity, "Facebook error.");
                                }
                            }
                        }
                    });
                } else if (!hasEmailPermission()) {
                    session.requestNewPublishPermissions(new Session.NewPermissionsRequest(activity, PERMISSIONS));

                    Request request = Request.newStatusUpdateRequest(session, "StormPins", new Request.Callback() {
                        @Override
                        public void onCompleted(Response response) {
                            Log.d("", "fb:done = " + response.getGraphObject() + "," + response.getError());
                        }
                    });
                    request.executeAsync();
                }

            }

        }
    };
}
