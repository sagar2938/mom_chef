package mom.vender.com.activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class CurrentAddress extends BaseActivity {

    TextView house_name_tv,locality_name_tv,city_name_tv,pincode_name_tv,state_name_tv;
    Button change_detail_bt ;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.logo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        locality_name_tv = findViewById(R.id.locality_name_tv);
        house_name_tv = findViewById(R.id.house_name_tv);
        locality_name_tv = findViewById(R.id.locality_name_tv);
        city_name_tv = findViewById(R.id.city_name_tv);
        pincode_name_tv = findViewById(R.id.pincode_name_tv);
        state_name_tv = findViewById(R.id.state_name_tv);
        change_detail_bt = findViewById(R.id.change_details_bt);
        fetchIdentityProofDetails();
        change_detail_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(CurrentAddress.this,CurrentAddressActivity.class);
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
        String url = Constants.BASE_URL + "api/get/user/details/";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", Preferences.getInstance(CurrentAddress.this).getMobileNumber());
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
