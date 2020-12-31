package mom.vender.com.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import mom.vender.com.R;


/**
 * Created by silence12 on 19/8/17.
 */

public class CustomProgressDialog extends Dialog {
    static CustomProgressDialog progressDialog;
    private com.victor.loading.rotate.RotateLoading rotateLoading;

    public CustomProgressDialog(Context a) {
        super(a);
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setContentView(R.layout.custom_progress_dialog);
        this.rotateLoading = findViewById(R.id.loading_spinner);
        this.rotateLoading.start();
    }

    public static CustomProgressDialog getInstance(Activity activity) {
        progressDialog = new CustomProgressDialog(activity);
        progressDialog.setCancelable(true);
        return progressDialog;
    }

    public static CustomProgressDialog getInstance(Activity activity,boolean b) {
        progressDialog = new CustomProgressDialog(activity);
        progressDialog.setCancelable(true);
        return progressDialog;
    }

    public static void setDismiss() {
        try {
            progressDialog.dismiss();
        }catch (Exception e){

        }
    }



}
