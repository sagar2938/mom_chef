package mom.vender.com.history_invice;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;


import mom.vender.com.R;

public class SimplifiedProgressBar extends Dialog {
    private com.victor.loading.rotate.RotateLoading rotateLoading;

    public SimplifiedProgressBar(Context a) {
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
}