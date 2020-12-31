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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.util.NumberUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.common.StringUtils;

import org.greenrobot.eventbus.Subscribe;
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
import mom.vender.com.utils.Helper;
import mom.vender.com.utils.Preferences;
import mom.vender.com.utils.UploadEvent;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class AddNewOrderActivity extends BaseActivity {

    Spinner type;
    EditText itemName;
    EditText itemDescription;
    EditText itemPreparationTime;
    Spinner group;
    Button submit;
    int id;
    boolean isUpdate;
    CheckBox quarter_cb, half_cb, full_cb;
    EditText quarter_et, half_et, full_et;
    String quarterPrice = "", halfPrice = "", fullPrice = "";
    boolean quarteChecked = false, halfChecked = false, fullChecked = false;

    private static final String IMAGE_DIRECTORY_NAME = "MOM";
    private static int REQUEST_CAMERA_PIC = 11;
    private final int PICK_IMAGE_REQUEST = 71;
    TextView choose_from_librar_tv, take_photo_tv;
    ImageView edit_profile_iv;
    LinearLayout edit_ll1;
    private Uri fileUri;
    private BottomSheetBehavior mBottomSheetBehavior;
    SharedPreferences.Editor mEditor;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    LinearLayout main_rl;
    FirebaseStorage storage;
    StorageReference storageReference;
    String imageName;
    String imageUrl;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_oder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Menu Item");
        itemDescription = findViewById(R.id.itemDescription);
        itemName = findViewById(R.id.itemName);
        type = findViewById(R.id.type);
        group = findViewById(R.id.group);
        quarter_cb = findViewById(R.id.quarter_cb);
        half_cb = findViewById(R.id.half_cb);
        full_cb = findViewById(R.id.full_cb);
        quarter_et = findViewById(R.id.quarter_et);
        half_et = findViewById(R.id.half_et);
        full_et = findViewById(R.id.full_et);
        submit = findViewById(R.id.submit);
        itemPreparationTime = findViewById(R.id.item_prep_time);

        full_cb.setChecked(true);

        Intent intent = getIntent();
        this.mFirebaseInstance = FirebaseDatabase.getInstance();
        this.mFirebaseDatabase = this.mFirebaseInstance.getReference("fileAttachements");
        this.storage = FirebaseStorage.getInstance();
        this.storageReference = this.storage.getReference();
        this.edit_profile_iv = findViewById(R.id.edit_icon);
        this.edit_ll1 = findViewById(R.id.edit_ll1);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        this.main_rl = findViewById(R.id.idproof_main_ll);
        this.mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        this.mBottomSheetBehavior.setPeekHeight(0);
        this.mBottomSheetBehavior.setState(4);
        this.edit_ll1.setOnClickListener(new View.OnClickListener() {
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
                imageName = Preferences.getInstance(AddNewOrderActivity.this).getMobileNumber() + "-item";
                captureImage(imageName, REQUEST_CAMERA_PIC);
            }
        });
        this.choose_from_librar_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageName = Preferences.getInstance(AddNewOrderActivity.this).getMobileNumber() + "-item";
                chooseAdharFront();
            }
        });


        isUpdate = intent.getBooleanExtra("isUpdate", false);

        if (isUpdate) {
            getSupportActionBar().setTitle("Update Menu Item");
            id = intent.getIntExtra("id", -1);
            itemName.setText(intent.getStringExtra("itemName"));
            itemDescription.setText(intent.getStringExtra("itemDescription"));
            itemPreparationTime.setText(intent.getStringExtra("itemPreparationTime"));
//            itemPrice.setText(intent.getStringExtra("itemPrice"));
            full_et.setText(intent.getStringExtra("itemPrice"));
            if (intent.getStringExtra("food_type").equals("Veg")) {
                type.setSelection(0);
            } else {
                type.setSelection(1);
            }
            if (intent.getStringExtra("itemGroup").equals("Starter")) {
                group.setSelection(0);
            } else if (intent.getStringExtra("itemGroup").equals("Main Course")){
                group.setSelection(1);
            }else {
                group.setSelection(2);
            }
            imageUrl = intent.getStringExtra("url");

            Glide.with(this)
                    .load(imageUrl)
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .apply(RequestOptions.skipMemoryCacheOf(true))
//                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .apply(new RequestOptions().placeholder(R.drawable.default_food))
                    .into(edit_profile_iv);
        }

        quarter_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                quarteChecked = isChecked;

            }
        });

        half_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                halfChecked = isChecked;

            }
        });

        full_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fullChecked = isChecked;

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemName.getText().toString().isEmpty()) {
                    getDialog("Please enter item name");
                    return;

                } else if (itemDescription.getText().toString().isEmpty()) {
                    getDialog("Please enter item description");
                    return;

                } else if (full_et.getText().toString().isEmpty()) {
                    getDialog("Please enter item price");
                    return;

                } else if (!quarter_cb.isChecked() && (!half_cb.isChecked()) && (!full_cb.isChecked())) {
                    getDialog("Please enter atleast one Price");
                    return;
                } else if (imageUrl == null) {
                    getDialog("Please upload item image");
                    return;
                } else if (imageUrl.equals("")) {
                    getDialog("Please upload item image");
                    return;
                } else if (itemPreparationTime.getText().toString().isEmpty()) {
                    getDialog("Please enter item preparation time");
                    return;

                } else if (!itemPreparationTime.getText().toString().isEmpty()) {
                    try {
                        Double prepTime = Double.parseDouble(itemPreparationTime.getText().toString());
                    } catch (NumberFormatException ex) {
                        getDialog("Please enter valid number value for item preparation time ");
                        return;
                    }


                }

                if (quarteChecked) {
                    if (quarter_et.getText().toString().isEmpty()) {
                        getDialog("Please enter Quater Price");
                    } else {
                        quarterPrice = quarter_et.getText().toString();
                    }

                }

                if (halfChecked) {
                    if (half_et.getText().toString().isEmpty()) {
                        getDialog("Please enter Half Price");
                    } else {
                        halfPrice = half_et.getText().toString();
                    }
                }

                if (fullChecked) {
                    if (full_et.getText().toString().isEmpty()) {
                        getDialog("Please enter Full Price");
                    } else {
                        fullPrice = full_et.getText().toString();
                    }

                }

                if (quarteChecked && quarterPrice.isEmpty()) {
                    getDialog("Please enter Quater Price");
                } else {
                    if (halfChecked && halfPrice.isEmpty()) {
                        getDialog("Please enter Half Price");
                    } else {
                        if (fullChecked && fullPrice.isEmpty()) {
                            getDialog("Please enter Full Price");
                        } else {
                            if (isUpdate) {
                                editMenuItem();
                            } else {
                                addMenuItem();
                            }
                        }
                    }
                }


            }
        });
    }


    private void addMenuItem() {
        CustomProgressDialog.getInstance(this).show();
        JSONObject jsonObject = new JSONObject();
        String url = Constants.BASE_URL + "api/add/vendor/menu/";
        try {
            jsonObject.put("mobile", Preferences.getInstance(AddNewOrderActivity.this).getMobileNumber());
            jsonObject.put("food_type", type.getSelectedItem().toString());
            jsonObject.put("itemName", itemName.getText().toString());
            jsonObject.put("itemDescription", itemDescription.getText().toString());
            jsonObject.put("itemPreparationTime", Double.parseDouble(itemPreparationTime.getText().toString()));
            jsonObject.put("itemGroup", group.getSelectedItem().toString());
            jsonObject.put("half", halfChecked);
            jsonObject.put("quarter", quarteChecked);
            jsonObject.put("full", true);
            jsonObject.put("halfPrice", halfPrice);
            jsonObject.put("quarterPrice", quarterPrice);
//            jsonObject.put("fullPrice", fullPrice);
            jsonObject.put("fullPrice", full_et.getText().toString());
            jsonObject.put("item_image", imageUrl);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        WebserviceHelper.getInstance().PostCall(this, url, jsonObject, new ResponseCallback() {
            @Override
            public void OnSuccessFull(JSONObject Response) {
                System.out.println("http request : " + jsonObject);
                System.out.println("http url : " + url);
                System.out.println("http response : " + Response);

                CustomProgressDialog.setDismiss();
                if (Response != null) {
                    Toast.makeText(getApplicationContext(), "Item added successfully !", Toast.LENGTH_SHORT).show();
                    try {
                        type.setSelection(0);
                        itemName.setText("");
                        itemDescription.setText("");
                        group.setSelection(0);
                        full_et.setText("");
                        itemPreparationTime.setText("");
                        edit_profile_iv.setImageBitmap(null);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), " Some thing went wrong !", Toast.LENGTH_SHORT).show();


                }

            }
        });
    }

    private void editMenuItem() {
        CustomProgressDialog.getInstance(this).show();
        JSONObject jsonObject = new JSONObject();
        String url = Constants.BASE_URL + "api/edit/vendor/menu/";
        try {
            jsonObject.put("id", id);
            jsonObject.put("mobile", Preferences.getInstance(AddNewOrderActivity.this).getMobileNumber());
            jsonObject.put("food_type", type.getSelectedItem().toString());
            jsonObject.put("itemName", itemName.getText().toString());
            jsonObject.put("itemDescription", itemDescription.getText().toString());
            jsonObject.put("itemPreparationTime", Double.parseDouble(itemPreparationTime.getText().toString()));
            jsonObject.put("itemGroup", group.getSelectedItem().toString());
            jsonObject.put("half", halfChecked);
            jsonObject.put("quarter", quarteChecked);
            jsonObject.put("full", true);
            jsonObject.put("halfPrice", halfPrice);
            jsonObject.put("quarterPrice", quarterPrice);
//            jsonObject.put("fullPrice", fullPrice);
            jsonObject.put("fullPrice", full_et.getText().toString());
            jsonObject.put("item_image", imageUrl);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        WebserviceHelper.getInstance().PostCall(this, url, jsonObject, new ResponseCallback() {
            @Override
            public void OnSuccessFull(JSONObject Response) {
                CustomProgressDialog.setDismiss();
                if (Response != null) {
                    Toast.makeText(getApplicationContext(), "Item added successfully !", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), " Some thing went wrong !", Toast.LENGTH_SHORT).show();


                }

            }
        });
    }

    private void uploadImage(Uri filePath) {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child("menu_item/" + imageName);
            ref.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            try {
                                progressDialog.dismiss();
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }

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
            Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create MOM directory");
            return null;
        } else if (type != MEDIA_TYPE_IMAGE) {
            return null;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(mediaStorageDir.getPath());
            stringBuilder.append(File.separator);
            stringBuilder.append(imageNameUrl);
//            stringBuilder.append(".jpg");
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
//            uploadImage(fileUri);
            uploadFile(imageName, this.fileUri);
            /*fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE, imageName));
            String str = compressImage(fileUri.toString());
            Uri uri = Uri.fromFile(new File(str));
            uploadFile(imageName, uri);*/
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
           /* edit_profile_iv.setImageURI(fileUri);
            String str = compressImage(fileUri.toString());
            Uri uri = Uri.fromFile(new File(str));
            uploadFile(imageName, uri);*/
//            uploadImage(fileUri);
            uploadFile(imageName, this.fileUri);
        }
    }

    private void chooseAdharFront() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }


    @Subscribe
    public void uploadEvent(UploadEvent event) {
        imageUrl = event.getUrl();
    }


}
