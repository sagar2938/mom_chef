package mom.vender.com.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
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
import mom.vender.com.R;
import mom.vender.com.base.BaseActivity;
import mom.vender.com.network.volley.NetworkCallBacks.ResponseCallback;
import mom.vender.com.network.volley.WebserviceHelper;
import mom.vender.com.utils.Constants;
import mom.vender.com.utils.CustomUploadProgressDialog;
import mom.vender.com.utils.Preferences;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class AwardVerification extends BaseActivity {
    private static final String IMAGE_DIRECTORY_NAME = "MOM";
    public static String uniqueId;
    private static int REQUEST_CAMERA_PIC = 11;
    private static String clickType;
    private final int PICK_IMAGE_REQUEST = 71;
    ImageView edit_profile_iv, edit_icon1, edit_icon2, edit_icon3;
    FirebaseStorage storage;
    StorageReference storageReference;
    RelativeLayout upload_rl;
    TextView choose_from_librar_tv, take_photo_tv;
    LinearLayout edit_ll2, edit_ll3, edit_ll4;
    CardView upload_ll;
    String imageName;
    byte[] decoded;
    private BottomSheetBehavior mBottomSheetBehavior;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;
    private Uri fileUri;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awardverification);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mFirebaseInstance = FirebaseDatabase.getInstance();
        this.mFirebaseDatabase = this.mFirebaseInstance.getReference("fileAttachements");
        this.storage = FirebaseStorage.getInstance();
        this.storageReference = this.storage.getReference();
        View bottomSheet = findViewById(R.id.bottom_sheet);
        init();
        this.mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        this.mBottomSheetBehavior.setPeekHeight(0);
        this.mBottomSheetBehavior.setState(4);

    }

    private void init() {
        upload_rl = findViewById(R.id.edit_ll1);
        upload_ll = findViewById(R.id.award_upload_bt);
        edit_icon1 = findViewById(R.id.edit_icon1);
        edit_profile_iv = findViewById(R.id.edit_icon);
        this.choose_from_librar_tv = findViewById(R.id.choose_from_library);
        this.take_photo_tv = findViewById(R.id.take_photo);
        edit_ll2 = findViewById(R.id.edit_ll2);
        edit_ll3 = findViewById(R.id.edit_ll3);
        edit_ll4 = findViewById(R.id.edit_ll4);
        edit_ll2.setVisibility(View.GONE);
        edit_ll3.setVisibility(View.GONE);
        edit_ll4.setVisibility(View.GONE);

        fetchProfile();
        upload_rl.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(3);
            }
        });

        take_photo_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageName = Preferences.getInstance(getApplicationContext()).getMobileNumber() + "_award_1";
                captureImage(imageName, REQUEST_CAMERA_PIC);
            }
        });

        choose_from_librar_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageName = Preferences.getInstance(getApplicationContext()).getMobileNumber() + "_award_1";
                chooseAdharBack();
            }
        });

        upload_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RequestUri", String.valueOf(fileUri));
                if (decoded != null) {
                    uploadProfile(decoded, imageName);
                } else {
                    Toast.makeText(getApplicationContext(), "Upload photo first", Toast.LENGTH_SHORT).show();
                }
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

    @SuppressLint("WrongConstant")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA_PIC && resultCode == RESULT_OK) {
            edit_ll2.setVisibility(View.VISIBLE);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(this.fileUri.getPath(), options);
            this.edit_icon1.setImageBitmap(bitmap);
            this.mBottomSheetBehavior.setState(4);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            decoded = out.toByteArray();

        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Log.d("RequestCode", String.valueOf(requestCode));
            this.fileUri = data.getData();
            edit_ll2.setVisibility(View.VISIBLE);
            Log.d("RequestBack", String.valueOf(this.fileUri));
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), this.fileUri);
                this.mBottomSheetBehavior.setState(4);
                this.edit_icon1.setImageBitmap(bitmap);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                decoded = out.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void chooseAdharBack() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Aadhaar back"), PICK_IMAGE_REQUEST);
    }

    private void uploadProfile(final byte[] fileUri, String front_image) {
        CustomUploadProgressDialog.getInstance(this);
        String url = Constants.BASE_URL + "api/upload/award/";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", Preferences.getInstance(this).getMobileNumber());
            jsonObject.put("image_name", front_image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebserviceHelper.getInstance().PostCall(getApplicationContext(), url, jsonObject, new ResponseCallback() {
            public void OnSuccessFull(JSONObject Response) {
                Log.d("AwardResponse", String.valueOf(Response));
                if (Response != null) {
                    CustomUploadProgressDialog.setDismiss();
                    uploadImage(fileUri);
                } else {
                    CustomUploadProgressDialog.setDismiss();
                }
            }
        });
    }

    private void uploadImage(byte[] filePath) {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child("idproof/" + imageName);
            ref.putBytes(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(AwardVerification.this, ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void fetchProfile() {
        CustomUploadProgressDialog.getInstance(this);
        String url = Constants.BASE_URL + "api/get/user/award/";
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
                    CustomUploadProgressDialog.setDismiss();

                    try {
                        JSONObject jsonObject1 = Response.getJSONObject("Response");
                        String imgURL = jsonObject1.getString("image_path") + jsonObject1.getString("image_name");
                        Log.d("ImageUrlCheck", imgURL);
                        if (jsonObject1.getString("image_name") == null) {
                            edit_ll2.setVisibility(View.GONE);

                        } else {
                            edit_ll2.setVisibility(View.VISIBLE);

                        }
                        if(!imgURL.isEmpty()) {
                            Picasso.get().load(imgURL)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                    .into(edit_icon1);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                } else {
                    CustomUploadProgressDialog.setDismiss();
                }
            }
        });
    }


}
