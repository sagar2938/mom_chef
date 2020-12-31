package mom.vender.com.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import mom.vender.com.R;
import mom.vender.com.network.ThisApp;
import mom.vender.com.network.response.GetVersionResponse;
import mom.vender.com.utils.AppUser;
import mom.vender.com.utils.LocalRepositories;
import mom.vender.com.utils.Preferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    public static final int MULTIPLE_PERMISSIONS = 4;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAPTURE_VIDEO_OUTPUT,
            Manifest.permission.CAPTURE_SECURE_VIDEO_OUTPUT,
            Manifest.permission.CAMERA};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        if (LocalRepositories.getAppUser(this)==null){
            LocalRepositories.saveAppUser(getApplicationContext(),new AppUser());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final int ACCESS_COARSE_LOCATION = PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            final int ACCESS_FINE_LOCATION = PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            final int WRITE_EXTERNAL_STORAGE = PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            final int READ_EXTERNAL_STORAGE = PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            final int CALL_PHONE = PermissionChecker.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
            final int CAMERA = PermissionChecker.checkSelfPermission(this, Manifest.permission.CAMERA);
            final int CAPTURE_VIDEO_OUTPUT = PermissionChecker.checkSelfPermission(this, Manifest.permission.CAPTURE_VIDEO_OUTPUT);
            final int CAPTURE_SECURE_VIDEO_OUTPUT = PermissionChecker.checkSelfPermission(this, Manifest.permission.CAPTURE_SECURE_VIDEO_OUTPUT);
            if (ACCESS_COARSE_LOCATION == PermissionChecker.PERMISSION_GRANTED
                    && ACCESS_FINE_LOCATION == PermissionChecker.PERMISSION_GRANTED
                    && WRITE_EXTERNAL_STORAGE == PermissionChecker.PERMISSION_GRANTED
                    && READ_EXTERNAL_STORAGE == PermissionChecker.PERMISSION_GRANTED
                    && CALL_PHONE == PermissionChecker.PERMISSION_GRANTED
                    && CAPTURE_VIDEO_OUTPUT == PermissionChecker.PERMISSION_GRANTED
                    && CAPTURE_SECURE_VIDEO_OUTPUT == PermissionChecker.PERMISSION_GRANTED
                    && CAMERA == PermissionChecker.PERMISSION_GRANTED) {
                launchActivity();
            } else {
                checkPermissions();
            }
        } else {
            launchActivity();
        }


    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(SplashActivity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);

            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchActivity();
                } else {
                    launchActivity();
                }
                return;
            }
        }
    }


    void launchActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ThisApp.getApi(getApplicationContext()).getVersion().enqueue(new Callback<GetVersionResponse>() {
                    @Override
                    public void onResponse(Call<GetVersionResponse> call, Response<GetVersionResponse> res) {
                        if (res.code() == 200) {
                            GetVersionResponse response = res.body();
                            if (response.isSuccess()) {
                                PackageInfo packageInfo = null;
                                try {
                                    packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                }
                                String versionName = packageInfo.versionName;
                                // Long versionCode = packageInfo.getLongVersionCode();
                                GetVersionResponse.App_data add_data=response.getApp_data().get(0);
                                if (add_data.getMomCheffVersion()!=null && add_data.getMomCheffVersion().compareTo(versionName)<=0) {
                                    if(Preferences.getInstance(SplashActivity.this).isLogin()){
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        finish();
                                    }else {
                                        startActivity(new Intent(getApplicationContext(), LogInSignUpActivity.class));
                                        finish();
                                    }
                                } else {
                                    final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(SplashActivity.this);
                                    alert.setCancelable(false);
                                    alert.setTitle("Update Required");
                                    alert.setMessage("A new version of MOMChef is available. Please update to version " + add_data.getMomCheffVersion() + " now.");
                                    alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                                            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=mom.com"));
                                            startActivity(i);
                                            alert.show();
                                        }
                                    });
                                    if (add_data.getFlag()==0){
                                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                if(Preferences.getInstance(SplashActivity.this).isLogin()){
                                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                                    finish();
                                                }else {
                                                    startActivity(new Intent(getApplicationContext(), LogInSignUpActivity.class));
                                                    finish();
                                                }
                                            }
                                        });
                                    }



                                    alert.show();
                                }

                            }

                        } else {
                            getDialog("Some thing went wrong " + res.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<GetVersionResponse> call, Throwable t) {
                        getDialog(t.getMessage());
                    }
                });












            }
        }, 500);
    }

    public void getDialog(String message) {
        new AlertDialog.Builder(SplashActivity.this)
                .setTitle("Alert")
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        checkPermissions();
                    }
                })
//                .setNegativeButton("Cancel", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

}
