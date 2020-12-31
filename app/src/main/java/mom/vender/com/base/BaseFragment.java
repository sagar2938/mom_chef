package mom.vender.com.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import mom.vender.com.R;
import mom.vender.com.network.ApiCallService;
import mom.vender.com.network.ThisApp;
import mom.vender.com.network.request.EventPushRequest;
import mom.vender.com.network.response.PushNotificationResponse;
import mom.vender.com.utils.CustomUploadProgressDialog;
import mom.vender.com.utils.UploadEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseFragment  extends Fragment {

    public boolean isKeyPadOpen;

    @Override
    public void onResume() {
        super.onResume();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
        }

        locationEnabled();
    }


    private void locationEnabled () {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context. LOCATION_SERVICE ) ;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager. GPS_PROVIDER ) ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager. NETWORK_PROVIDER ) ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(getActivity() )
                    .setTitle("GPS Status")
                    .setMessage( "Please Enable GPS" )
                    .setPositiveButton( "TURN ON" , new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick (DialogInterface paramDialogInterface , int paramInt) {
                                    startActivity( new Intent(Settings. ACTION_LOCATION_SOURCE_SETTINGS )) ;
                                }
                            })
                    .setNegativeButton( "Cancel" , null )
                    .show() ;
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
        }
    }

    @Subscribe
    public void timeOut(String msg) {
        getDialog("Failed", msg);
    }

    public void getDialog(String tittle, String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(tittle)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
//                .setNegativeButton("Cancel", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

    public void getDialogSuccess( String message) {
        new AlertDialog.Builder(getContext())
                .setTitle("Success")
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
//                .setNegativeButton("Cancel", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }


    public void getDialog(String message) {
        new AlertDialog.Builder(getContext())
                .setTitle("Sorry")
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
//                .setNegativeButton("Cancel", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }


    public void getDialogSorry() {
        new AlertDialog.Builder(getContext())
                .setTitle("Sorry")
                .setMessage("Something went wrong, Please try again.")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
//                .setNegativeButton("Cancel", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }


    public void uploadFile(String imageName, Uri uri) {
        CustomUploadProgressDialog.getInstance(getActivity()).show();
        StorageReference storage = FirebaseStorage.getInstance().getReference().child(ApiCallService.Action.DOCUMENT).child(imageName);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(ApiCallService.Action.DOCUMENT);
        storage.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                EventBus.getDefault().post(new UploadEvent(uri.toString()));
                                uri.toString();
                                CustomUploadProgressDialog.setDismiss();
                            }
                        });
                    }


                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        CustomUploadProgressDialog.setDismiss();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        CustomUploadProgressDialog.setProgress("Uploading... " + String.format("%.0f", progress) + " %");
                        if (progress == 100) {
                            CustomUploadProgressDialog.setProgress("Uploaded");
                        }
                    }
                });
    }


    @Subscribe
    public void eventPush(EventPushRequest request) {
        if (!request.getTo().trim().isEmpty()){
            ThisApp.getApi(getContext()).pushNotification(request).enqueue(new Callback<PushNotificationResponse>() {
                @Override
                public void onResponse(Call<PushNotificationResponse> call, Response<PushNotificationResponse> response) {
//                Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<PushNotificationResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "push notification failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
