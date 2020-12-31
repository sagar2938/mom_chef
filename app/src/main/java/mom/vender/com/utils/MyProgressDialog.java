package mom.vender.com.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

public class MyProgressDialog {
    static Activity context;
   static ProgressDialog progressDialog;
    public static void  getInstance(Activity context){
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

    public static void  setDismiss(){
        progressDialog.dismiss();
    }
}
