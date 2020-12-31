package mom.vender.com.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import de.hdodenhof.circleimageview.CircleImageView;
import mom.vender.com.R;
import mom.vender.com.base.BaseActivity;
import mom.vender.com.network.volley.NetworkCallBacks.ResponseCallback;
import mom.vender.com.network.volley.WebserviceHelper;
import mom.vender.com.utils.Constants;
import mom.vender.com.utils.CustomProgressDialog;
import mom.vender.com.utils.Preferences;

public class ProfileActivity extends BaseActivity {
    ImageView award_iv;
    RelativeLayout award_rl;
    boolean connected = false;
    ImageView declaration_iv;
    TextView getHelp_tv, fewStepAway_tv, nameTextView;
    ImageView identity_iv, bank_iv;
    RelativeLayout identity_rl, bankdetails_rl;
    CircleImageView profile_civ;
    ImageView rating_iv, verify_tick_iv;
    RelativeLayout rating_rl, signOut_rl;
    LinearLayout verifcation_status_ll;
    CardView submit_approval_cv;
    int ratingStatus, identityStatus, awardStatus, bankStutus;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.logo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        this.profile_civ = findViewById(R.id.profileimage_iv);
        this.rating_rl = findViewById(R.id.myrating_rl);
        this.identity_rl = findViewById(R.id.idenetity_rl);
        this.award_rl = findViewById(R.id.award_rl);
        bankdetails_rl = findViewById(R.id.bank_rl);
        submit_approval_cv = findViewById(R.id.move_next_bt);
        this.rating_iv = findViewById(R.id.myrating_iv);
        this.identity_iv = findViewById(R.id.identity_iv);
        this.award_iv = findViewById(R.id.award_iv);
        nameTextView = findViewById(R.id.name_tv);
        bank_iv = findViewById(R.id.bank_iv);
        verifcation_status_ll = findViewById(R.id.verifcation_status_ll);
        verifcation_status_ll.setVisibility(View.GONE);
        verify_tick_iv = findViewById(R.id.verify_iv);
        verify_tick_iv.setVisibility(View.GONE);
        fewStepAway_tv = findViewById(R.id.fewsteps_tv);
        signOut_rl = findViewById(R.id.signout_rl);
//        fetchProfile();
        this.profile_civ.setOnClickListener(new C04891());
        this.identity_rl.setOnClickListener(new C04913());
        this.rating_rl.setOnClickListener(new C04924());
        this.award_rl.setOnClickListener(new C04935());
        fetchUploadStatus();
        nameTextView.setText(Preferences.getInstance(this).getMobileNumber());

