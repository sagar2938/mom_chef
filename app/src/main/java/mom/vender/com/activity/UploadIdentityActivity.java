package mom.vender.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import mom.vender.com.R;
import mom.vender.com.base.BaseActivity;
import mom.vender.com.network.volley.NetworkCallBacks.ResponseCallback;
import mom.vender.com.network.volley.WebserviceHelper;
import mom.vender.com.utils.Constants;
import mom.vender.com.utils.CustomProgressDialog;
import mom.vender.com.utils.Preferences;

public class UploadIdentityActivity extends BaseActivity {
    RelativeLayout idproof_rl,personal_rl,current_rl,declaration_rll,license_rl;
    ImageView address_iv,license_iv;
    Button address_rl;
    View address_view;
    ImageView declaration_iv;
    Button declaration_rl,license_bt;
    ImageView details_iv;
    Button details_rl;
    View details_view,license_view;
    CircleImageView profile_civ;
    ImageView proof_iv;
    Button proof_rl;
    View proof_view;
    TextView review_tv;

    /* renamed from: com.servesimplified.partner.profiledetails.UploadIdentityActivity$1 */
    class C04941 implements View.OnClickListener {
        C04941() {
        }

        public void onClick(View v) {
            Toast.makeText(UploadIdentityActivity.this.getApplicationContext(), "Comming Soon !", Toast.LENGTH_SHORT).show();
        }
    }

    /* renamed from: com.servesimplified.partner.profiledetails.UploadIdentityActivity$2 */
    class C04952 implements View.OnClickListener {
        C04952() {
        }

        public void onClick(View v) {
            UploadIdentityActivity.this.startActivity(new Intent(UploadIdentityActivity.this, UploadProofActivity.class));
            finish();

        }
    }

    /* renamed from: com.servesimplified.partner.profiledetails.UploadIdentityActivity$3 */
    class C04963 implements View.OnClickListener {
        C04963() {
        }

        public void onClick(View v) {
            UploadIdentityActivity.this.startActivity(new Intent(UploadIdentityActivity.this, PersonalDetailsActivity.class));
            finish();
        }
    }

    /* renamed from: com.servesimplified.partner.profiledetails.UploadIdentityActivity$4 */
    class C04974 implements View.OnClickListener {
        C04974() {
        }

        public void onClick(View v) {
            UploadIdentityActivity.this.startActivity(new Intent(UploadIdentityActivity.this, CurrentAddressActivity.class));
            finish();

        }
    }

    /* renamed from: com.servesimplified.partner.profiledetails.UploadIdentityActivity$5 */
    class C04985 implements View.OnClickListener {
        C04985() {
        }

        public void onClick(View v) {
            declaration_rl.setVisibility(View.GONE);

            UploadIdentityActivity.this.startActivity(new Intent(UploadIdentityActivity.this, DeclarationActivity.class));
            finish();
            UploadIdentityActivity.this.declaration_iv.setImageDrawable(UploadIdentityActivity.this.getResources().getDrawable(R.drawable.green_tick_icon));

        }
    }

    /* renamed from: com.servesimplified.partner.profiledetails.UploadIdentityActivity$6 */
    class C06966 implements ResponseCallback {
        C06966() {
        }

        public void OnSuccessFull(JSONObject Response) {
            if (Response != null) {
                CustomProgressDialog.getInstance(UploadIdentityActivity.this);
                try {
                    Log.d("ResponseUpload", String.valueOf(Response));
                    JSONObject jsonObject1 = Response.getJSONObject("Response");
                    int identity_prove = jsonObject1.getInt("identity_prove");
                    int current_address = jsonObject1.getInt("current_address");
                    int personal_details = jsonObject1.getInt("personal_details");
                    int declaration = jsonObject1.getInt("declaration");
                    int license_details = jsonObject1.getInt("license_details");


                    if(identity_prove == 1 ){
                        UploadIdentityActivity.this.proof_iv.setImageDrawable(UploadIdentityActivity.this.getResources().getDrawable(R.drawable.green_tick_icon));
                        UploadIdentityActivity.this.proof_view.setBackgroundColor(UploadIdentityActivity.this.getResources().getColor(R.color.green));
                        proof_rl.setVisibility(View.GONE);
                        details_rl.setVisibility(View.VISIBLE);
                        idproof_rl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent =  new Intent(UploadIdentityActivity.this,IdentityProofDetails.class);
                                intent.putExtra("FROM","identity");
                                startActivity(intent);

                            }
                        });
                    }
                    if(personal_details == 1){
                        UploadIdentityActivity.this.details_iv.setImageDrawable(UploadIdentityActivity.this.getResources().getDrawable(R.drawable.green_tick_icon));
                        UploadIdentityActivity.this.details_view.setBackgroundColor(UploadIdentityActivity.this.getResources().getColor(R.color.green));
                        details_rl.setVisibility(View.GONE);
                        license_bt.setVisibility(View.VISIBLE);
                        personal_rl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent =  new Intent(UploadIdentityActivity.this, PersonalDetails.class);
                                startActivity(intent);

                            }
                        });
                    }

