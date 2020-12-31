package mom.vender.com.activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import mom.vender.com.R;
import mom.vender.com.base.BaseActivity;
import mom.vender.com.network.volley.NetworkCallBacks.ResponseCallback;
import mom.vender.com.network.volley.WebserviceHelper;
import mom.vender.com.utils.Constants;
import mom.vender.com.utils.CustomUploadProgressDialog;
import mom.vender.com.utils.Preferences;

public class PersonalDetails extends BaseActivity {

    TextView father_name_tv,user_dob_tv,gender_name_tv,house_name_tv,locality_name_tv,city_name_tv,pincode_name_tv,state_name_tv;
    ImageView idProof_iv ;
    Button change_detail_bt ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proof_personal_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.logo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        father_name_tv = findViewById(R.id.father_name_tv);
        user_dob_tv = findViewById(R.id.user_dob_tv);
        locality_name_tv = findViewById(R.id.locality_name_tv);
        gender_name_tv = findViewById(R.id.gender_name_tv);
        house_name_tv = findViewById(R.id.house_name_tv);
        locality_name_tv = findViewById(R.id.locality_name_tv);
        city_name_tv = findViewById(R.id.city_name_tv);
        pincode_name_tv = findViewById(R.id.pincode_name_tv);
        state_name_tv = findViewById(R.id.state_name_tv);
        change_detail_bt = findViewById(R.id.change_details_bt);
        int statusForApproval = Integer.parseInt(Preferences.getInstance(this).getProfileStatus());

        if(statusForApproval == 1 || statusForApproval == 2){
            change_detail_bt.setVisibility(View.GONE);
        }else {
            change_detail_bt.setVisibility(View.VISIBLE);
        }
        fetchIdentityProofDetails();
        change_detail_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(PersonalDetails.this,PersonalDetailsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return false;
        }
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private void fetchIdentityProofDetails() {
        CustomUploadProgressDialog.getInstance(this);
        String url = Constants.BASE_URL + "api/get/user/information/";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", Preferences.getInstance(PersonalDetails.this).getMobileNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebserviceHelper.getInstance().PostCall(getApplicationContext(), url, jsonObject, new ResponseCallback() {
            @Override
            public void OnSuccessFull(JSONObject Response) {

                if(Response!=null) {
                    try {
                        CustomUploadProgressDialog.setDismiss();
                        JSONObject jsonObject1 = Response.getJSONObject("Response");
                        house_name_tv.setText(jsonObject1.getString("houseNo"));
                        locality_name_tv.setText(jsonObject1.getString("locality"));
                        city_name_tv.setText(jsonObject1.getString("city"));
                        pincode_name_tv.setText(jsonObject1.getString("pincode"));
                        state_name_tv.setText(jsonObject1.getString("state"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    CustomUploadProgressDialog.setDismiss();
                }

            }
        });
    }
}
