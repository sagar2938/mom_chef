package mom.vender.com.utils;


import java.util.ArrayList;
import java.util.List;

import mom.vender.com.network.request.RequestSignUp;
import mom.vender.com.network.response.SignUpResponse;

public class AppUser {

    static AppUser appUser;
    public static AppUser getInstance(){
        if (AppUser.appUser==null){
            appUser=new AppUser();
        }
        return appUser;
    }

    String mobile;
    RequestSignUp requestSignUp;


    public RequestSignUp getRequestSignUp() {
        return requestSignUp;
    }

    public void setRequestSignUp(RequestSignUp requestSignUp) {
        this.requestSignUp = requestSignUp;
    }

    public static AppUser getAppUser() {
        return appUser;
    }

    public static void setAppUser(AppUser appUser) {
        AppUser.appUser = appUser;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    SignUpResponse signUpResponse;

    public SignUpResponse getSignUpResponse() {
        return signUpResponse;
    }

    public void setSignUpResponse(SignUpResponse signUpResponse) {
        this.signUpResponse = signUpResponse;
    }
}
