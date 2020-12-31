package mom.vender.com.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import de.hdodenhof.circleimageview.CircleImageView;
import mom.vender.com.R;
import mom.vender.com.base.BaseActivity;
import mom.vender.com.fragment.AddMenuFragment;
import mom.vender.com.fragment.MenuFragment;
import mom.vender.com.fragment.NewLeadFragment;
import mom.vender.com.fragment.OnGoingFragment;
import mom.vender.com.fragment.ProfileFragment;
import mom.vender.com.network.ApiCallService;
import mom.vender.com.network.ThisApp;
import mom.vender.com.network.request.EventPushRequest;
import mom.vender.com.network.response.ChangeStatusResponse;
import mom.vender.com.network.response.GetStatusResponse;
import mom.vender.com.network.response.SuccessResponse;
import mom.vender.com.utils.Preferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.suke.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends BaseActivity {

    boolean doubleBackToExitPressedOnce = false;
    int onlineStatus;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    MainActivity.this.setTitle("  Ongoing");
                    fragment = new OnGoingFragment();
                    break;
                case R.id.navigation_home:
                    MainActivity.this.setTitle("  New Leads ");
                    fragment = new NewLeadFragment();
                    break;
                case R.id.navigation_notifications:
                    MainActivity.this.setTitle("  Profile ");
                    fragment = new ProfileFragment();
                    break;
                case R.id.navigation_target:
                    MainActivity.this.setTitle("  Menu  ");
                    fragment = new MenuFragment();
                    break;
                default:
                    break;
            }
            return MainActivity.this.loadFragment(fragment);
        }
    };


    LinearLayout home, search, order, profile, cart;
    LinearLayout homeBar, searchBar, orderBar, profileBar, cartBar;
    TextView homeTxt, searchTxt, orderTxt, profileTxt, cartTxt;
    ImageView homeIcon, searchIcon, orderIcon, profileIcon, cartIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_partner);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("MOMCHEF");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        home = findViewById(R.id.home);
        search = findViewById(R.id.search);
        order = findViewById(R.id.order);
        profile = findViewById(R.id.profile);
        homeBar = findViewById(R.id.homeBar);
        searchBar = findViewById(R.id.searchBar);
        orderBar = findViewById(R.id.orderBar);
        profileBar = findViewById(R.id.profileBar);
        homeIcon = findViewById(R.id.homeIcon);
        searchIcon = findViewById(R.id.searchIcon);
        orderIcon = findViewById(R.id.orderIcon);
        profileIcon = findViewById(R.id.profileIcon);
        homeTxt = findViewById(R.id.homeTxt);
        searchTxt = findViewById(R.id.searchTxt);
        orderTxt = findViewById(R.id.orderTxt);
        profileTxt = findViewById(R.id.profileTxt);
        cart = findViewById(R.id.cart);
        cartBar = findViewById(R.id.cartBar);
        cartTxt = findViewById(R.id.cartTxt);
        cartIcon = findViewById(R.id.cartIcon);


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setBottomView("cart");
                fragmentSwitching(new AddMenuFragment());