//                    if(current_address == 1){
//                        UploadIdentityActivity.this.address_iv.setImageDrawable(UploadIdentityActivity.this.getResources().getDrawable(R.drawable.green_tick_icon));
//                        UploadIdentityActivity.this.address_view.setBackgroundColor(UploadIdentityActivity.this.getResources().getColor(R.color.green));
//                        address_rl.setVisibility(View.GONE);
//                        license_bt.setVisibility(View.VISIBLE);
//                        current_rl.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent =  new Intent(UploadIdentityActivity.this, CurrentAddress.class);
//                                startActivity(intent);
//
//                            }
//                        });
//                    }


                    if(license_details == 1){
                        UploadIdentityActivity.this.license_iv.setImageDrawable(UploadIdentityActivity.this.getResources().getDrawable(R.drawable.green_tick_icon));
                        UploadIdentityActivity.this.license_view.setBackgroundColor(UploadIdentityActivity.this.getResources().getColor(R.color.green));
                        license_bt.setVisibility(View.GONE);
                        declaration_rl.setVisibility(View.VISIBLE);
                        license_rl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent =  new Intent(UploadIdentityActivity.this,IdentityProofDetails.class);
                                intent.putExtra("FROM","license");
                                startActivity(intent);

                            }
                        });
                    }


                    if(declaration == 1){
                        declaration_rl.setVisibility(View.GONE);
                        review_tv.setText(getString(R.string.review_string));
                        UploadIdentityActivity.this.declaration_iv.setImageDrawable(UploadIdentityActivity.this.getResources().getDrawable(R.drawable.green_tick_icon));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("ResponseUploaded", String.valueOf(Response));
                return;
            }
            CustomProgressDialog.setDismiss();
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_identity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.logo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        this.proof_rl = findViewById(R.id.identity_bt);
        this.details_rl = findViewById(R.id.details_bt);
        this.address_rl = findViewById(R.id.address_bt);
        this.declaration_rl = findViewById(R.id.declaration_bt);
        this.license_bt = findViewById(R.id.license_bt);
        this.review_tv = findViewById(R.id.name_tv);
        this.details_rl.setVisibility(View.GONE);
        this.address_rl.setVisibility(View.GONE);
        this.declaration_rl.setVisibility(View.GONE);
        this.license_bt.setVisibility(View.GONE);
        this.proof_view = findViewById(R.id.proof_view);
        this.details_view = findViewById(R.id.personal_view);
        this.address_view = findViewById(R.id.address_view);
        this.license_view = findViewById(R.id.license_view);
        this.proof_iv = findViewById(R.id.identity_iv);
        this.details_iv = findViewById(R.id.personal_iv);
        this.address_iv = findViewById(R.id.address_iv);
        this.declaration_iv = findViewById(R.id.declaration_iv);
        this.license_iv = findViewById(R.id.license_iv);
        this.proof_rl.setOnClickListener(new C04952());
        this.details_rl.setOnClickListener(new C04963());
        this.address_rl.setOnClickListener(new C04974());
        this.declaration_rl.setOnClickListener(new C04985());


        this.license_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadIdentityActivity.this,LicenseUploadActivity.class);
                startActivity(intent);

            }
        });

        idproof_rl = findViewById(R.id.myrating_rl);
        personal_rl = findViewById(R.id.idenetity_rl);
        current_rl = findViewById(R.id.award_rl);
        declaration_rll = findViewById(R.id.declaration_rl);
        license_rl = findViewById(R.id.mylicense_rl);
        fetchUploadStatus();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return false;
        }
        onBackPressed();
        return true;
    }

    @Override
    public void onResume() {

        fetchUploadStatus();
        super.onResume();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private void fetchUploadStatus() {
        CustomProgressDialog.getInstance(this);
        String url = Constants.BASE_URL + "api/get/user/status/";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", Preferences.getInstance(this).getMobileNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebserviceHelper.getInstance().PostCall(getApplicationContext(), url, jsonObject, new C06966());
    }
}
