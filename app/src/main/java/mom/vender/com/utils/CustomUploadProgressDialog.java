package mom.vender.com.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import mom.vender.com.R;


/**
 * Created by silence12 on 19/8/17.
 */

public class CustomUploadProgressDialog extends Dialog {
    static CustomUploadProgressDialog progressDialog;
    private com.victor.loading.rotate.RotateLoading rotateLoading;
    static TextView progress;

    public CustomUploadProgressDialog(Context a) {
        super(a);
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setContentView(R.layout.custom_progress_dialog_upload);
        this.rotateLoading = findViewById(R.id.loading_spinner);
        progress = findViewById(R.id.progress);
        this.rotateLoading.start();
    }

    public static CustomUploadProgressDialog getInstance(Activity activity) {
        progressDialog = new CustomUploadProgressDialog(activity);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public static CustomUploadProgressDialog getInstance(Activity activity, boolean b) {
        progressDialog = new CustomUploadProgressDialog(activity);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public static void setProgress(String string){
        progress.setText(string);
    }

    public static void setDismiss() {
        try {
            progressDialog.dismiss();
        }catch (Exception e){

        }
    }



}
