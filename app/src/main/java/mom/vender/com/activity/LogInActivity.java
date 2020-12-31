package mom.vender.com.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mom.vender.com.R;
import mom.vender.com.base.BaseActivity;
import mom.vender.com.network.volley.NetworkCallBacks.ResponseCallback;
import mom.vender.com.network.volley.WebserviceHelper;
import mom.vender.com.utils.Constants;
import mom.vender.com.utils.CustomProgressDialog;
import mom.vender.com.utils.Helper;
import mom.vender.com.utils.Preferences;


public class LogInActivity extends BaseActivity {

    EditText mobile;
    TextView signup;
    String userName;
    Button submit;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);
        String token = FirebaseInstanceId.getInstance().getToken();
        System.out.println("FCMtoken "+token);
        init();
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

    public void init() {
        this.submit = findViewById(R.id.submit);
        this.mobile = findViewById(R.id.mobile);
        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mobile.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter mobile number", Toast.LENGTH_SHORT).show();
                } else {
                    loginApiFetch(mobile.getText().toString());
                }


            }
        });

    }

    private void loginApiFetch(final String mobile) {
        CustomProgressDialog.getInstance(this).show();
        String url = Constants.BASE_URL + "api/vendor/login/";
        final String val = Helper.getOtp();
        Log.d("GeneratedOTP", val);
//        Toast.makeText(this, ""+val, Toast.LENGTH_SHORT).show();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", mobile);
            jsonObject.put("otp", val);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebserviceHelper.getInstance().PostCall(getApplicationContext(), url, jsonObject, new ResponseCallback() {
            public void OnSuccessFull(JSONObject Response) {
                System.out.println("http request : "+jsonObject);
                System.out.println("http : "+url);
                System.out.println("http response : "+Response);
                if (Response != null) {
                    CustomProgressDialog.setDismiss();
                    try {
                        JSONObject jsonObject1 = Response.getJSONObject("response");
                        Log.d("Response", String.valueOf(Response));
                        if (jsonObject1.getInt("confirmation") == 0) {
                            Toast.makeText(getApplicationContext(), "User not Exist ! Sign Up", Toast.LENGTH_SHORT).show();
                        } else {


//                            Toast.makeText(LogInActivity.this, ""+val, Toast.LENGTH_SHORT).show();
                            Preferences.getInstance(getApplicationContext()).setFirstName(jsonObject1.getJSONArray("user_data").getJSONObject(0).getString("firstName"));
                            Preferences.getInstance(getApplicationContext()).setMiddleName(jsonObject1.getJSONArray("user_data").getJSONObject(0).getString("middleName"));
                            Preferences.getInstance(getApplicationContext()).setLastName(jsonObject1.getJSONArray("user_data").getJSONObject(0).getString("lastName"));
                            Preferences.getInstance(getApplicationContext()).setEmail(jsonObject1.getJSONArray("user_data").getJSONObject(0).getString("email"));
                            Preferences.getInstance(getApplicationContext()).setImageName(jsonObject1.getJSONArray("user_data").getJSONObject(0).getString("image_name"));
                            Preferences.getInstance(getApplicationContext()).setProfileImage(jsonObject1.getJSONArray("user_data").getJSONObject(0).getString("image_name"));
                            Preferences.getInstance(getApplicationContext()).setProfileStatus(jsonObject1.getJSONArray("user_data").getJSONObject(0).getString("status"));
                            if (!jsonObject1.getJSONArray("user_data").getJSONObject(0).getString("status").equals("1")){
                                getDialog("Your profile is yet to be verified by admin please contact administration");
                            }else {
                                Preferences.getInstance(LogInActivity.this).setOtp(val);
                                Preferences.getInstance(LogInActivity.this).setMobileNumber(mobile);
                                Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);
                                intent.putExtra("From", 0);
                                startActivity(intent);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    CustomProgressDialog.setDismiss();
                    Toast.makeText(getApplicationContext(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