//                Intent intent = new Intent(getApplicationContext(), AddNewOrderActivity.class);
//                intent.putExtra("isUpdate", false);
//                startActivity(intent);
            }
        });


        loadFragment(new NewLeadFragment());
        setBottomView("lead");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBottomView("lead");
                fragmentSwitching(new NewLeadFragment());
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBottomView("on");
                fragmentSwitching(new OnGoingFragment());
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBottomView("menu");
                fragmentSwitching(new MenuFragment());

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBottomView("profile");
                fragmentSwitching(new ProfileFragment());

            }
        });


        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Map map = new HashMap();
                map.put("mobile", Preferences.getInstance(getApplicationContext()).getMobileNumber());
                map.put("fcmToken", newToken);
                map.put("userType", "Vendor");
                System.out.println("fcmToken"+newToken);
                ThisApp.getApi(getApplicationContext()).saveToken(map).enqueue(new Callback<SuccessResponse>() {
                    @Override
                    public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                        System.out.println("http : Token saved");
//                        Toast.makeText(MainActivity.this, "Token saved", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<SuccessResponse> call, Throwable t) {
                        System.out.println("http : Token saved failed");
//                        Toast.makeText(MainActivity.this, "Token saved failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        

       /* ThisApp.getApi(this).checkLicence(map).enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
            }
        });*/

//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            String data = bundle.getString("name");
//            Log.e("FCMMessage", "From: " + data);
//            //bundle must contain all info sent in "data" field of the notification
//        }

        if(getIntent().getIntExtra("FROM",-1) == 3) {
            loadIncomingOrderUi();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        Map map = new HashMap();
        map.put("mobile", Preferences.getInstance(getApplicationContext()).getMobileNumber());
//        ApiCallService.action2(this,map,ApiCallService.Action.ACTION_CHECK_LICENCE);

        ApiCallService.action2(MainActivity.this, map, ApiCallService.Action.ACTION_GET_STATUS);
    }

    void setBottomView(String value) {
        if (value.equals("lead")) {
            homeBar.setBackgroundColor(getResources().getColor(R.color.white));
            searchBar.setBackgroundColor(getResources().getColor(R.color.white));
            orderBar.setBackgroundColor(getResources().getColor(R.color.white));
            profileBar.setBackgroundColor(getResources().getColor(R.color.white));
            cartBar.setBackgroundColor(getResources().getColor(R.color.white));

            homeTxt.setTextColor(getResources().getColor(R.color.black));
            searchTxt.setTextColor(getResources().getColor(R.color.black));
            orderTxt.setTextColor(getResources().getColor(R.color.black));
            profileTxt.setTextColor(getResources().getColor(R.color.black));
            cartTxt.setTextColor(getResources().getColor(R.color.black));

            homeTxt.setTypeface(null, Typeface.NORMAL);
            searchTxt.setTypeface(null, Typeface.NORMAL);
            orderTxt.setTypeface(null, Typeface.NORMAL);
            profileTxt.setTypeface(null, Typeface.NORMAL);
            cartTxt.setTypeface(null, Typeface.NORMAL);


            homeBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            homeTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
            homeTxt.setTypeface(null, Typeface.BOLD);


        } else if (value.equals("on")) {
            homeBar.setBackgroundColor(getResources().getColor(R.color.white));
            searchBar.setBackgroundColor(getResources().getColor(R.color.white));
            orderBar.setBackgroundColor(getResources().getColor(R.color.white));
            profileBar.setBackgroundColor(getResources().getColor(R.color.white));
            cartBar.setBackgroundColor(getResources().getColor(R.color.white));

            homeTxt.setTextColor(getResources().getColor(R.color.black));
            searchTxt.setTextColor(getResources().getColor(R.color.black));
            orderTxt.setTextColor(getResources().getColor(R.color.black));
            profileTxt.setTextColor(getResources().getColor(R.color.black));
            cartTxt.setTextColor(getResources().getColor(R.color.black));

            homeTxt.setTypeface(null, Typeface.NORMAL);
            searchTxt.setTypeface(null, Typeface.NORMAL);
            orderTxt.setTypeface(null, Typeface.NORMAL);
            profileTxt.setTypeface(null, Typeface.NORMAL);
            cartTxt.setTypeface(null, Typeface.NORMAL);


            searchBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            searchTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
            searchTxt.setTypeface(null, Typeface.BOLD);


        } else if (value.equals("menu")) {
            homeBar.setBackgroundColor(getResources().getColor(R.color.white));
            searchBar.setBackgroundColor(getResources().getColor(R.color.white));
            orderBar.setBackgroundColor(getResources().getColor(R.color.white));
            profileBar.setBackgroundColor(getResources().getColor(R.color.white));
            cartBar.setBackgroundColor(getResources().getColor(R.color.white));

            homeTxt.setTextColor(getResources().getColor(R.color.black));
            searchTxt.setTextColor(getResources().getColor(R.color.black));
            orderTxt.setTextColor(getResources().getColor(R.color.black));
            profileTxt.setTextColor(getResources().getColor(R.color.black));
            cartTxt.setTextColor(getResources().getColor(R.color.black));

            homeTxt.setTypeface(null, Typeface.NORMAL);
            searchTxt.setTypeface(null, Typeface.NORMAL);
            orderTxt.setTypeface(null, Typeface.NORMAL);
            profileTxt.setTypeface(null, Typeface.NORMAL);
            cartTxt.setTypeface(null, Typeface.NORMAL);


            orderBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            orderTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
            orderTxt.setTypeface(null, Typeface.BOLD);


        } else if (value.equals("profile")) {
            homeBar.setBackgroundColor(getResources().getColor(R.color.white));
            searchBar.setBackgroundColor(getResources().getColor(R.color.white));
            orderBar.setBackgroundColor(getResources().getColor(R.color.white));
            profileBar.setBackgroundColor(getResources().getColor(R.color.white));
            cartBar.setBackgroundColor(getResources().getColor(R.color.white));

            homeTxt.setTextColor(getResources().getColor(R.color.black));
            searchTxt.setTextColor(getResources().getColor(R.color.black));
            orderTxt.setTextColor(getResources().getColor(R.color.black));
            profileTxt.setTextColor(getResources().getColor(R.color.black));
            cartTxt.setTextColor(getResources().getColor(R.color.black));


            homeTxt.setTypeface(null, Typeface.NORMAL);
            searchTxt.setTypeface(null, Typeface.NORMAL);
            orderTxt.setTypeface(null, Typeface.NORMAL);
            profileTxt.setTypeface(null, Typeface.NORMAL);
            cartTxt.setTypeface(null, Typeface.NORMAL);


            profileBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            profileTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
            profileTxt.setTypeface(null, Typeface.BOLD);


        } else if (value.equals("cart")) {
            homeBar.setBackgroundColor(getResources().getColor(R.color.white));
            searchBar.setBackgroundColor(getResources().getColor(R.color.white));
            orderBar.setBackgroundColor(getResources().getColor(R.color.white));
            profileBar.setBackgroundColor(getResources().getColor(R.color.white));
            cartBar.setBackgroundColor(getResources().getColor(R.color.white));

            homeTxt.setTextColor(getResources().getColor(R.color.black));
            searchTxt.setTextColor(getResources().getColor(R.color.black));
            orderTxt.setTextColor(getResources().getColor(R.color.black));
            profileTxt.setTextColor(getResources().getColor(R.color.black));
            cartTxt.setTextColor(getResources().getColor(R.color.black));


            homeTxt.setTypeface(null, Typeface.NORMAL);
            searchTxt.setTypeface(null, Typeface.NORMAL);
            orderTxt.setTypeface(null, Typeface.NORMAL);
            profileTxt.setTypeface(null, Typeface.NORMAL);
            cartTxt.setTypeface(null, Typeface.NORMAL);


            cartBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            cartTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
            cartTxt.setTypeface(null, Typeface.BOLD);


        }

    }


    private boolean loadFragment(Fragment fragment) {
        if (fragment == null) {
            return false;
        }

        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }


        return true;
    }

    MenuItem item;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        item = menu.findItem(R.id.button_item);
        final SwitchButton switchButton = item.getActionView().findViewById(R.id.switch_button);
        switchButton.setChecked(true);
        switchButton.isChecked();
        switchButton.toggle();     //switch state
        switchButton.toggle(false);//switch without animation
        switchButton.setShadowEffect(true);//disable shadow effect
        switchButton.setEnabled(true);//disable button
        switchButton.setEnableEffect(false);//disable the switch animation
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                Map map = new Hashtable();
                map.put("mobile", Preferences.getInstance(getApplicationContext()).getMobileNumber());
                if (onlineStatus == 0) {
                    map.put("status", 1);
                } else {
                    map.put("status", 0);
                }
                ApiCallService.action(MainActivity.this, map, ApiCallService.Action.ACTION_CHANGE_STATUS);

            }
        });
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        Map map = new HashMap();
        map.put("mobile", Preferences.getInstance(getApplicationContext()).getMobileNumber());
        if (onlineStatus == 0) {
            map.put("status", 1);
        } else {
            map.put("status", 0);
        }
        ApiCallService.action(MainActivity.this, map, ApiCallService.Action.ACTION_CHANGE_STATUS);
        return false;
    }


    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }



    /*@Subscribe
    public void checkLicence(SuccessResponse response){
        if (response.getResponse().getConfirmation()==0){
            getDialog("Please Buy a food licence");
        }
    }*/

    @Subscribe
    public void changeStatus(ChangeStatusResponse response) {
        if (onlineStatus == 0) {
            onlineStatus = 1;
        } else {
            onlineStatus = 1;
        }
    }

    @Subscribe
    public void getStatus(GetStatusResponse response) {
        onlineStatus = response.getResponse().getStatusInt();
        final SwitchButton switchButton = item.getActionView().findViewById(R.id.switch_button);
        if (onlineStatus == 1) {
            switchButton.setChecked(true);
        } else {
            switchButton.setChecked(false);
        }

    }

    private void loadIncomingOrderUi() {

        String orderIdFCM = Objects.requireNonNull(getIntent().getExtras()).getString("orderId");
        String customerMobile = Objects.requireNonNull(getIntent().getExtras()).getString("customerMobile");

        final MaterialDialog materialDialog;
        final MediaPlayer mediaPlayer;

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.carnatic);
        mediaPlayer.start();

        materialDialog = new MaterialDialog.Builder(this)
                .autoDismiss(true).customView(R.layout.new_order_fcm, true)
                .cancelable(false)
                .positiveColor(getResources().getColor(R.color.darkGreen))
                .negativeColor(getResources().getColor(R.color.colorPrimaryDark))

                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {


                    }
                }).build();
        View view = materialDialog.getCustomView();

        TextView name = view.findViewById(R.id.name);
        TextView totalAmount = view.findViewById(R.id.totalAmount);
        TextView address = view.findViewById(R.id.address);
        TextView note = view.findViewById(R.id.note);
        TextView promo = view.findViewById(R.id.promo);
        TextView orderId = view.findViewById(R.id.orderId);

        LinearLayout bPositive = view.findViewById(R.id.accept);
        CircleImageView circleImageView = view.findViewById(R.id.profileimage_iv);
        LinearLayout bNegative = view.findViewById(R.id.cancel);
        name.setText(getIntent().getStringExtra("customerName"));
        orderId.setText(Objects.requireNonNull(getIntent().getExtras()).getString("orderId"));
        totalAmount.setText(getIntent().getStringExtra("totalAmount"));
        address.setText(getIntent().getStringExtra("address"));
        note.setText(getIntent().getStringExtra("note"));
        promo.setText(getIntent().getStringExtra("promo"));

        bPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.dismiss();
                mediaPlayer.stop();
                acceptOrder( orderIdFCM,customerMobile);

            }


        });

        bNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                new MaterialDialog.Builder(MainActivity.this)
                        .title(R.string.confirm)
                        .content(R.string.please_confirm)
                        .positiveText(R.string.confirm1)
                        .negativeText(R.string.cancel)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                materialDialog.dismiss();
                                cancelOrder(orderIdFCM,customerMobile);

                            }
                        })
                        .negativeColor(getResources().getColor(R.color.colorPrimaryDark))
                        .positiveColor(getResources().getColor(R.color.darkGreen))
                        .show();

            }
        });

        materialDialog.show();


    }


    public void getDialog(String tittle, String message,String orderId,String mobile) {
        new AlertDialog.Builder(getApplicationContext())
                .setTitle(tittle)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Map map=new HashMap();
                        map.put("orderId",orderId);
                        ApiCallService.action(MainActivity.this, map, ApiCallService.Action.ACTION_CANCEL_ORDER);
                        EventBus.getDefault().post(new EventPushRequest(mobile,"Your order no- "+orderId +" has been cancelled by MOM Chef"));

                    }
                })
                .setNegativeButton("No", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

    private void acceptOrder(String orderId,String mobile){
        Map map=new HashMap();
        map.put("orderId",orderId);
        map.put("mom_mobile", Preferences.getInstance(MainActivity.this).getMobileNumber());
        ApiCallService.action(MainActivity.this, map, ApiCallService.Action.ACTION_ACCEPT_ORDER);
        EventBus.getDefault().post(new EventPushRequest(mobile,"Your order has been accepted by MOMCHEF!"));
    }

    private void cancelOrder(String orderId,String mobile){
        Map map=new HashMap();
        map.put("orderId",orderId);
        ApiCallService.action(MainActivity.this, map, ApiCallService.Action.ACTION_CANCEL_ORDER);
        EventBus.getDefault().post(new EventPushRequest(mobile,"Your order no- "+orderId +" has been cancelled by MOM Chef"));

    }


}
