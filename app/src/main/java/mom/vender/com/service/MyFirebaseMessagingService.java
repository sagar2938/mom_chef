package mom.vender.com.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import mom.vender.com.R;
import mom.vender.com.activity.MainActivity;


/**
 * Created by NgocTri on 8/9/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    public static final int GCM_NOTIF_ID = 0xCAFE;
    PendingIntent pendingIntent;

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);
        String from = message.getFrom();
        Map data = message.getData();
        JSONObject jsonObject = new JSONObject(data);
        Log.e("FCMMessage", "From: " + jsonObject);
        try {
            if (jsonObject.has("orderId")){
                sendNotificationConfirm(jsonObject.getString("totalAmount"), jsonObject.getString("orderId"), jsonObject.getString("address"), jsonObject.getString("promoCode"), jsonObject.getString("note"), jsonObject.getString("customerName"), jsonObject.getString("customerMobile"));
            }else {
                sendNotification(jsonObject.getString("message"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        String token = FirebaseInstanceId.getInstance().getToken();
        System.out.println("http token " + token);
        /*Map map=new HashMap<>();
        map.put("mobile", Preferences.getInstance(getApplicationContext()).getMobileNumber());
        map.put("fcmToken",token);
        map.put("userType","delivery");
        ApiCallService.action(this,map,ApiCallService.Action.ACTION_SAVE_TOKEN);*/

    }

    private void sendNotificationConfirm(String totalAmount, String orderId, String address, String promoCode, String note, String customerName, String customerMobile) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("MOM")
                .setContentText(orderId)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        Intent trackingActivity = new Intent(getBaseContext(), MainActivity.class);
        trackingActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        trackingActivity.putExtra("FROM", 3);
        trackingActivity.putExtra("totalAmount", totalAmount);
        trackingActivity.putExtra("orderId", orderId);
        trackingActivity.putExtra("address", address);
        trackingActivity.putExtra("promoCode", promoCode);
        trackingActivity.putExtra("note", note);
        trackingActivity.putExtra("customerName", customerName);
        trackingActivity.putExtra("customerMobile", customerMobile);
        startActivity(trackingActivity);
    }




    private void sendNotification(String message) {

//        intent used to click on notification
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

//        custom notification view
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_push_layout);
        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        contentView.setTextViewText(R.id.title, "MOM");
        contentView.setTextViewText(R.id.text, message);


//        notification code
        final String CHANNEL_ID = "channel_02";
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle("MOM")
                .setContent(contentView)
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }
        builder.setAutoCancel(true);
        mNotificationManager.notify(0, builder.build());
    }



}
