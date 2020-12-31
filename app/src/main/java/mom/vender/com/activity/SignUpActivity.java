package mom.vender.com.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import mom.vender.com.base.BaseActivity;
import mom.vender.com.R;
import mom.vender.com.network.request.RequestSignUp;
import mom.vender.com.network.volley.NetworkCallBacks.ResponseCallback;
import mom.vender.com.network.volley.WebserviceHelper;
import mom.vender.com.utils.AppUser;
import mom.vender.com.utils.Constants;
import mom.vender.com.utils.Helper;
import mom.vender.com.utils.LocalRepositories;
import mom.vender.com.utils.MyProgressDialog;
import mom.vender.com.utils.Preferences;
import mom.vender.com.utils.UploadEvent;
import mom.vender.com.utils.Validation;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class SignUpActivity extends BaseActivity {
    String url="";
    Uri fileUri;

    EditText mobile;
    EditText firstName;
    EditText middleName;
    EditText lastName;
    EditText aboutMom;
    EditText email;
    RelativeLayout upload;
    Button submit;
    ImageView image;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        mobile=findViewById(R.id.mobile);
        firstName=findViewById(R.id.firstName);
        middleName=findViewById(R.id.middleName);
        lastName=findViewById(R.id.lastName);
        email=findViewById(R.id.email);
        upload=findViewById(R.id.upload);
        image=findViewById(R.id.image);
        submit=findViewById(R.id.submit);
        aboutMom = findViewById(R.id.mom_about);




        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile.getText().toString().trim().isEmpty()){
                    getDialog("Enter mobile number");
                    return;
                }if (mobile.getText().toString().trim().length()!=10){
                    getDialog("Enter valid mobile number");
                    return;
                }if (firstName.getText().toString().trim().isEmpty()){
                    getDialog("Enter firstName");
                    return;
                }if (!email.getText().toString().trim().isEmpty()){
                    if (!Validation.isEmailValid(email.getText().toString())){
                        getDialog("Enter valid email id");
                        return;
                    }
                }if (aboutMom.getText().toString().trim().isEmpty()){
                    getDialog("Enter Brief info about MOM CHEF");
                    return;
                }



                RequestSignUp requestSignUp=new RequestSignUp();
                requestSignUp.setMobile(mobile.getText().toString());
                requestSignUp.setFirstName(firstName.getText().toString());
                requestSignUp.setMiddleName(middleName.getText().toString());
                requestSignUp.setLastName(lastName.getText().toString());
                requestSignUp.setEmail(email.getText().toString());
                requestSignUp.setAbout_mom(aboutMom.getText().toString());
                requestSignUp.setUrl(url);
                Preferences.getInstance(getApplicationContext()).setOtp(Helper.getOtp());
                requestSignUp.setOtp(Preferences.getInstance(getApplicationContext()).getOtp());
                AppUser appUser=LocalRepositories.getAppUser(getApplicationContext());
                appUser.setRequestSignUp(requestSignUp);
                LocalRepositories.saveAppUser(getApplicationContext(),appUser);

                signUpStatusCheck(mobile.getText().toString().trim(),"");

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                if (requestCode == 1) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    uploadFile(Helper.getRandom(), this.fileUri);
                }
                if (requestCode == 2) {
                    fileUri = data.getData();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    uploadFile(Helper.getRandom(), this.fileUri );
                }
                image.setImageURI(this.fileUri);
            }
        } catch (Exception ex) {
        }

    }


    void dialog() {
        new AlertDialog.Builder(SignUpActivity.this)
                .setTitle("Select From")
                .setCancelable(true)
                .setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
                    }
                })
                .setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String imageName=Helper.getRandom();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT > 23) {
                            fileUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", Helper.getOutputMediaFile(MEDIA_TYPE_IMAGE, imageName));
                        } else {
                            fileUri = Uri.fromFile(Helper.getOutputMediaFile(MEDIA_TYPE_IMAGE,imageName ));
                        }
                        //   this line is to be used for android 9
                        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                        for (ResolveInfo resolveInfo : resInfoList) {
                            String packageName = resolveInfo.activityInfo.packageName;
                            grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                        intent.putExtra("output", fileUri);
                        startActivityForResult(intent, 1);
                    }
                })
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

    @Subscribe
    public void uploadEvent(UploadEvent event){
        url=event.getUrl();
    }

    private void signUpStatusCheck(final String mobile, final String referalCode) {
        MyProgressDialog.getInstance(this);
        String url = Constants.BASE_URL + "api/vendor/check/auth/";
        Log.d("http GeneratedOTP", Preferences.getInstance(getApplicationContext()).getOtp());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", mobile);
            jsonObject.put("otp", Preferences.getInstance(getApplicationContext()).getOtp());
            jsonObject.put("referal_code", referalCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebserviceHelper.getInstance().PostCall(getApplicationContext(), url, jsonObject, new ResponseCallback() {
            public void OnSuccessFull(JSONObject Response) {
                MyProgressDialog.setDismiss();
                if (Response != null) {
                    Log.d("Response SignUp", String.valueOf(Response));
                    try {
                        JSONObject jsonObject1 = Response.getJSONObject("response");
                        if (jsonObject1.getInt("confirmation") == 1) {
                            Toast.makeText(getApplicationContext(), "User Already Exist", Toast.LENGTH_SHORT).show();
                        } else if (jsonObject1.getInt("confirmation") == 2) {
                            Toast.makeText(getApplicationContext(), "Referral code does not exist", Toast.LENGTH_SHORT).show();
                        } else {
//                            Toast.makeText(SignUpActivity.this, ""+Preferences.getInstance(getApplicationContext()).getOtp(), Toast.LENGTH_SHORT).show();
                            Preferences.getInstance(SignUpActivity.this).setOtp(Preferences.getInstance(getApplicationContext()).getOtp());
                            Preferences.getInstance(SignUpActivity.this).setMobileNumber(mobile);
                            Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);
                            intent.putExtra("mobile", mobile);
                            intent.putExtra("From", 1);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
