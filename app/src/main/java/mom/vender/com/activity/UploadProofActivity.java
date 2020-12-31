package mom.vender.com.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.ArrayList;
import java.util.List;
import mom.vender.com.R;
import mom.vender.com.base.BaseActivity;
import mom.vender.com.network.volley.NetworkCallBacks.ResponseCallback;
import mom.vender.com.network.volley.WebserviceHelper;
import mom.vender.com.utils.Constants;
import mom.vender.com.utils.CustomProgressDialog;
import mom.vender.com.utils.Preferences;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class UploadProofActivity extends BaseActivity implements View.OnClickListener {
    private static final String IMAGE_DIRECTORY_NAME = "MOM";
    private static int REQUEST_CAMERA_PIC = 11;
    private static int REQUEST_CAMERAB_PIC = 12;
    private static int REQUEST_CAMERA_PAN = 13 ;
    private static String clickType;
    public static String uniqueId;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int PICK_IMAGEB_REQUEST = 72;
    private final int PICK_IMAGE_PAN = 73;
    TextView choose_from_librar_tv,imgsecond_tv;
    Button confirmButton;
    ImageView edit_profile_iv;
    ImageView edit_profile_iv1;
    private Uri fileUri ,fileUri_back;
    String idCode;
    EditText idCode_et,dob_et;
    String idName,dobStr,genderStr;
    EditText idName_et,gender_et;
    String idType;
    String imageName;
    String imageName2;
    private List<String> lowerList;
    private BottomSheetBehavior mBottomSheetBehavior;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    LinearLayout main_rl,edit_ll2;
    Spinner schoolrange_lower_sp;
    FirebaseStorage storage;
    StorageReference storageReference;
    TextView take_photo_tv;

    class C05041 implements View.OnClickListener {
        C05041() {
        }

        @SuppressLint("WrongConstant")
        public void onClick(View v) {

            if (UploadProofActivity.this.idType.equalsIgnoreCase("Adhar Card")) {
                UploadProofActivity.clickType = "Front";
                UploadProofActivity.this.mBottomSheetBehavior.setState(3);
            } else if (UploadProofActivity.this.idType.equalsIgnoreCase("Pan Card")) {
                UploadProofActivity.clickType = "Front";
                UploadProofActivity.this.mBottomSheetBehavior.setState(3);
            }
        }
    }

    /* renamed from: com.servesimplified.partner.uploadproof.UploadProofActivity$2 */
    class C05052 implements View.OnClickListener {
        C05052() {
        }

        @SuppressLint("WrongConstant")
        public void onClick(View v) {
            UploadProofActivity.clickType = "Back";
            UploadProofActivity.this.mBottomSheetBehavior.setState(3);
        }
    }

    class C05063 implements View.OnClickListener {
        C05063() {
        }

        public void onClick(View view) {
            if (UploadProofActivity.clickType.equalsIgnoreCase("Front")) {
                if (UploadProofActivity.this.idType.equalsIgnoreCase("Adhar Card")) {
                    imageName = Preferences.getInstance(UploadProofActivity.this).getMobileNumber()+"-adhar-front";
                    captureImage(UploadProofActivity.this.imageName, UploadProofActivity.REQUEST_CAMERA_PIC);
                } else if (UploadProofActivity.this.idType.equalsIgnoreCase("Voter Id")) {
                    imageName = Preferences.getInstance(UploadProofActivity.this).getMobileNumber()+"-voter-ront";
                    captureImage(UploadProofActivity.this.imageName, UploadProofActivity.REQUEST_CAMERA_PAN);
                } else if (UploadProofActivity.this.idType.equalsIgnoreCase("Driver's license")) {
                    imageName = Preferences.getInstance(UploadProofActivity.this).getMobileNumber()+"-driver-front";
                    captureImage(UploadProofActivity.this.imageName, UploadProofActivity.REQUEST_CAMERA_PAN);
                } else if (UploadProofActivity.this.idType.equalsIgnoreCase("Pan Card")) {
                    imageName = Preferences.getInstance(UploadProofActivity.this).getMobileNumber()+"-pancard-front";
                    captureImage(UploadProofActivity.this.imageName, UploadProofActivity.REQUEST_CAMERA_PAN);
                }
            } else if (UploadProofActivity.clickType.equalsIgnoreCase("Back")) {
                if (UploadProofActivity.this.idType.equalsIgnoreCase("Adhar Card")) {
                    imageName2 = Preferences.getInstance(UploadProofActivity.this).getMobileNumber()+"-adhar-back";

                } else if (UploadProofActivity.this.idType.equalsIgnoreCase("Voter Id")) {
                    imageName2 = Preferences.getInstance(UploadProofActivity.this).getMobileNumber()+"-voter-back";

                }else if (UploadProofActivity.this.idType.equalsIgnoreCase("Pan Card")) {
                    imageName2 = Preferences.getInstance(UploadProofActivity.this).getMobileNumber()+"-pancard-back";

                }
                captureImage(UploadProofActivity.this.imageName2, UploadProofActivity.REQUEST_CAMERAB_PIC);
            }
        }
    }

    /* renamed from: com.servesimplified.partner.uploadproof.UploadProofActivity$4 */
    class C05074 implements View.OnClickListener {
        C05074() {
        }

        public void onClick(View view) {
            if (UploadProofActivity.clickType.equalsIgnoreCase("Front")) {
                if (UploadProofActivity.clickType.equalsIgnoreCase("Front")) {
                    if (UploadProofActivity.this.idType.equalsIgnoreCase("Adhar Card")) {
                        imageName = Preferences.getInstance(UploadProofActivity.this).getMobileNumber()+"-adhar-front";
                        chooseAdharFront();
                    } else if (UploadProofActivity.this.idType.equalsIgnoreCase("Voter Id")) {
                        imageName = Preferences.getInstance(UploadProofActivity.this).getMobileNumber()+"-voter-front";
                        choosePanCard();
                    } else if (UploadProofActivity.this.idType.equalsIgnoreCase("Driver's license")) {
                        imageName = Preferences.getInstance(UploadProofActivity.this).getMobileNumber()+"-driver-front";
                        choosePanCard();
                    } else if (UploadProofActivity.this.idType.equalsIgnoreCase("Pan Card")) {
                        imageName = Preferences.getInstance(UploadProofActivity.this).getMobileNumber()+"-pancard-front";
                        choosePanCard();
                    }
                } else if (UploadProofActivity.clickType.equalsIgnoreCase("Back")) {
                    if (UploadProofActivity.this.idType.equalsIgnoreCase("Adhar Card")) {
                        imageName2 = Preferences.getInstance(UploadProofActivity.this).getMobileNumber()+"-adhar-back";
                        chooseAdharBack();
                    } else if (UploadProofActivity.this.idType.equalsIgnoreCase("Voter Id")) {
                        imageName2 = Preferences.getInstance(UploadProofActivity.this).getMobileNumber()+"-voter-back";

                    }else if (UploadProofActivity.this.idType.equalsIgnoreCase("Pan Card")) {
                        imageName2 = Preferences.getInstance(UploadProofActivity.this).getMobileNumber()+"-pancard-back";

                    }
                }
            }
        }
    }

    /* renamed from: com.servesimplified.partner.uploadproof.UploadProofActivity$5 */
    class C05085 implements AdapterView.OnItemSelectedListener {
        C05085() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            UploadProofActivity.this.idType = adapterView.getItemAtPosition(i).toString();
            if (UploadProofActivity.this.idType.equalsIgnoreCase("Type of ID")) {
                UploadProofActivity.this.main_rl.setVisibility(View.GONE);
            } else {
                UploadProofActivity.this.main_rl.setVisibility(View.VISIBLE);
                if(UploadProofActivity.this.idType.equalsIgnoreCase("Adhar Card")){
                    edit_ll2.setVisibility(View.VISIBLE);
                    imgsecond_tv.setVisibility(View.VISIBLE);
                }else {
                    edit_ll2.setVisibility(View.GONE);
                    imgsecond_tv.setVisibility(View.GONE);
                }
            }
            if (view != null) {
                ((TextView) view).setTextColor(Color.parseColor("#484848"));
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }



    @SuppressLint("WrongConstant")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_proof);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mFirebaseInstance = FirebaseDatabase.getInstance();
        this.mFirebaseDatabase = this.mFirebaseInstance.getReference("fileAttachements");
        this.storage = FirebaseStorage.getInstance();
        this.storageReference = this.storage.getReference();
        this.schoolrange_lower_sp = findViewById(R.id.grade_rage_sp);
        this.edit_profile_iv = findViewById(R.id.edit_icon);
        this.edit_profile_iv1 = findViewById(R.id.edit_icon1);
        this.idCode_et = findViewById(R.id.idcode_et);
        this.idName_et = findViewById(R.id.idproof_et);
        this.dob_et = findViewById(R.id.dob_et);
        this.gender_et = findViewById(R.id.gender_et);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        this.confirmButton = findViewById(R.id.confirm_bt);
        this.main_rl = findViewById(R.id.idproof_main_ll);
        this.main_rl.setVisibility(View.GONE);
        this.confirmButton.setOnClickListener(this);
        this.mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        this.mBottomSheetBehavior.setPeekHeight(0);
        this.mBottomSheetBehavior.setState(4);
        this.edit_profile_iv.setOnClickListener(new C05041());
        this.edit_profile_iv1.setOnClickListener(new C05052());
        this.choose_from_librar_tv = findViewById(R.id.choose_from_library);
        this.take_photo_tv = findViewById(R.id.take_photo);
        imgsecond_tv = findViewById(R.id.second_img_tv);
        imgsecond_tv.setVisibility(View.GONE);
        edit_ll2 = findViewById(R.id.edit_ll2);
        edit_ll2.setVisibility(View.GONE);
        this.take_photo_tv.setOnClickListener(new C05063());
        this.choose_from_librar_tv.setOnClickListener(new C05074());
        this.lowerList = new ArrayList();
        this.lowerList.add("Type of ID");
        this.lowerList.add("Adhar Card");
        this.lowerList.add("Pan Card");
        ArrayAdapter<String> lowerrange_adapter = new ArrayAdapter(this, R.layout.spiner_state, lowerList);
        lowerrange_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.schoolrange_lower_sp.setAdapter(lowerrange_adapter);
        this.schoolrange_lower_sp.setOnItemSelectedListener(new C05085());

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
            this.fileUri = FileProvider.getUriForFile(this, "mom.vender.com" + ".provider", getOutputMediaFile(MEDIA_TYPE_IMAGE, imageNameUrl));
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
            uploadMethod(fileUri,imageName);

        }

        if (requestCode == REQUEST_CAMERAB_PIC && resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            fileUri_back = fileUri;
            options.inSampleSize = 8;
            this.edit_profile_iv1.setImageBitmap(BitmapFactory.decodeFile(this.fileUri_back.getPath(), options));
            this.mBottomSheetBehavior.setState(4);
            uploadMethod(fileUri_back,imageName2);

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
            uploadMethod(fileUri,imageName);

        }

        if (requestCode == PICK_IMAGEB_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Log.d("RequestCode", String.valueOf(requestCode));
            this.fileUri_back = data.getData();
            Log.d("RequestBack", String.valueOf(this.fileUri_back));
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), this.fileUri_back);
                this.mBottomSheetBehavior.setState(4);
                this.edit_profile_iv1.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            uploadMethod(fileUri_back,imageName2);

        }

        if (requestCode == REQUEST_CAMERA_PAN && resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            this.edit_profile_iv.setImageBitmap(BitmapFactory.decodeFile(this.fileUri.getPath(), options));
            this.mBottomSheetBehavior.setState(4);
            uploadMethod(fileUri,imageName);

        }

        if (requestCode == PICK_IMAGE_PAN && resultCode == RESULT_OK && data != null && data.getData() != null) {
            this.fileUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), this.fileUri);
                this.mBottomSheetBehavior.setState(4);
                this.edit_profile_iv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            uploadMethod(fileUri,imageName);

        }

    }

    private void chooseAdharFront() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Aadhaar back"), PICK_IMAGE_REQUEST);
    }

    private void chooseAdharBack() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Aadhaar back"), PICK_IMAGEB_REQUEST);
    }

    private void choosePanCard() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Pan Card"), PICK_IMAGE_PAN);
    }

    private void uploadProfile(final String idType, String idNumber, String front_image, String back_image, final String name,String dob,String gender) {
        CustomProgressDialog.getInstance(this);
        String url = Constants.BASE_URL + "api/upload/document/";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", Preferences.getInstance(this).getMobileNumber());
            jsonObject.put("idType", idType);
            jsonObject.put("idNumber", idNumber);
            jsonObject.put("front_image", front_image);
            jsonObject.put("back_image", back_image);
            jsonObject.put("dob",dob);
            jsonObject.put("name", name);
            jsonObject.put("gender",gender);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebserviceHelper.getInstance().PostCall(getApplicationContext(), url, jsonObject, new ResponseCallback() {
            public void OnSuccessFull(JSONObject Response) {
                if (Response != null) {


                            Preferences.getInstance(UploadProofActivity.this).setKeyIdType(idType);
                            Intent intent = new Intent(UploadProofActivity.this,UploadIdentityActivity.class);
                            startActivity(intent);
                            finish();

                    CustomProgressDialog.setDismiss();
                }else {
                    CustomProgressDialog.setDismiss();
                }
            }
        });
    }


    public void onClick(View v) {
        if (v.getId() == R.id.confirm_bt) {
            if(UploadProofActivity.this.idType.equalsIgnoreCase("Adhar Card")){
                imageName =  Preferences.getInstance(this).getMobileNumber()+"-adhar-front";
                imageName2 =  Preferences.getInstance(this).getMobileNumber()+"-adhar-back";
            }else if(UploadProofActivity.this.idType.equalsIgnoreCase("Pan Card")){
                imageName =  Preferences.getInstance(this).getMobileNumber()+"-pancard-front";
                imageName2 =  "";
            }
            this.idName = this.idName_et.getText().toString();
            this.idCode = this.idCode_et.getText().toString();
            this.dobStr = this.dob_et.getText().toString();
            this.genderStr = this.gender_et.getText().toString();
            if(idCode.trim().isEmpty()){
                Toast.makeText(getApplicationContext(),"Please enter id number",Toast.LENGTH_SHORT).show();
            }else if(idName.trim().isEmpty()){
                Toast.makeText(getApplicationContext(),"Please enter name",Toast.LENGTH_SHORT).show();

            }else if(dobStr.trim().isEmpty()){
                Toast.makeText(getApplicationContext(),"Please enter DOB",Toast.LENGTH_SHORT).show();

            }else if(genderStr.trim().isEmpty()){
                Toast.makeText(getApplicationContext(),"Please enter gender",Toast.LENGTH_SHORT).show();

            }
            else if(fileUri==null) {
                Toast.makeText(getApplicationContext(), "Please select image", Toast.LENGTH_SHORT).show();
            }else {
                uploadProfile(this.idType, this.idCode, this.imageName, this.imageName2, this.idName,dobStr,genderStr);
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
