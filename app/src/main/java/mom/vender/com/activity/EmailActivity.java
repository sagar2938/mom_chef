package mom.vender.com.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;

import mom.vender.com.R;

public class EmailActivity extends AppCompatActivity {

    Spinner reason;
    EditText detail;
    TextInputLayout otherInput;
    EditText other;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        getSupportActionBar().setTitle("Specify Issue");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        reason=findViewById(R.id.reason);
        detail=findViewById(R.id.detail);
        otherInput=findViewById(R.id.otherInput);
        other=findViewById(R.id.other);
        otherInput.setVisibility(View.GONE);


        reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (reason.getSelectedItem().toString().equals("Other")){
                    otherInput.setVisibility(View.VISIBLE);
                }else {
                    otherInput.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={"chef.query@motheronmission.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                if (reason.getSelectedItem().toString().equals("Other")){
                    intent.putExtra(Intent.EXTRA_SUBJECT,other.getText().toString());
                }else {
                    intent.putExtra(Intent.EXTRA_SUBJECT,reason.getSelectedItem().toString());
                }
                intent.putExtra(Intent.EXTRA_TEXT,detail.getText().toString());
//                intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
