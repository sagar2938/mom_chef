package mom.vender.com.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import mom.vender.com.R;
import mom.vender.com.base.BaseActivity;
import mom.vender.com.network.volley.NetworkCallBacks.ResponseCallback;
import mom.vender.com.network.volley.WebserviceHelper;
import mom.vender.com.utils.Constants;
import mom.vender.com.utils.CustomProgressDialog;
import mom.vender.com.utils.Preferences;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class LicenseUploadActivity extends BaseActivity implements View.OnClickListener {
    private static final String IMAGE_DIRECTORY_NAME = "MOM";
    private static int REQUEST_CAMERA_PIC = 11;
    private static int REQUEST_CAMERAB_PIC = 12;
    private static String clickType;
    public static String uniqueId;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int PICK_IMAGEB_REQUEST = 72;
    TextView choose_from_librar_tv, imgsecond_tv;
    Button confirmButton;
    ImageView edit_profile_iv;
    private Uri fileUri, fileUri_back;
    String idCode;
    EditText idCode_et;
    String idName;
    EditText idName_et;
    String idType;
    String imageName;
    String imageName2;
    private List<String> lowerList;
    private BottomSheetBehavior mBottomSheetBehavior;
    SharedPreferences.Editor mEditor;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    LinearLayout main_rl;
    Spinner schoolrange_lower_sp;
    FirebaseStorage storage;
    StorageReference storageReference;
    TextView take_photo_tv;

    /* renamed from: com.servesimplified.partner.uploadproof.LicenseUploadActivity$1 */
    class C05041 implements View.OnClickListener {
        C05041() {
        }

        @SuppressLint("WrongConstant")
        public void onClick(View v) {
            idType = "Driving License";
            LicenseUploadActivity.this.mBottomSheetBehavior.setState(3);

        }
    }


    @SuppressLint("WrongConstant")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_license);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mFirebaseInstance = FirebaseDatabase.getInstance();
        this.mFirebaseDatabase = this.mFirebaseInstance.getReference("fileAttachements");
        this.storage = FirebaseStorage.getInstance();
        this.storageReference = this.storage.getReference();
        this.schoolrange_lower_sp = findViewById(R.id.grade_rage_sp);
        this.edit_profile_iv =  findViewById(R.id.edit_icon);
        this.idCode_et = findViewById(R.id.idcode_et);
        this.idName_et =  findViewById(R.id.idproof_et);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        this.confirmButton =  findViewById(R.id.confirm_bt);
        this.main_rl =  findViewById(R.id.idproof_main_ll);
        this.confirmButton.setOnClickListener(this);
        this.mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        this.mBottomSheetBehavior.setPeekHeight(0);
        this.mBottomSheetBehavior.setState(4);
        this.edit_profile_iv.setOnClickListener(new LicenseUploadActivity.C05041());
        this.choose_from_librar_tv = findViewById(R.id.choose_from_library);
        this.take_photo_tv = findViewById(R.id.take_photo);
        this.take_photo_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageName = Preferences.getInstance(LicenseUploadActivity.this).getMobileNumber() + "-license";
                LicenseUploadActivity.this.captureImage(LicenseUploadActivity.this.imageName, REQUEST_CAMERA_PIC);
            }
        });
        this.choose_from_librar_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageName = Preferences.getInstance(LicenseUploadActivity.this).getMobileNumber() + "-license";
                LicenseUploadActivity.this.chooseAdharFront();
            }
        });

    }

    private void uploadImage(Uri filePath) {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child("idproof/" + imageName);
            ref.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            try {
                                progressDialog.dismiss();
                                Preferences.getInstance(LicenseUploadActivity.this).setKeyIdType(idType);
                                Intent intent = new Intent(LicenseUploadActivity.this, UploadIdentityActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(LicenseUploadActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");

                        }
                    });
        }
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

    private void captureImage(String imageNameUrl, int imageRequest) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT > 23) {
            this.fileUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", getOutputMediaFile(MEDIA_TYPE_IMAGE, imageNameUrl));
        } else {
            this.fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, imageNameUrl);
        }
        intent.putExtra("output", this.fileUri);
        startActivityForResult(intent, imageRequest);
    }


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", this.fileUri);
    }

    public Uri getOutputMediaFileUri(int type, String imageNameUrl) {
        return Uri.fromFile(getOutputMediaFile(type, imageNameUrl));
    }

    private static File getOutputMediaFile(int type, String imageNameUrl) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create ServeSimplified directory");
            return null;
        } else if (type != MEDIA_TYPE_IMAGE) {
            return null;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(mediaStorageDir.getPath());
            stringBuilder.append(File.separator);
            stringBuilder.append(imageNameUrl);
            stringBuilder.append(".jpg");
            return new File(stringBuilder.toString());
        }
    }

    @SuppressLint("WrongConstant")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA_PIC && resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            this.edit_profile_iv.setImageBitmap(BitmapFactory.decodeFile(this.fileUri.getPath(), options));
            this.mBottomSheetBehavior.setState(4);
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Log.d("RequestCode", String.valueOf(requestCode));
            this.fileUri = data.getData();
            Log.d("RequestBack", String.valueOf(this.fileUri));
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), this.fileUri);
                this.mBottomSheetBehavior.setState(4);
                this.edit_profile_iv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void chooseAdharFront() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Aadhaar back"), PICK_IMAGE_REQUEST);
    }

    private void uploadProfile(final Uri fileUri, String idNumber, String front_image, final String name) {
        CustomProgressDialog.getInstance(this);
        String url = Constants.BASE_URL + "api/upload/license/details/";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", Preferences.getInstance(LicenseUploadActivity.this).getMobileNumber());
            jsonObject.put("license_no", idNumber);
            jsonObject.put("image_name", front_image);
            jsonObject.put("license_name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebserviceHelper.getInstance().PostCall(getApplicationContext(), url, jsonObject, new ResponseCallback() {
            public void OnSuccessFull(JSONObject Response) {
                if (Response != null) {
                    CustomProgressDialog.setDismiss();
                    LicenseUploadActivity.this.uploadImage(fileUri);
                    Log.d("ResponseUploadImg", String.valueOf(Response));

                } else {
                    CustomProgressDialog.setDismiss();
                }
            }
        });
    }


    public void onClick(View v) {
        if (v.getId() == R.id.confirm_bt) {

            this.idName = this.idName_et.getText().toString();
            this.idCode = this.idCode_et.getText().toString();
            if (idCode.trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter id number", Toast.LENGTH_SHORT).show();
            } else if (idName.trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter name", Toast.LENGTH_SHORT).show();

            } else if (fileUri == null) {
                Toast.makeText(getApplicationContext(), "Please select image", Toast.LENGTH_SHORT).show();
            } else {

                uploadProfile(this.fileUri, this.idCode, this.imageName, this.idName);
            }
        }
    }


}
