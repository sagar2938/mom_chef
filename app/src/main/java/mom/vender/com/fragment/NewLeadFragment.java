package mom.vender.com.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import de.hdodenhof.circleimageview.CircleImageView;
import mom.vender.com.R;
import mom.vender.com.activity.ContactAdminActivity;
import mom.vender.com.activity.MainActivity;
import mom.vender.com.adapter.NewLeadAdapter;
import mom.vender.com.adapter.TodaySpecialDialogPlusAdapter;
import mom.vender.com.base.BaseFragment;
import mom.vender.com.network.ApiCallService;
import mom.vender.com.network.ThisApp;
import mom.vender.com.network.request.EventPushRequest;
import mom.vender.com.network.response.AddResponse;
import mom.vender.com.network.response.GetNewOrderResponse;
import mom.vender.com.network.response.SuccessResponse;
import mom.vender.com.network.response.menu_item.GetOrderMenuResponse;
import mom.vender.com.network.response.menu_item.Menu_data;
import mom.vender.com.utils.Helper;
import mom.vender.com.utils.Preferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewLeadFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_lead_new_total, null);
        recyclerView=view.findViewById(R.id.recyclerView);
        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Map map=new HashMap();
        map.put("mobile", Preferences.getInstance(getContext()).getMobileNumber());
        ApiCallService.action(getActivity(),map,ApiCallService.Action.ACTION_GET_NEW_LEAD);


        ThisApp.getApi(getActivity()).checkLicence(map).enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                try {
                    if (response.body().getResponse().getConfirmation() == 0) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Food license not available")
                                .setMessage("Please submit a food license as soon as possible")
                                .setCancelable(true)
                                .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new Intent(getActivity(), ContactAdminActivity.class));
                                    }
                                })
                                .setIcon(R.mipmap.ic_launcher)
                                .show();
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {

            }
        });


        if (!checkPermissions()) {
            requestPermission();
        }
    }

    @Subscribe
    public void getNewLead(GetNewOrderResponse response){
        swipeRefreshLayout.setRefreshing(false);
        if (response.getSuccess()){
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new NewLeadAdapter(getActivity(),response.getOrderData()));

            if (!Preferences.getInstance(getContext()).getTodayDate().equals(Helper.getCurrentDate())){
                Map map=new Hashtable();
                map.put("mobile",Preferences.getInstance(getActivity()).getMobileNumber());
                ApiCallService.action2(getActivity(),map,ApiCallService.Action.ACTION_MENU_LIST);
            }

            if (response.getOrderData().size()>0){
                loadIncomingOrderUi(response);
            }

        }else {
            getDialog("Something went wrong");
        }

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        Map map=new HashMap();
        map.put("mobile", Preferences.getInstance(getContext()).getMobileNumber());
        ApiCallService.action(getActivity(),map,ApiCallService.Action.ACTION_GET_NEW_LEAD);
    }


    @Subscribe
    public void AcceptCancel(SuccessResponse response){
        if (response.getResponse().getConfirmation()==1) {
            getDialogSuccess(response.getResponse().getOrderId()+" "+response.getResponse().getMessage());
            onRefresh();
        }else {
            getDialogSorry();
        }

    }




    @Subscribe
    public void getMenuList(GetOrderMenuResponse response) {
        if (response.getMenu_data()!=null) {
            if (response.getMenu_data().size() > 0) {
                dialogTodaySpecialTime(response.getMenu_data());
            }
        }
    }


    void dialogTodaySpecialTime(List<Menu_data> menuDataList){
        DialogPlus dialogPlus = null;
        DialogPlusBuilder dialogPlusBuilder = DialogPlus.newDialog(getActivity());
        dialogPlusBuilder.setHeader(R.layout.today_spcial_dialog);
        dialogPlusBuilder.setContentHolder(new GridHolder(1));
        dialogPlusBuilder.setGravity(Gravity.BOTTOM);
        dialogPlusBuilder.setCancelable(true);

        dialogPlusBuilder.setMargin(5, 300, 5, 160);
        dialogPlusBuilder.setPadding(0, 0, 0, 10);
        dialogPlusBuilder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(DialogPlus dialog, View view) {
                Log.e(" inside", " dialog is dismiss issssssssssssssss");
                view.findViewById(R.id.footer_close_button);
                dialog.dismiss();
            }
        });

        dialogPlusBuilder.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {

                Map map=new Hashtable();
                map.put("mobile",Preferences.getInstance(getContext()).getMobileNumber());
                map.put("menuItem",menuDataList.get(position).getId());
                map.put("date",Helper.getCurrentDate());
                ApiCallService.action(getActivity(),map,ApiCallService.Action.ADD_SPECIAL_ITEM);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                },100);

            }
        });
