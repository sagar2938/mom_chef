package mom.vender.com.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ImageView;
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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import de.hdodenhof.circleimageview.CircleImageView;
import mom.vender.com.R;
import mom.vender.com.base.BaseActivity;
import mom.vender.com.network.volley.NetworkCallBacks.ResponseCallback;
import mom.vender.com.network.volley.WebserviceHelper;
import mom.vender.com.utils.Constants;
import mom.vender.com.utils.CustomProgressDialog;
import mom.vender.com.utils.Preferences;

public class ChooseProfileActivity extends BaseActivity {

    private static final String IMAGE_DIRECTORY_NAME = "MOM";
    public static String uniqueId;
    private static int REQUEST_CAMERA_PIC = 11;
    private final int PICK_IMAGE_REQUEST = 71;
    TextView choose_from_librar_tv;
    ImageView edit_profile_iv;
    String imageName;
    CircleImageView profileImg_civ;
    FirebaseStorage storage;
    StorageReference storageReference;
    TextView take_photo_tv;
    private Uri fileUri;
    private BottomSheetBehavior mBottomSheetBehavior;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private static File getOutputMediaFile(int type, String imageNameUrl) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create MOM directory");
            return null;
        } else if (type != 1) {
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.logo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        this.mFirebaseInstance = FirebaseDatabase.getInstance();
        this.mFirebaseDatabase = this.mFirebaseInstance.getReference("fileAttachements");
        this.storage = FirebaseStorage.getInstance();
        this.storageReference = this.storage.getReference();
        this.edit_profile_iv = findViewById(R.id.edit_icon);
        this.profileImg_civ = findViewById(R.id.profileimage_iv);
        this.mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        this.mBottomSheetBehavior.setPeekHeight(0);
        this.mBottomSheetBehavior.setState(4);
        this.edit_profile_iv.setOnClickListener(new C04861());
        this.choose_from_librar_tv = findViewById(R.id.choose_from_library);
        this.take_photo_tv = findViewById(R.id.take_photo);
        this.take_photo_tv.setOnClickListener(new C04872());
        this.choose_from_librar_tv.setOnClickListener(new C04883());
        fetchProfile();
    }

    private void uploadImage(byte[] filePath) {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child("profile/" + imageName);
            ref.putBytes(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(ChooseProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ChooseProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (Build.VERSION.SDK_INT > 23) {
            this.fileUri = FileProvider.getUriForFile(this, getPackageName(), getOutputMediaFile(1, imageNameUrl));
        } else {
            this.fileUri = getOutputMediaFileUri(1, imageNameUrl);
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

    @SuppressLint("WrongConstant")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA_PIC) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeFile(this.fileUri.getPath(), options);
                this.profileImg_civ.setImageBitmap(bitmap);
                this.mBottomSheetBehavior.setState(4);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                byte[] decoded = out.toByteArray();
                uploadProfile(decoded, this.imageName);
            }

            if (requestCode == PICK_IMAGE_REQUEST  && data != null && data.getData() != null) {
                this.fileUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), this.fileUri);
                    this.profileImg_civ.setImageBitmap(bitmap);
                    this.mBottomSheetBehavior.setState(4);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                    byte[] decoded = out.toByteArray();
                    uploadProfile(decoded, this.imageName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void chooseAdharBack() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Profile"), PICK_IMAGE_REQUEST);
    }

    private void uploadProfile(final byte[] fileUri, String imageName) {
        CustomProgressDialog.getInstance(this);
        String url = Constants.BASE_URL + "api/upload/profile/";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", Preferences.getInstance(this).getMobileNumber());
            jsonObject.put("image_name", imageName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebserviceHelper.getInstance().PostCall(getApplicationContext(), url, jsonObject, new ResponseCallback() {
            public void OnSuccessFull(JSONObject Response) {
                CustomProgressDialog.setDismiss();
                if (Response != null) {
                    uploadImage(fileUri);
                    Log.d("ResponseUploadImg", String.valueOf(Response));
                }
            }
        });
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
        WebserviceHelper.getInstance().PostCall(getApplicationContext(), url, jsonObject, new ResponseCallback() {
            @Override
            public void OnSuccessFull(JSONObject Response) {
                if (Response != null) {
                    try {
                        JSONObject jsonObject1 = Response.getJSONObject("Response");
                        String imgURL = jsonObject1.getString("image_path") + jsonObject1.getString("image_name");
                        if(!imgURL.isEmpty()) {
                            Picasso.get().load(imgURL)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                    .placeholder(R.drawable.img)
                                    .error(R.drawable.img)
                                    .into(profileImg_civ);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        });
    }

    class C04861 implements View.OnClickListener {
        C04861() {
        }

        @SuppressLint("WrongConstant")
        public void onClick(View v) {
            ChooseProfileActivity.this.mBottomSheetBehavior.setState(3);
        }
    }


    class C04872 implements View.OnClickListener {
        C04872() {
        }

        public void onClick(View view) {
            imageName = Preferences.getInstance(ChooseProfileActivity.this).getMobileNumber() + "-profile";
            ChooseProfileActivity.this.captureImage(ChooseProfileActivity.this.imageName, ChooseProfileActivity.REQUEST_CAMERA_PIC);
        }
    }

    class C04883 implements View.OnClickListener {
        C04883() {
        }

        public void onClick(View view) {
            imageName = Preferences.getInstance(ChooseProfileActivity.this).getMobileNumber() + "-profile";
            ChooseProfileActivity.this.chooseAdharBack();
        }
    }
}
