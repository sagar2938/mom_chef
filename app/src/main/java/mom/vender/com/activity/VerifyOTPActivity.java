package mom.vender.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.goodiebag.pinview.Pinview;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import mom.vender.com.R;
import mom.vender.com.base.BaseActivity;
import mom.vender.com.network.ApiCallService;
import mom.vender.com.network.response.SignUpResponse;
import mom.vender.com.network.volley.NetworkCallBacks.ResponseCallback;
import mom.vender.com.network.volley.WebserviceHelper;
import mom.vender.com.utils.AppUser;
import mom.vender.com.utils.Constants;
import mom.vender.com.utils.LocalRepositories;
import mom.vender.com.utils.Preferences;

public class VerifyOTPActivity extends BaseActivity {
    Pinview pinView;
    Button submit;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);
        pinView = findViewById(R.id.pinView);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinView.getValue().length() == 0) {
                    getDialog("Enter Otp");
                    return;
                }
                if (pinView.getValue().length() != 4) {
                    getDialog("Enter complete Otp");
                    return;
                }
                if (!pinView.getValue().equals(Preferences.getInstance(VerifyOTPActivity.this).getOtp())) {
                    getDialog("Otp does not match");
                    return;
                } else {

                    if (pinView.getValue() != null) {
                        if (pinView.getValue().equalsIgnoreCase(Preferences.getInstance(VerifyOTPActivity.this).getOtp())) {
                            storeFcmToken(FirebaseInstanceId.getInstance().getToken());
                            if (getIntent().getIntExtra("From", -1) == 0) {
                                if (Preferences.getInstance(getApplicationContext()).getProfileStatus().equals("0")){
//                                    Preferences.getInstance(VerifyOTPActivity.this).setLogin(true);
                                    Intent intent = new Intent(VerifyOTPActivity.this, ContactDetailActivity.class);
                                    Bundle extra = new Bundle();
                                    extra.putInt("FROM", new Integer(1));
                                    intent.putExtras(extra);
                                    startActivity(intent);
                                    finish();
//                                    Toast.makeText(VerifyOTPActivity.this, "Verified", Toast.LENGTH_SHORT).show();
                                }else {
                                    Preferences.getInstance(VerifyOTPActivity.this).setLogin(true);
                                    Intent intent = new Intent(VerifyOTPActivity.this, MainActivity.class);
                                    Bundle extra = new Bundle();
                                    extra.putInt("FROM", new Integer(1));
                                    intent.putExtras(extra);
                                    startActivity(intent);
                                    finish();
//                                    Toast.makeText(VerifyOTPActivity.this, "Verified", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                ApiCallService.action(VerifyOTPActivity.this, LocalRepositories.getAppUser(getApplicationContext()).getRequestSignUp(),ApiCallService.Action.ACTION_SIGN_UP);

                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter correct OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    @Subscribe
    public void signUp(SignUpResponse response){
        if (response.getResponse().getConfirmation()==1){
            AppUser appUser= LocalRepositories.getAppUser(this);
            appUser.setSignUpResponse(response);
//            Preferences.getInstance(VerifyOTPActivity.this).setLogin(true);
            LocalRepositories.saveAppUser(getApplicationContext(),appUser);
            Intent intent = new Intent(VerifyOTPActivity.this, ContactDetailActivity.class);
            Bundle extra = new Bundle();
            extra.putInt("FROM", new Integer(1));
            intent.putExtras(extra);
            startActivity(intent);
            finish();
        }else {
            getDialog(response.getResponse().getMessage());
        }
    }

    private void storeFcmToken(String fcm) {
        String url = Constants.BASE_URL + "api/partner/addtoken/";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", Preferences.getInstance(VerifyOTPActivity.this).getMobileNumber());
            jsonObject.put("fcmToken", fcm);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebserviceHelper.getInstance().PostCall(getApplicationContext(), url, jsonObject, new ResponseCallback() {
            @Override
            public void OnSuccessFull(JSONObject Response) {
                Log.d("ResponseFCMAdd", String.valueOf(Response));

            }
        });
    }




}