        signOut_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences.getInstance(ProfileActivity.this).setLogin(false);
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Calendar c = Calendar.getInstance();
                String formattedDate = df.format(c.getTime());
                sendOfflineOnlineStatus(formattedDate, "Offline");
                Intent intent = new Intent(ProfileActivity.this, LogInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        submit_approval_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ratingStatus == 1 && bankStutus == 1 && identityStatus == 1) {
                    sendForApproval();
                } else {
                    Toast.makeText(getApplicationContext(), "Please upload all the details", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onResume() {
        fetchProfile();
        fetchUploadStatus();
        super.onResume();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return false;
        }
        onBackPressed();
        return true;
    }

    protected void onStart() {
        super.onStart();
    }

    public void onBackPressed() {
        if (getIntent().getIntExtra("FROM", -1) == 1) {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            Bundle extra = new Bundle();
            extra.putInt("FROM", new Integer(1));
            intent.putExtras(extra);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }

    private void fetchProfile() {
        CustomProgressDialog.getInstance(this);
        String url = Constants.BASE_URL + "api/get/user/profile/";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", Preferences.getInstance(this).getMobileNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebserviceHelper.getInstance().PostCall(getApplicationContext(), url, jsonObject, new C06956());
    }

    private void fetchUploadStatus() {
        CustomProgressDialog.getInstance(this);
        String url = Constants.BASE_URL + "api/get/user/status/";
        JSONObject jsonObject = new JSONObject();
//        if (mSharedPreferences.getString("saved_rating", "").equalsIgnoreCase("Saved")) {
//            rating_iv.setImageDrawable(ProfileActivity.this.getResources().getDrawable(R.drawable.green_tick_icon));
//
//        }

        try {
            jsonObject.put("mobile", Preferences.getInstance(this).getMobileNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebserviceHelper.getInstance().PostCall(getApplicationContext(), url, jsonObject, new ResponseCallback() {
            @Override
            public void OnSuccessFull(JSONObject Response) {
                try {
                    if (Response != null) {
                        CustomProgressDialog.setDismiss();
                        boolean profileStatus;
                        JSONObject jsonObject1 = Response.getJSONObject("Response");
                        ratingStatus = jsonObject1.getInt("rating");
                        awardStatus = jsonObject1.getInt("award_certification");
                        identityStatus = jsonObject1.getInt("identity_verification");
                        bankStutus = jsonObject1.getInt("bank_details");
                        int statusForApproval = jsonObject1.getInt("status");
                        profileStatus = identityStatus == 1 && bankStutus == 1;

                        Preferences.getInstance(ProfileActivity.this).setProfileCompleteStatus(Completeness());

                        Log.d("StatusListData", String.valueOf(statusForApproval));
//                        mEditor = mSharedPreferences.edit();
//                        mEditor.putInt(Constants.KEY_PROFILE_APPROVED, statusForApproval);
//                        mEditor.putBoolean(Constants.KEY_PROFILE_STATUS, profileStatus);
//                        mEditor.apply();
                        if (statusForApproval == 1) {
                            verify_tick_iv.setVisibility(View.GONE);
                            fewStepAway_tv.setVisibility(View.GONE);
                            submit_approval_cv.setVisibility(View.GONE);
                            verifcation_status_ll.setVisibility(View.VISIBLE);
                        } else if (statusForApproval == 2) {
                            verify_tick_iv.setVisibility(View.VISIBLE);
                            fewStepAway_tv.setVisibility(View.VISIBLE);
                            fewStepAway_tv.setText("Your proflie has been verified.");
                            submit_approval_cv.setVisibility(View.GONE);
                            verifcation_status_ll.setVisibility(View.GONE);
                        } else if (statusForApproval == 0) {
                            verify_tick_iv.setVisibility(View.GONE);
                            fewStepAway_tv.setVisibility(View.VISIBLE);
                            verifcation_status_ll.setVisibility(View.GONE);
                        } else if (statusForApproval == 3) {
                            verify_tick_iv.setVisibility(View.VISIBLE);
                            Drawable res = getResources().getDrawable(R.drawable.reject_icon);
                            verify_tick_iv.setImageDrawable(res);
                            fewStepAway_tv.setVisibility(View.VISIBLE);
                            fewStepAway_tv.setText("Your proflie has been rejected.");
                            submit_approval_cv.setVisibility(View.GONE);
                            verifcation_status_ll.setVisibility(View.GONE);
                        }
                        if (ratingStatus == 1) {
                            rating_iv.setImageDrawable(ProfileActivity.this.getResources().getDrawable(R.drawable.green_tick_icon));
                        }
                        if (identityStatus == 1) {
                            identity_iv.setImageDrawable(ProfileActivity.this.getResources().getDrawable(R.drawable.green_tick_icon));
                        }

                        if (awardStatus == 1) {
                            award_iv.setImageDrawable(ProfileActivity.this.getResources().getDrawable(R.drawable.green_tick_icon));
                        }

                        if (bankStutus == 1) {
                            bank_iv.setImageDrawable(ProfileActivity.this.getResources().getDrawable(R.drawable.green_tick_icon));
                            bankdetails_rl.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ProfileActivity.this, IdentityProofDetails.class);
                                    intent.putExtra("FROM", "bank");
                                    startActivity(intent);
                                }
                            });
                        } else {
                            bankdetails_rl.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ProfileActivity.this, BankDetailsActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }

                    } else {
                        CustomProgressDialog.setDismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void sendForApproval() {
        CustomProgressDialog.getInstance(this);
        String url = Constants.BASE_URL + "api/submit/user/documents/";
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
                    fetchUploadStatus();
                } else {
                    CustomProgressDialog.setDismiss();
                }

            }
        });
    }

    private void sendOfflineOnlineStatus(String time, String status) {
        String url = Constants.BASE_URL + "api/partner/online/status/";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", Preferences.getInstance(this).getMobileNumber());
            jsonObject.put("timeStamp", time);
            jsonObject.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebserviceHelper.getInstance().PostCall(getApplicationContext(), url, jsonObject, new ResponseCallback() {
            @Override
            public void OnSuccessFull(JSONObject Response) {
                Log.d("Response Status", String.valueOf(Response));

            }
        });
    }

    /* renamed from: com.servesimplified.partner.profiledetails.ProfileActivity$1 */
    class C04891 implements View.OnClickListener {
        C04891() {
        }

        public void onClick(View v) {
            ProfileActivity.this.startActivity(new Intent(ProfileActivity.this, ChooseProfileActivity.class));
        }
    }

    /* renamed from: com.servesimplified.partner.profiledetails.ProfileActivity$2 */
    class C04902 implements View.OnClickListener {
        C04902() {
        }

        public void onClick(View v) {
            Toast.makeText(ProfileActivity.this.getApplicationContext(), "Comming Soon !", Toast.LENGTH_SHORT).show();
        }
    }

    /* renamed from: com.servesimplified.partner.profiledetails.ProfileActivity$3 */
    class C04913 implements View.OnClickListener {
        C04913() {
        }

        public void onClick(View v) {
            ProfileActivity.this.startActivity(new Intent(ProfileActivity.this, UploadIdentityActivity.class));
        }
    }

    /* renamed from: com.servesimplified.partner.profiledetails.ProfileActivity$4 */
    class C04924 implements View.OnClickListener {
        C04924() {
        }

        public void onClick(View v) {
            Intent intent = new Intent(ProfileActivity.this, RatingActivity.class);
            startActivity(intent);
        }
    }

    /* renamed from: com.servesimplified.partner.profiledetails.ProfileActivity$5 */
    class C04935 implements View.OnClickListener {
        C04935() {
        }

        public void onClick(View v) {
            Intent intent = new Intent(ProfileActivity.this, AwardVerification.class);
            startActivity(intent);
        }
    }


    class C06956 implements ResponseCallback {
        C06956() {
        }

        public void OnSuccessFull(JSONObject Response) {
            CustomProgressDialog.setDismiss();
            if (Response != null) {
                try {
                    JSONObject jsonObject1 = Response.getJSONObject("Response");
                    Log.d("Response Check", String.valueOf(Response));
                    String imagePath = jsonObject1.getString("image_path") + jsonObject1.getString("image_name");
                    Log.d("ImageUrlDOne", imagePath);
                    if (!imagePath.isEmpty()) {
                        Picasso.get().load(imagePath)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .placeholder(R.drawable.img)
                                .error(R.drawable.img)
                                .into(profile_civ);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return;
            }
            CustomProgressDialog.setDismiss();
        }
    }

    private String Completeness(){
        String completeString;
        if(awardStatus == 1){
            completeString ="100 %" ;
        }else if(bankStutus ==1){
            completeString = "75 %" ;
        }else if(identityStatus == 1){
            completeString = "50 %" ;
        }else if (ratingStatus == 1){
            completeString = "25 %" ;
        }else {
            completeString = "0 %" ;
        }
        return completeString ;
    }


}
