package mom.vender.com.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;


import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import mom.vender.com.BuildConfig;
import mom.vender.com.R;

import static android.os.Build.VERSION_CODES.M;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class Helper {

//    public static void initActionbar(Activity activity, ActionBar actionBar, String tittleText, boolean homeButton) {
//        View viewActionBar = activity.getLayoutInflater().inflate(R.layout.tool_bar, null);
//        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
//                ActionBar.LayoutParams.MATCH_PARENT,
//                ActionBar.LayoutParams.WRAP_CONTENT,
//                Gravity.CENTER);
//        actionBar.setElevation(0);
//        actionBar.setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.colorPrimaryDark)));
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setCustomView(viewActionBar, params);
//        TextView actionbarTitle = (TextView) viewActionBar.findViewById(R.id.title);
//        actionbarTitle.setText(tittleText);
//        actionbarTitle.setTypeface(TypefaceCache.get(activity.getAssets(), 3));
//        actionbarTitle.setTextSize(18);
//        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayHomeAsUpEnabled(homeButton);
//        actionBar.setHomeButtonEnabled(homeButton);
//    }


    public static void setProfilePic(ImageView v, String path) {
        v.setImageBitmap(BitmapFactory.decodeFile(path));
    }

    public static String[] createStringRange(int start, int delta) {

        String[] res = new String[delta];

        for (int i = 0; i < delta; i++) {
            res[i] = String.valueOf(start + i);
        }
        return res;
    }

    public static float defineVolume(Context ctx, int stream) {

        AudioManager audioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);

        final float actualVolume = (float) audioManager.getStreamVolume(stream);
        final float maxVolume = (float) audioManager.getStreamMaxVolume(stream);
        float volume = actualVolume / maxVolume;
        volume = volume == 0 ? .5f : volume;
        return volume;
    }


    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isNetworkAvailableWithDialog(Context ctx) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        boolean available = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        if (!available) {
            new AlertDialog.Builder(ctx)
                    .setMessage("Check out your Network connection!")
                    .setPositiveButton("Ok", null)
                    .show();
        }
        return available;
    }

    public static void isLocationEnabled(Context ctx) {

        LocationManager service = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

        boolean gpsEnabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean netEnabled = service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gpsEnabled && !netEnabled) {

//            new AlertDialog.Builder(ctx)
//                    .setMessage(ctx.getString(R.string.enable_location))
//                    .setPositiveButton(ctx.getString(R.string.ok), (d, i) ->
//                            ctx.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
//                    .show();
        }
    }

    public static void isGPSEnabled(Context ctx) {



        LocationManager service = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

        boolean gpsEnabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {

//            new AlertDialog.Builder(ctx)
//                    .setMessage(ctx.getString(R.string.enable_gps))
//                    .setPositiveButton(ctx.getString(R.string.ok), (d, i) ->
//                            ctx.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
//                    .setNegativeButton(ctx.getString(R.string.btn_cancel), null)
//                    .show();
        }
    }

    public static String getDeviceId(Context ctx) {

        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

        String deviceId;

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        if (tm.getDeviceId() != null) {

            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            deviceId = tm.getDeviceId();
        } else {

            deviceId = Settings.Secure.getString(
                    ctx.getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }

    public static String bitmapToBase64(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap base64ToBitmap(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static Bitmap getRoundBitmap(Bitmap bitmap) {

        int min = Math.min(bitmap.getWidth(), bitmap.getHeight());

        Bitmap bitmapRounded = Bitmap.createBitmap(min, min, bitmap.getConfig());

        Canvas canvas = new Canvas(bitmapRounded);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0.0f, 0.0f, min, min)), min / 2, min / 2, paint);

        return bitmapRounded;
    }


    /****
     * Method for Setting the Height of the ListView dynamically.
     * *** Hack to fix the issue of not showing all the items of the ListView
     * *** when placed inside a ScrollView
     ****/
    public static String mystring(String string) {
        if (string == null||string.equals("null")) {
            string = "";
        }
        return string;
    }

    public static void openKeyPad(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }  public static void closeKeyPad(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_HIDDEN,0);
    }

    public static String  getMonth(int month){
        String[] ar={"","Jan","Feb","Mar","Apr","May","Jun","July","Aug","Sep","Oct","Nov","Dec"};
        return ar[month];
    }

    public static String getDayFromDateString(String stringDate) {
        String[] daysArray = new String[] {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
        String day = "";

        int dayOfWeek =0;
        //dateTimeFormat = yyyy-MM-dd HH:mm:ss
        SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");
//        SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
        Date date;
        try {
            date = formatter.parse(stringDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            dayOfWeek = c.get(Calendar.DAY_OF_WEEK)-1;
            if (dayOfWeek < 0) {
                dayOfWeek += 7;
            }
            day = daysArray[dayOfWeek];
        }catch (Exception e) {
            e.printStackTrace();
        }

        return day;
    }

    public static void closeKeyPad(Activity activity,boolean isKeyPadOpen) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isKeyPadOpen){
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }


    boolean isKeyPadOpen;
    public boolean isKeyPadOpen(Activity context){
        isKeyPadOpen=false;
        KeyboardVisibilityEvent.setEventListener(context, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                isKeyPadOpen = isOpen;
            }
        });
        return isKeyPadOpen;
    }


    public static void getDialog(Context context,String tittle,String message){
        AlertDialog alertDialog=new AlertDialog.Builder(context)
                .setTitle(tittle)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

//                .setNegativeButton("Cancel", null)
                .setIcon(R.drawable.ic_launcher_background)
                .show();
    }


    public static void getDialogAddUpdate(Context context,String tittle,String message){
        AlertDialog alertDialog=new AlertDialog.Builder(context)
                .setTitle(tittle)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

//                .setNegativeButton("Cancel", null)
                .setIcon(R.drawable.ic_launcher_background)
                .show();
    }


    public static File getOutputMediaFile(int type, String imageNameUrl) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "mDoctor");
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            return null;
        } else if (type != MEDIA_TYPE_IMAGE) {
            return null;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(mediaStorageDir.getPath());
            stringBuilder.append(File.separator);
            stringBuilder.append(imageNameUrl);
            stringBuilder.append(".jpg");
            return new File(stringBuilder.toString());
        }
    }


    public static File getOutputMediaFileVideo(int type, String imageNameUrl) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "mDoctor");
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            return null;
        } else if (type != MEDIA_TYPE_VIDEO) {
            return null;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(mediaStorageDir.getPath());
            stringBuilder.append(File.separator);
            stringBuilder.append(imageNameUrl);
            stringBuilder.append(".mp4");
            return new File(stringBuilder.toString());
        }
    }

    public static String getRandom(){
        return ""+System.currentTimeMillis();
        //        return String.format("%07d", new Random().nextInt(10000));
    }

    public static String getOtp(){
        return String.format("%04d", new Random().nextInt(1000));
    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    public static int  dateValidation(String start, String end){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Date dateObject1 = null;
        Date dateObject2 = null;
        try {
            dateObject1 = dateFormatter.parse(start);
            dateObject2 = dateFormatter.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
       /* if (dateObject2.compareTo(dateObject1) == -1) {
            return;
        }*/
        return dateObject2.compareTo(dateObject1);
    }

    public static String getFileName(Context context,Uri uri){
        String displayName = null;
        if (uri.toString().startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        } else if (uri.toString().startsWith("file://")) {
            displayName = new File(uri.toString()).getName();
        }
        return displayName;
    }

    public static String getCurrentDate() {
        return  new SimpleDateFormat("dd/MM/yyyy").format(new Date());

    }

    public static String getCurrentYear() {
        return  new SimpleDateFormat("yyyy").format(new Date());

    }

    public static String getCurrentTime() {
        return  new SimpleDateFormat("HH:mm a").format(new Date());
    }

    public static String getDate(int plus) {
        if (plus == 0) {
            return getCurrentDate();
        }
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, plus);
        date = c.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

// Helper.preview(getContext(),Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/mSchooling/QrCode.pdf"));

    public static void preview(Context context,File file){
        Uri imgUri ;
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Toast.makeText(context,"Mounted",Toast.LENGTH_SHORT).show();
        }
        if (Build.VERSION.SDK_INT > M) {
            imgUri = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider", file);
            Log.d("PdfUriPath", String.valueOf(imgUri));
        } else {
            imgUri = Uri.fromFile(file);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(imgUri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static Date toDate(String dateStr){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = format.parse(dateStr);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }


    public static void enableDisableView(View view, boolean enabled) {
        /*view.setEnabled(enabled);
        if ( view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;
            for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }*/
    }

    public static boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public static boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public static void notificationVoice(Context context){
        final MediaPlayer mediaPlayer;
        final Vibrator vib;
        mediaPlayer = MediaPlayer.create(context, R.raw.carnatic);
        mediaPlayer.start();
//        vib = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
//        vib.vibrate(111110000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.stop();
            }
        },5000);
    }

}
