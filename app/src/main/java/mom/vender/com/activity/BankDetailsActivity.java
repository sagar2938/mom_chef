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
import mom.vender.com.R;
import mom.vender.com.base.BaseActivity;
import mom.vender.com.network.volley.NetworkCallBacks.ResponseCallback;
import mom.vender.com.network.volley.WebserviceHelper;
import mom.vender.com.utils.Constants;
import mom.vender.com.utils.CustomProgressDialog;
import mom.vender.com.utils.Preferences;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class BankDetailsActivity extends BaseActivity implements View.OnClickListener {
    private static final String IMAGE_DIRECTORY_NAME = "MOM";
    private static int REQUEST_CAMERA_PIC = 11;
    private final int PICK_IMAGE_REQUEST = 71;
    Button confirmButton;
    ImageView edit_profile_iv;
    String idName, accountName, ifscCode;
    EditText idName_et, account_et, ifsc_et;
    String imageName;
    SharedPreferences.Editor mEditor;
    LinearLayout main_rl;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri fileUri;
    DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private BottomSheetBehavior mBottomSheetBehavior;
    TextView take_photo_tv, choose_from_librar_tv;


    @SuppressLint("WrongConstant")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mFirebaseInstance = FirebaseDatabase.getInstance();
        this.mFirebaseDatabase = this.mFirebaseInstance.getReference("fileAttachements");
        this.storage = FirebaseStorage.getInstance();
        this.storageReference = this.storage.getReference();
        this.edit_profile_iv = findViewById(R.id.edit_icon);
        this.account_et = findViewById(R.id.account_et);
        this.idName_et = findViewById(R.id.name_et);
        this.ifsc_et = findViewById(R.id.ifsc_et);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        this.mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        this.mBottomSheetBehavior.setPeekHeight(0);
        this.mBottomSheetBehavior.setState(4);
        this.confirmButton = findViewById(R.id.confirm_bt);
        this.main_rl = findViewById(R.id.idproof_main_ll);
        this.confirmButton.setOnClickListener(this);

        this.edit_profile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(3);
            }
        });

        this.choose_from_librar_tv = findViewById(R.id.choose_from_library);
        this.take_photo_tv = findViewById(R.id.take_photo);
        edit_profile_iv.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(3);
            }
        });

        this.choose_from_librar_tv = findViewById(R.id.choose_from_library);
        this.take_photo_tv = findViewById(R.id.take_photo);
        this.take_photo_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageName = Preferences.getInstance(BankDetailsActivity.this).getMobileNumber() + "-bank";
                captureImage(BankDetailsActivity.this.imageName, REQUEST_CAMERA_PIC);
            }
        });

        this.choose_from_librar_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageName = Preferences.getInstance(BankDetailsActivity.this).getMobileNumber() + "-bank";
                chooseAdharFront();
            }
        });


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


    private void chooseAdharFront() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Passbook Image"), PICK_IMAGE_REQUEST);
    }


    public Uri getOutputMediaFileUri(int type, String imageNameUrl) {
        return Uri.fromFile(getOutputMediaFile(type, imageNameUrl));
    }

    private static File getOutputMediaFile(int type, String imageNameUrl) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
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


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", this.fileUri);
    }


    @SuppressLint("WrongConstant")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CAMERA_PIC) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                this.edit_profile_iv.setImageBitmap(BitmapFactory.decodeFile(this.fileUri.getPath(), options));
                this.mBottomSheetBehavior.setState(4);
            }

            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                Log.d("RequestCode", String.valueOf(requestCode));
                this.fileUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), this.fileUri);
                    this.mBottomSheetBehavior.setState(4);
                    this.edit_profile_iv.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private void uploadProfile(final Uri fileUri, final String idType, String account, String front_image, String ifsc, String name) {
        CustomProgressDialog.getInstance(this);
        String url = Constants.BASE_URL + "api/upload/bank/details/";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", Preferences.getInstance(this).getMobileNumber());
            jsonObject.put("idType", idType);
            jsonObject.put("account_no", account);
            jsonObject.put("image_name", front_image);
            jsonObject.put("ifsc", ifsc);
            jsonObject.put("name", "");
            jsonObject.put("bank_name", name);
            jsonObject.put("status", "Active");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebserviceHelper.getInstance().PostCall(getApplicationContext(), url, jsonObject, new ResponseCallback() {
            public void OnSuccessFull(JSONObject Response) {
                if (Response != null) {
                    CustomProgressDialog.setDismiss();
                    uploadMethod(fileUri,imageName);
                    return;
                } else {
                    CustomProgressDialog.setDismiss();
                }
            }
        });
    }

    public void onClick(View v) {
        if (v.getId() == R.id.confirm_bt) {

            imageName = Preferences.getInstance(this).getMobileNumber() + "-bank";
            String idType = "Bank Passbook";
            this.idName = this.idName_et.getText().toString();
            this.accountName = this.account_et.getText().toString();
            this.ifscCode = this.ifsc_et.getText().toString();
            if (accountName.trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter account number", Toast.LENGTH_SHORT).show();
            } else if (idName.trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter name", Toast.LENGTH_SHORT).show();

            } else if (ifscCode.trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter Ifsc code", Toast.LENGTH_SHORT).show();

            } else if (fileUri == null) {
                Toast.makeText(getApplicationContext(), "Please select image", Toast.LENGTH_SHORT).show();
            } else {

                uploadProfile(this.fileUri, idType, this.accountName, this.imageName, ifscCode, this.idName);
            }
        }
    }

    private void uploadMethod(Uri imageUri,String imageName) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReferenceProfilePic = firebaseStorage.getReference();
        StorageReference imageRef = storageReferenceProfilePic.child("idproof" + "/" + imageName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        String profilePicUrl = taskSnapshot.getUploadSessionUri().toString();
                        Intent intent = new Intent(BankDetailsActivity.this, ProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), exception.getCause().getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //calculating progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });

    }



}
