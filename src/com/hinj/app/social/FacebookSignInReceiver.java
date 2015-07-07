package com.hinj.app.social;

public interface FacebookSignInReceiver {

    public void finishFacebookLogin(String FB_Id, String FB_EmailId, String fbName, String first_name, String last_name, String stateName);
}
