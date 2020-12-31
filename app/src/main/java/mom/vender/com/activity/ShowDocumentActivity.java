package mom.vender.com.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;



import java.net.URL;

import mom.vender.com.R;
import mom.vender.com.base.BaseActivity;
import mom.vender.com.utils.CustomProgressDialog;
import mom.vender.com.utils.ZoomageView;


public class ShowDocumentActivity extends BaseActivity {

    ZoomageView zoomageView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_document);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        zoomageView = findViewById(R.id.image_zoom);
        String url = getIntent().getStringExtra("ImgUrl");
        String idNumber = getIntent().getStringExtra("idNumber");
        setTitle(idNumber);
        CustomProgressDialog.getInstance(this);
        new DownLoadImageTask(zoomageView).execute(url);

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

    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            try {
                return BitmapFactory.decodeStream(new URL(urldisplay).openStream());
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
