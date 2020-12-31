package mom.vender.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mom.vender.com.R;
import mom.vender.com.base.BaseActivity;
import mom.vender.com.network.volley.NetworkCallBacks.ResponseCallback;
import mom.vender.com.network.volley.WebserviceHelper;
import mom.vender.com.utils.Constants;
import mom.vender.com.utils.CustomProgressDialog;
import mom.vender.com.utils.Preferences;

public class IdentityProofDetails extends BaseActivity {

    TextView identityType_tv, identity_idNumber_tv, nameInIdentitiy, ifsc_tv, ifscCode_tv, nameinid_tv, gender_tv, gend_tv;
    ImageView idProof_iv;
    Button change_detail_bt;
    String imgURL, idNumber;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proof_identity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.logo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        identityType_tv = findViewById(R.id.idtype_tv);
        identity_idNumber_tv = findViewById(R.id.idtypenum_tv);
        nameInIdentitiy = findViewById(R.id.useridname_tv);
        ifsc_tv = findViewById(R.id.ifsc_tv);
        ifscCode_tv = findViewById(R.id.ifsc_code_tv);
        idProof_iv = findViewById(R.id.frontid_iv);
        nameinid_tv = findViewById(R.id.nameinid_tv);
        gend_tv = findViewById(R.id.gend_tv);
        gender_tv = findViewById(R.id.gender_tv);
        change_detail_bt = findViewById(R.id.change_details_bt);

        int statusForApproval = Integer.parseInt(Preferences.getInstance(this).getProfileStatus());

        if (statusForApproval == 1 || statusForApproval == 2) {
            change_detail_bt.setVisibility(View.GONE);
        } else {
            change_detail_bt.setVisibility(View.VISIBLE);
        }


        if (getIntent().getStringExtra("FROM").equalsIgnoreCase("identity")) {
            ifscCode_tv.setVisibility(View.VISIBLE);
            ifsc_tv.setVisibility(View.VISIBLE);
            gend_tv.setVisibility(View.VISIBLE);
            gender_tv.setVisibility(View.VISIBLE);
            fetchIdentityProofDetails();
            change_detail_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(IdentityProofDetails.this, UploadProofActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } else if (getIntent().getStringExtra("FROM").equalsIgnoreCase("bank")) {
            ifscCode_tv.setVisibility(View.VISIBLE);
            ifsc_tv.setVisibility(View.VISIBLE);
            setTitle("Bank Details");

            nameinid_tv.setText("Bank Name");
            fetchIdentityBankDetails();
            change_detail_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(IdentityProofDetails.this, BankDetailsActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
//        } else if (getIntent().getStringExtra("FROM").equalsIgnoreCase("license")) {
//            ifscCode_tv.setVisibility(View.GONE);
//            setTitle("Driving License");
//            ifsc_tv.setVisibility(View.GONE);
//            fetchLicenseDetails();
//            change_detail_bt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(IdentityProofDetails.this, LicenseUploadActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            });
//        }
        }

        idProof_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IdentityProofDetails.this, ShowDocumentActivity.class);
                intent.putExtra("ImgUrl", imgURL);
                intent.putExtra("idNumber", idNumber);
                startActivity(intent);
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
        CustomProgressDialog.getInstance(this);
        String url = Constants.BASE_URL + "api/get/user/document/";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", Preferences.getInstance(this).getMobileNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebserviceHelper.getInstance().PostCall(getApplicationContext(), url, jsonObject, new ResponseCallback() {
            @Override
            public void OnSuccessFull(JSONObject Response) {
                if (Response != null) {
                   CustomProgressDialog.setDismiss();

                    try {
                        Log.d("ProofResponse", String.valueOf(Response));
                        JSONArray jsonObject1 = Response.getJSONArray("image_data");
                        JSONObject jsonObject2 = jsonObject1.getJSONObject(0);
                        identityType_tv.setText(jsonObject2.getString("idType"));
                        identity_idNumber_tv.setText(jsonObject2.getString("idNumber"));
                        idNumber = jsonObject2.getString("idNumber");
                        nameInIdentitiy.setText(jsonObject2.getString("name"));
                        ifscCode_tv.setText(jsonObject2.getString("dob"));
                        ifsc_tv.setText("D.O.B");
                        gender_tv.setText(jsonObject2.getString("gender"));
                        imgURL = jsonObject2.getString("image_path") + jsonObject2.getString("front_image");
                        Log.d("ImageUrlCheck", imgURL);
                        if (!imgURL.isEmpty()) {
                            Picasso.get().load(imgURL)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                    .error(R.drawable.dotted_boundry)
                                    .into(idProof_iv);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    CustomProgressDialog.setDismiss();
                }

            }
        });
    }



    private void fetchIdentityBankDetails() {
        CustomProgressDialog.getInstance(this);
        String url = Constants.BASE_URL + "api/get/bank/details/";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", Preferences.getInstance(this).getMobileNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebserviceHelper.getInstance().PostCall(getApplicationContext(), url, jsonObject, new ResponseCallback() {
            @Override
            public void OnSuccessFull(JSONObject Response) {
                if (Response != null) {
                   CustomProgressDialog.setDismiss();

                    try {
                        Log.d("ProofResponse", String.valueOf(Response));
                        JSONObject jsonObject2 = Response.getJSONObject("Response");
                        identityType_tv.setText("Account No.");
                        idNumber = jsonObject2.getString("account_no");
                        identity_idNumber_tv.setText(jsonObject2.getString("account_no"));
                        nameInIdentitiy.setText(jsonObject2.getString("bank_name"));
                        ifscCode_tv.setText(jsonObject2.getString("ifsc"));
                        imgURL = jsonObject2.getString("image_path") + jsonObject2.getString("image_name");
                        Log.d("ImageUrlCheck", imgURL);

                        if (!imgURL.isEmpty()) {
                            Picasso.get().load(imgURL)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                    .error(R.drawable.dotted_boundry)
                                    .into(idProof_iv);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                   CustomProgressDialog.setDismiss();
                }

            }
        });
    }

}