//        dialogPlusBuilder.setFooter(R.layout.footer);
        dialogPlusBuilder.setExpanded(false); // This will enable the expand feature, (similar to android L share dialog)
        dialogPlusBuilder.setAdapter(new TodaySpecialDialogPlusAdapter(getActivity(), menuDataList));
        dialogPlus = dialogPlusBuilder.create();
        dialogPlus.show();
    }


    @Subscribe
    public void addSpecial(AddResponse response) {
        if (response.getResponse().getConfirmation()==1){
            Toast.makeText(getActivity(), ""+response.getResponse().getMessage(), Toast.LENGTH_SHORT).show();
            Preferences.getInstance(getActivity()).setTodayDate(Helper.getCurrentDate());
        }

    }




    String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getActivity(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(),
                permissions, 100);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private void loadIncomingOrderUi(GetNewOrderResponse response) {


        final MaterialDialog materialDialog;
        final MediaPlayer mediaPlayer;

        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.carnatic);
        mediaPlayer.start();

        materialDialog = new MaterialDialog.Builder(getActivity())
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
        name.setText(response.getOrderData().get(0).getName());
        orderId.setText(response.getOrderData().get(0).getOrderId());
        totalAmount.setText(response.getOrderData().get(0).getTotalPrice());
        address.setText(response.getOrderData().get(0).getLocation());
        note.setText(response.getOrderData().get(0).getNote());
        promo.setText(response.getOrderData().get(0).getPromoCode());

        bPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.dismiss();
                mediaPlayer.stop();
                Map map=new HashMap();
                map.put("orderId",response.getOrderData().get(0).getOrderId());
                map.put("mom_mobile", Preferences.getInstance(getActivity()).getMobileNumber());
                ApiCallService.action(getActivity(), map, ApiCallService.Action.ACTION_ACCEPT_ORDER);
            }
        });

        bNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.confirm)
                        .content(R.string.please_confirm)
                        .positiveText(R.string.confirm1)
                        .negativeText(R.string.cancel)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                materialDialog.dismiss();
                                mediaPlayer.stop();
                                getDialog(response,"Cancel","Are you sure you want to cancel this order?",0);
                            }
                        })
                        .negativeColor(getResources().getColor(R.color.colorPrimaryDark))
                        .positiveColor(getResources().getColor(R.color.darkGreen))
                        .show();

            }
        });

        materialDialog.show();


    }


    public void getDialog(GetNewOrderResponse response,String tittle, String message,Integer position) {
        new AlertDialog.Builder(getActivity())
                .setTitle(tittle)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Map map=new HashMap();
                        map.put("orderId",response.getOrderData().get(0).getOrderId());
                        ApiCallService.action(getActivity(), map, ApiCallService.Action.ACTION_CANCEL_ORDER);
                        EventBus.getDefault().post(new EventPushRequest(response.getOrderData().get(0).getMobile(),"Your order no "+response.getOrderData().get(0).getOrderId() +" has been cancelled by MOM Chef"));

                    }
                })
                .setNegativeButton("No", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

}
