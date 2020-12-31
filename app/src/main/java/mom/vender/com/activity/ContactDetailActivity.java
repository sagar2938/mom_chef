package mom.vender.com.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import mom.vender.com.R;
import mom.vender.com.base.BaseActivity;
import mom.vender.com.network.ApiCallService;
import mom.vender.com.network.response.GetProfileResponse;
import mom.vender.com.network.response.SignUpResponse;
import mom.vender.com.network.response.User_Data;
import mom.vender.com.utils.Helper;
import mom.vender.com.utils.ImagePicker;
import mom.vender.com.utils.Preferences;
import mom.vender.com.utils.UploadEvent;
import mom.vender.com.utils.Validation;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class ContactDetailActivity extends BaseActivity {

    Button submit;

    ImageView openPicker;
    ImageView closePicker;
    ImageView startPicker;
    ImageView endPicker;

    TextView open;
    TextView close;
    TextView start;
    TextView end;
    EditText address;
    Spinner country;
    Spinner state;
    EditText pin;
    EditText license;
    TextView dob;
    ImageView dobPicker;
    EditText specialization;
    EditText comment;
    EditText about_mom;


    EditText mobile;
    EditText firstName;
    EditText middleName;
    EditText lastName;
    EditText email;
    RelativeLayout upload;
    ImageView image;
    TextView mark;


    RelativeLayout markLocation;
    private ImageView tick;
    private ImageView locationIcon;
    private ImageView coverImage;
    private LinearLayout uploadCover;
    private LinearLayout uploadCover2;

    String lat;
    String lon;

    String url = "";
    String coverUrl = "";

    String type = "";

    GetProfileResponse response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        getSupportActionBar().setTitle("Update Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        coverImage = findViewById(R.id.coverImage);
        uploadCover = findViewById(R.id.uploadCover);
        uploadCover2 = findViewById(R.id.uploadCover2);
        submit = findViewById(R.id.submit);
        openPicker = findViewById(R.id.openPicker);
        closePicker = findViewById(R.id.closePicker);
        startPicker = findViewById(R.id.startPicker);
        endPicker = findViewById(R.id.endPicker);
        open = findViewById(R.id.open);
        close = findViewById(R.id.close);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        address = findViewById(R.id.address);
        country = findViewById(R.id.country);
        state = findViewById(R.id.state);
        pin = findViewById(R.id.pin);
        license = findViewById(R.id.license);
        dob = findViewById(R.id.dob);
        dobPicker = findViewById(R.id.dobPicker);
        specialization = findViewById(R.id.specialization);
        comment = findViewById(R.id.comment);
        about_mom = findViewById(R.id.about_mom);

        mobile = findViewById(R.id.mobile);
        firstName = findViewById(R.id.firstName);
        middleName = findViewById(R.id.middleName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        upload = findViewById(R.id.upload);
        image = findViewById(R.id.image);
        submit = findViewById(R.id.submit);
        mark = findViewById(R.id.mark);

        locationIcon = findViewById(R.id.location_icon);
        tick = findViewById(R.id.tick);
        markLocation = findViewById(R.id.markLocation);

        Map map = new HashMap();
        map.put("mobile", Preferences.getInstance(getApplicationContext()).getMobileNumber());
        ApiCallService.action(this, map, ApiCallService.Action.ACTION_GET_PROFILE);


        locationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactDetailActivity.this, PersonalDetailsActivity.class);
                intent.putExtra("from", true);
                startActivityForResult(intent, 3);
            }
        });
        markLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactDetailActivity.this, PersonalDetailsActivity.class);
                intent.putExtra("from", true);
                startActivityForResult(intent, 3);
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!checkPermissions()) {
                    requestPermission();
                }else {
                    type = "profile";
                    dialogPlus();
                }



            }
        });

        uploadCover2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermissions()) {
                    requestPermission();
                }else {
                    type = "cover";
                    dialogPlus();
                }
            }
        });


        dobPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(ContactDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        dob.setText(sdf1.format(myCalendar.getTime()));

                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });


        timePicker(openPicker, open);
        timePicker(closePicker, close);
        timePicker(startPicker, start);
        timePicker(endPicker, end);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap();
                if (firstName.getText().toString().trim().isEmpty()) {
                    getDialog("Enter first name");
                    return;
                }
                if (!email.getText().toString().trim().isEmpty()) {
                    if (!Validation.isEmailValid(email.getText().toString())) {
                        getDialog("Enter valid email id");
                        return;
                    }
                }
                if (address.getText().toString().trim().isEmpty()) {
                    getDialog("Enter address");
                    return;
                }
                if (pin.getText().toString().trim().isEmpty()) {
                    getDialog("Enter zip code");
                    return;
                }
                if (pin.getText().toString().trim().length() != 6) {
                    getDialog("Enter valid zip code");
                    return;
                }
                /*if (license.getText().toString().trim().isEmpty()) {
                    getDialog("Enter license");
                    return;
                }*/
                if (dob.getText().toString().trim().isEmpty()) {
                    getDialog("Enter date of birth");
                    return;
                }
                if (specialization.getText().toString().trim().isEmpty()) {
                    getDialog("Enter specialization");
                    return;
                }
                if (specialization.getText().toString().trim().isEmpty()) {
                    getDialog("Enter specialization");
                    return;
                }

                if (open.getText().toString().trim().isEmpty()) {
                    getDialog("Enter open time");
                    return;
                }
                if (close.getText().toString().trim().isEmpty()) {
                    getDialog("Enter close time");
                    return;
                }
                if (start.getText().toString().trim().isEmpty()) {
                    getDialog("Enter break start time");
                    return;
                }
                if (end.getText().toString().trim().isEmpty()) {
                    getDialog("Enter break end time");
                    return;
                }
                if (mark.getText().toString().trim().isEmpty()) {
                    getDialog("Mark your location on map");
                    return;
                }


                if (lat == null) {
                    getDialog("Mark your location on map");
                    return;
                }

                if (lon == null) {
                    getDialog("Mark your location on map");
                    return;
                }


                if (lat.isEmpty()) {
                    getDialog("Mark your location on map");
                    return;
                }

                if (lon.isEmpty()) {
                    getDialog("Mark your location on map");
                    return;
                }if (about_mom.getText().toString().trim().isEmpty()){
                    getDialog("Enter Brief info about MOM CHEF");
                    return;
                }

                map.put("mobile", Preferences.getInstance(getApplicationContext()).getMobileNumber());
                map.put("firstName", firstName.getText().toString());
                map.put("middleName", middleName.getText().toString());
                map.put("lastName", lastName.getText().toString());
                map.put("url", url);
                map.put("email", email.getText().toString());
                map.put("address", address.getText().toString());
                map.put("country", country.getSelectedItem().toString());
                map.put("state", state.getSelectedItem().toString());
                map.put("zipCode", pin.getText().toString());
                map.put("foodLicenseNo", license.getText().toString());
                map.put("dob", dob.getText().toString());
                map.put("specialization", specialization.getText().toString());
                map.put("comment", comment.getText().toString());
                map.put("openTime", open.getText().toString());
                map.put("endTime", close.getText().toString());
                map.put("breakStart", start.getText().toString());
                map.put("breakEnd", end.getText().toString());
                map.put("image_name", url);
                map.put("latitude", lat);
                map.put("longitude", lon);
                map.put("trackingStatus", "");
                map.put("about_mom", about_mom.getText().toString());
                map.put("image", coverUrl);
                ApiCallService.action(ContactDetailActivity.this, map, ApiCallService.Action.ACTION_UPDATE_PROFILE);

            }
        });

    }


    void timePicker(View view, TextView textView) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(ContactDetailActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String h = "";
                                String m = "";
                                if (hourOfDay < 10) {
                                    h = "0" + hourOfDay;
                                } else {
                                    h = "" + hourOfDay;
                                }

                                if (minute < 10) {
                                    m = "0" + minute;
                                } else {
                                    m = "" + minute;
                                }
                                textView.setText("" + h + ":" + m);
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });
    }


    @Subscribe
    public void signUp(SignUpResponse response) {
        if (response.getResponse().getConfirmation() == 1) {
            Toast.makeText(this, "Successfully updated", Toast.LENGTH_SHORT).show();
            Preferences.getInstance(getApplicationContext()).setProfileImage(url);

            if (!Preferences.getInstance(getApplicationContext()).getProfileStatus().equals("1")) {
                getDialog("Success", "Your Profile has been updated successfully, yet to be verified by admin.");
            } else {
                Preferences.getInstance(getApplicationContext()).setLogin(true);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

        } else {
            getDialog(response.getResponse().getMessage());
        }
    }


    @Subscribe
    public void getProfile(GetProfileResponse response) {
        if (response.getSuccess()) {
            this.response = response;
            User_Data userData = response.getUserData().get(0);
            open.setText(userData.getOpenTime());
            close.setText(userData.getCloseTime());
            start.setText(userData.getBreakStart());
            end.setText(userData.getBreakEnd());
            address.setText(userData.getAddress());
            if (userData.getZipCode() == null) {
                pin.setText("");
            } else {
                pin.setText("" + userData.getZipCode());
            }
            license.setText(userData.getFoodLicenseNo());
            dob.setText(userData.getDob());
            specialization.setText(userData.getSpecialization());
            comment.setText(userData.getComment());

            firstName.setText(userData.getFirstName());
            middleName.setText(userData.getMiddleName());
            lastName.setText(userData.getLastName());
            mobile.setText(userData.getMobile());
            email.setText(userData.getEmail());
            about_mom.setText(userData.getAbout_mom());
            if (userData.getImage_name() != null) {
                url = userData.getImage_name();
            } else {
                url = "";
            }
            if (userData.getImage() != null) {
                coverUrl = userData.getImage();
            } else {
                coverUrl = "";
            }

            lat = userData.getLatitude();
            lon = userData.getLongitude();

            if (lat != null) {
                mark.setText("Your location has mapped");
                tick.setImageDrawable(getResources().getDrawable(R.drawable.tick_location));
            } else if (lon != null) {
                mark.setText("Your location has mapped");
                tick.setImageDrawable(getResources().getDrawable(R.drawable.tick_location));
            } else if (!lat.isEmpty()) {
                mark.setText("Your location has mapped");
                tick.setImageDrawable(getResources().getDrawable(R.drawable.tick_location));
            } else if (!lon.isEmpty()) {
                mark.setText("Your location has mapped");
                tick.setImageDrawable(getResources().getDrawable(R.drawable.tick_location));
            }else {
                mark.setText("Mark your location on map");
                tick.setImageDrawable(getResources().getDrawable(R.drawable.untick_location));
            }

            if (userData.getImage_name() != null) {
                if (!userData.getImage_name().equals("")) {
                    Glide.with(this)
                            .load(userData.getImage_name())
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .apply(RequestOptions.skipMemoryCacheOf(true))
//                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                            .apply(new RequestOptions().placeholder(R.drawable.user))
                            .into(image);
                }
            }

            if (userData.getImage() != null) {
                if (!userData.getImage().equals("")) {
                    Glide.with(this)
                            .load(userData.getImage())
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .apply(RequestOptions.skipMemoryCacheOf(true))
//                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                            .apply(new RequestOptions().placeholder(R.drawable.user))
                            .into(coverImage);
                }
            }

            Preferences.getInstance(getApplicationContext()).setProfileStatus(userData.getProfileStatus());


        } else {
            getDialog("Something went wrong");
        }
    }


    void dialog() {
        imageName = Helper.getRandom();
        new AlertDialog.Builder(ContactDetailActivity.this)
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
                        imageName = Helper.getRandom();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT > 23) {
                            fileUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", Helper.getOutputMediaFile(MEDIA_TYPE_IMAGE, imageName));
                        } else {
                            fileUri = Uri.fromFile(Helper.getOutputMediaFile(MEDIA_TYPE_IMAGE, imageName));
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
    public void uploadEvent(UploadEvent event) {
        if (type.equals("profile")) {
            url = event.getUrl();
        } else {
            coverUrl = event.getUrl();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                if (requestCode == 3) {
                    String address = data.getStringExtra("address");
                    lat = data.getStringExtra("lat");
                    lon = data.getStringExtra("lon");
//                    Toast.makeText(this, ""+lat+","+lon, Toast.LENGTH_SHORT).show();
//                    this.address.setText(address);
                    this.mark.setText("Your location has been marked on map");
                    tick.setImageDrawable(getResources().getDrawable(R.drawable.tick_location));
                }
                if (requestCode == REQUEST_CAMERA) {
                    /*BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    if (type.equals("profile")) {
                        image.setImageURI(this.fileUri);
                    } else {
                        coverImage.setImageURI(this.fileUri);
                    }
                    fileUri = Uri.fromFile(Helper.getOutputMediaFile(MEDIA_TYPE_IMAGE, imageName));
                    String str = compressImage(this.fileUri.toString());
                    Uri uri = Uri.fromFile(new File(str));
                    uploadFile(imageName, uri);*/
                    startCropImageActivity(this.fileUri);
                }
                if (requestCode == REQUEST_GALLERY) {
                    try {
                        fileUri = data.getData();
                        if (type.equals("profile")) {
                            image.setImageURI(this.fileUri);
                        } else {
                            coverImage.setImageURI(this.fileUri);
                        }
                        startCropImageActivity(this.fileUri);
                       /* String str = compressImage(fileUri.toString());
                        Uri uri = Uri.fromFile(new File(str));
                        uploadFile(imageName, uri);*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {

                        /*BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;
                        if (type.equals("profile")) {
                            image.setImageURI(this.fileUri);
                        } else {
                            coverImage.setImageURI(this.fileUri);
                        }
                        fileUri = Uri.fromFile(Helper.getOutputMediaFile(MEDIA_TYPE_IMAGE, imageName));
                        */
                        fileUri = result.getUri();
                        if (type.equals("profile")) {
                            image.setImageURI(this.fileUri);
                        } else {
                            coverImage.setImageURI(this.fileUri);
                        }

                        String str = compressImage(this.fileUri.toString());
                        Uri uri = Uri.fromFile(new File(str));
                        uploadFile(imageName, uri);


                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
//                .setRequestedSize(400,200)
                .setMinCropResultSize(4000, 1500)
                .setMaxCropResultSize(4000, 1600)
                .start(this);
    }




    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(ContactDetailActivity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(ContactDetailActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(ContactDetailActivity.this,
                permissions, 100);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ContactDetailActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ContactDetailActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(ContactDetailActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ContactDetailActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}