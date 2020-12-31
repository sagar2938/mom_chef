package mom.vender.com.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import mom.vender.com.utils.CustomProgressDialog;
import mom.vender.com.utils.Preferences;

public class DeclarationActivity  extends BaseActivity {
    SharedPreferences.Editor editor;
    //    TextView getHelp_tv;
    Button terms_bt ;
    TextView terms_tv ;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declaration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.logo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        terms_bt = findViewById(R.id.terms_bt);
        terms_tv = findViewById(R.id.term_tv);
        terms_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadProfile(terms_tv.getText().toString());

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

    private void uploadProfile( String text) {
        CustomProgressDialog.getInstance(this);
        String url = Constants.BASE_URL + "api/add/user/declaration/";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", Preferences.getInstance(this).getMobileNumber());
            jsonObject.put("declaration", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebserviceHelper.getInstance().PostCall(getApplicationContext(), url, jsonObject, new ResponseCallback() {
            public void OnSuccessFull(JSONObject Response) {
                Log.d("Declare Response", String.valueOf(Response));
                if (Response != null) {
                    CustomProgressDialog.setDismiss();
                    startActivity(new Intent(DeclarationActivity.this, UploadIdentityActivity.class));
                    finish();
                }else {
                    CustomProgressDialog.setDismiss();
                }
            }
        });
    }

}
