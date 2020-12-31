package mom.vender.com.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import mom.vender.com.R;
import mom.vender.com.base.BaseActivity;
import mom.vender.com.network.volley.NetworkCallBacks.ResponseCallback;
import mom.vender.com.network.volley.WebserviceHelper;
import mom.vender.com.utils.Constants;
import mom.vender.com.utils.CustomProgressDialog;
import mom.vender.com.utils.Preferences;

public class RatingActivity extends BaseActivity {


    CircleImageView profile_civ ;
    SharedPreferences.Editor editor;
    Button terms_bt ;
    TextView comments_1,comments_2,comments_3,comments_4,comments_5,rating_tv,name_tv ;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.logo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        terms_bt = findViewById(R.id.terms_bt);
        profile_civ = findViewById(R.id.profileimage_iv);
        comments_1 = findViewById(R.id.comments_1);
        comments_2 = findViewById(R.id.comments_2);
        comments_3 = findViewById(R.id.comments_3);
        comments_4 = findViewById(R.id.comments_4);
        comments_5 = findViewById(R.id.comments_5);
        rating_tv = findViewById(R.id.userrating_tv);
        name_tv = findViewById(R.id.username_tv);
        name_tv.setText(Preferences.getInstance(this).getMobileNumber());
        fetchIdentityProofDetails();

        terms_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RatingActivity.this, ProfileActivity.class);
//                editor = mSharedPreferences.edit();
//                editor.putString("saved_rating","Saved");
//                editor.apply();
                startActivity(intent);
                finish();
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

    protected void onStart() {
        super.onStart();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private void fetchIdentityProofDetails() {
        CustomProgressDialog.getInstance(this);
        String url = Constants.BASE_URL + "api/get/rating/";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", Preferences.getInstance(this).getMobileNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebserviceHelper.getInstance().PostCall(getApplicationContext(), url, jsonObject, new ResponseCallback() {
            @Override
            public void OnSuccessFull(JSONObject Response) {
                if(Response!=null){
                   CustomProgressDialog.setDismiss();
                    try {
                        String comments1,comments2,comments3,comments4,comments5;
                        JSONObject jsonObject1 = Response.getJSONObject("Rating");
                        String rating  = jsonObject1.getString("rating");
                        JSONObject jsonObject2 = Response.getJSONObject("Data");
                        JSONArray jsonArray = jsonObject2.getJSONArray("Reveiw");
                        String imgURL = jsonObject1.getString("image_path")+jsonObject1.getString("image_name");
                        new DownLoadImageTask(profile_civ).execute(imgURL);
                        rating_tv.setText(rating);


                        for(int i= 0 ;i<jsonArray.length();i++){
                            String jsonObject3 = jsonArray.getJSONObject(i).getString("comment");
                            if(jsonObject3!=null){
                                if(i==0){
                                    comments1 = jsonArray.getJSONObject(0).getString("comment");
                                    comments_1.setText(comments1);

                                }else if(i==1){
                                    comments2 = jsonArray.getJSONObject(1).getString("comment");
                                    comments_2.setText(comments2);

                                }else if(i==2){
                                    comments3 = jsonArray.getJSONObject(2).getString("comment");
                                    comments_3.setText(comments3);

                                }else if(i==3){
                                    comments4 = jsonArray.getJSONObject(3).getString("comment");
                                    comments_4.setText(comments4);

                                }else if(i==4){
                                    comments5 = jsonArray.getJSONObject(4).getString("comment");
                                    comments_5.setText(comments5);

                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    CustomProgressDialog.setDismiss();
                }

            }
        });
    }

    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            try {
                return BitmapFactory.decodeStream(new URL(urls[0]).openStream());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(Bitmap result) {
            this.imageView.setImageBitmap(result);
           CustomProgressDialog.setDismiss();

        }
    }


}
