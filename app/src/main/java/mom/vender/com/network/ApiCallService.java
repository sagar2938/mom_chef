package mom.vender.com.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import mom.vender.com.network.request.EventPushRequest;
import mom.vender.com.network.response.AddResponse;
import mom.vender.com.network.response.ChangeStatusResponse;
import mom.vender.com.network.response.GetNewOrderResponse;
import mom.vender.com.network.response.GetProfileResponse;
import mom.vender.com.R;
import mom.vender.com.network.request.RequestSignUp;
import mom.vender.com.network.response.GetStatusResponse;
import mom.vender.com.network.response.HistoryResponse;
import mom.vender.com.network.response.PushNotificationResponse;
import mom.vender.com.network.response.SignUpResponse;
import mom.vender.com.network.response.SuccessResponse;
import mom.vender.com.network.response.menu_item.GetOrderMenuResponse;
import mom.vender.com.utils.CustomProgressDialog;
import mom.vender.com.utils.Helper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCallService extends IntentService {

    static Object request;
    static Activity context;


    public ApiCallService() {
        super("ApiCallService");
    }


    public static void action(Activity ctx, String action) {
        Intent intent = new Intent(ctx, ApiCallService.class);
        intent.setAction(action);
        ctx.startService(intent);
        context = ctx;
    }


    public static void action(Activity ctx, Object request, String action) {
        context = ctx;
        if (!Helper.isNetworkAvailable(context)){
            getDialog(context,"No Internet","Please check your internet connection!!!",request,action);
            return;
        }
        CustomProgressDialog.getInstance(ctx).show();
        ApiCallService.request = request;
        Intent intent = new Intent(ctx, ApiCallService.class);
        intent.setAction(action);
        ctx.startService(intent);
    }

    class Local<T> implements Callback<T> {

        public void onResponse(Call<T> call, Response<T> response) {
            CustomProgressDialog.setDismiss();
            if (response.code() == 200) {
                T body = response.body();
                EventBus.getDefault().post(body);
            } else {
                EventBus.getDefault().post("Some thing went wrong!!! " + response.code());
            }

        }

        public void onFailure(Call<T> call, Throwable t) {
            CustomProgressDialog.setDismiss();
            EventBus.getDefault().post(t.getMessage());
        }
    }



    public static void action2(Activity ctx, Object request, String action) {
        context = ctx;
        ApiCallService.request = request;
        Intent intent = new Intent(ctx, ApiCallService.class);
        intent.setAction(action);
        ctx.startService(intent);
    }


    public  class Local2<T> implements Callback<T> {

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (response.code() == 200) {
                T body = response.body();
                EventBus.getDefault().post(body);
            } else {
                EventBus.getDefault().post(ApiCallService.Action.ERROR + " " + response.code());
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            EventBus.getDefault().post(t.getMessage());
        }
    }


    public interface Action{
        String ACTION_SIGN_UP="ACTION_SIGN_UP";
        String ACTION_UPDATE_PROFILE="ACTION_UPDATE_PROFILE";
        String ACTION_GET_PROFILE="ACTION_GET_PROFILE";
        String ERROR = "Some thing went wrong";
        String DOCUMENT = "documents";
        String ACTION_GET_NEW_LEAD = "ACTION_GET_NEW_LEAD";
        String ACTION_GET_ON_GOING= "ACTION_GET_ON_GOING";
        String ACTION_ACCEPT_ORDER = "ACTION_ACCEPT_ORDER";
        String ACTION_CANCEL_ORDER = "ACTION_CANCEL_ORDER";
        String ACTION_DELIVER = "ACTION_DELIVER";
        String ACTION_MENU_LIST = "ACTION_MENU_LIST";
        String ADD_SPECIAL_ITEM = "ADD_SPECIAL_ITEM";
        String ACTION_SAVE_TOKEN = "ACTION_SAVE_TOKEN";
        String ACTION_PUSH_NOTIFICATION = "ACTION_PUSH_NOTIFICATION";
        String ACTION_HISTORY = "ACTION_HISTORY";
        String ACTION_CHECK_LICENCE = "ACTION_CHECK_LICENCE";
        String ACTION_CHANGE_STATUS = "ACTION_CHANGE_STATUS";
        String ACTION_GET_STATUS = "ACTION_GET_STATUS";
    }

    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        Api api = ThisApp.getApi(this.getApplicationContext());
        if (action.equals(Action.ACTION_SIGN_UP)) {
            api.signUp((RequestSignUp) request).enqueue(new Local<SignUpResponse>());
        }if (action.equals(Action.ACTION_UPDATE_PROFILE)) {
            api.update((Map) request).enqueue(new Local<SignUpResponse>());
        }if (action.equals(Action.ACTION_GET_PROFILE)) {
            api.getProfile((Map) request).enqueue(new Local<GetProfileResponse>());
        }if (action.equals(Action.ACTION_GET_NEW_LEAD)) {
            api.getNewLeadOrder((Map) request).enqueue(new Local<GetNewOrderResponse>());
        }if (action.equals(Action.ACTION_ACCEPT_ORDER)) {
            api.acceptOrder((Map) request).enqueue(new Local<SuccessResponse>());
        }if (action.equals(Action.ACTION_CANCEL_ORDER)) {
            api.cancelOrder((Map) request).enqueue(new Local<SuccessResponse>());
        }if (action.equals(Action.ACTION_GET_ON_GOING)) {
            api.onGoingOrder((Map) request).enqueue(new Local<GetNewOrderResponse>());
        }if (action.equals(Action.ACTION_DELIVER)) {
            api.deliver((Map) request).enqueue(new Local<SuccessResponse>());
        }if (action.equals(Action.ACTION_MENU_LIST)) {
            api.getMenuList((Map) request).enqueue(new Local2<GetOrderMenuResponse>());
        }if (action.equals(Action.ADD_SPECIAL_ITEM)) {
            api.addSpecialItem((Map) request).enqueue(new Local<AddResponse>());
        }else if (action.equals(Action.ACTION_SAVE_TOKEN)) {
            api.saveToken((Map) request).enqueue(new Local<SuccessResponse>());
        }else if (action.equals(Action.ACTION_PUSH_NOTIFICATION)) {
            api.pushNotification((EventPushRequest) request).enqueue(new Local2<PushNotificationResponse>());
        }else if (action.equals(Action.ACTION_HISTORY)) {
            api.history((Map) request).enqueue(new Local<HistoryResponse>());
        }else if (action.equals(Action.ACTION_CHECK_LICENCE)) {
            api.checkLicence((Map) request).enqueue(new Local<SuccessResponse>());
        }else if (action.equals(Action.ACTION_CHANGE_STATUS)) {
            api.changeStatus((Map) request).enqueue(new Local<ChangeStatusResponse>());
        }else if (action.equals(Action.ACTION_GET_STATUS)) {
            api.getStatus((Map) request).enqueue(new Local2<GetStatusResponse>());
        }
    }





    static void getDialog(final Activity context, String tittle, String message, final Object request, final String action) {
        new AlertDialog.Builder(context)
                .setTitle(tittle)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ApiCallService.action(context,request,action);
                    }
                })
//                .setNegativeButton("Exit", null)
                .setIcon(R.drawable.ic_launcher_background)
                .show();
    }

}

