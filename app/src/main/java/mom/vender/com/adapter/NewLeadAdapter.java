package mom.vender.com.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import mom.vender.com.R;
import mom.vender.com.activity.AddNewOrderActivity;
import mom.vender.com.model.MenuModel;
import mom.vender.com.model.OnNewOrderClick;
import mom.vender.com.network.Api;
import mom.vender.com.network.ApiCallService;
import mom.vender.com.network.ThisApp;
import mom.vender.com.network.request.EventPushRequest;
import mom.vender.com.network.response.OrderDatum;
import mom.vender.com.network.response.PushNotificationResponse;
import mom.vender.com.utils.Preferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewLeadAdapter extends RecyclerView.Adapter<NewLeadAdapter.ViewHolder> {

    Activity context;
    List<OrderDatum> response;
    public NewLeadAdapter(Activity mContext, List<OrderDatum> orderDataList) {
        this.context=mContext;
        response=orderDataList;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public NewLeadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_new_lead, parent, false);
        return new NewLeadAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewLeadAdapter.ViewHolder viewHolder, int position) {
        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.recyclerView.setAdapter(new OrderItemDetailAdapter(context,response.get(position).getProductList()));


        viewHolder.name.setText(response.get(position).getName());
        viewHolder.address.setText(response.get(position).getLocation());
        viewHolder.totalAmount.setText("₹"+response.get(position).getTotalPrice());
        viewHolder.time.setText(response.get(position).getCreatedAt());
        viewHolder.orderId.setText(response.get(position).getOrderId());

        viewHolder.note.setText(response.get(position).getNote());
        if (response.get(position).getNote()==null){
            viewHolder.note.setVisibility(View.GONE);
        }else {
            if (response.get(position).getNote().trim().equals("")){
                viewHolder.note.setVisibility(View.GONE);
            }else {
                viewHolder.note.setVisibility(View.VISIBLE);
            }
        }

        if (response.get(position).getPromoCode()==null){
            viewHolder.promo.setVisibility(View.GONE);
        }else {
            if (response.get(position).getPromoCode().trim().equals("")){
                viewHolder.promo.setVisibility(View.GONE);
            }else {
                viewHolder.promo.setVisibility(View.VISIBLE);
                try {
                    viewHolder.promo.setText("Applied PromoCode: "+response.get(position).getPromoCode().split("_")[1]);
                } catch (Exception e) {
                    viewHolder.promo.setText("Applied PromoCode: "+response.get(position).getPromoCode().trim());
                }
            }
        }




        viewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map=new HashMap();
                map.put("orderId",response.get(position).getOrderId());
                map.put("mom_mobile", Preferences.getInstance(context).getMobileNumber());
                ApiCallService.action(context, map, ApiCallService.Action.ACTION_ACCEPT_ORDER);

                /*ThisApp.getApi(context).pushNotification(new EventPushRequest(response.get(position).getMobile(), "Your order has been accepted by MOMCHEF!")).enqueue(new Callback<PushNotificationResponse>() {
                    @Override
                    public void onResponse(Call<PushNotificationResponse> call, Response<PushNotificationResponse> response) {
                        Toast.makeText(context, "hi"+response.code(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<PushNotificationResponse> call, Throwable t) {
                        Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });*/
                EventBus.getDefault().post(new EventPushRequest(response.get(position).getMobile(),"Your order has been accepted by MOMCHEF!"));
            }
        });


        viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog("Cancel","Are you sure you want to cancel this order?",position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        TextView name;
        TextView address;
        TextView totalAmount;
        TextView time;
        TextView orderId;
        LinearLayout accept;
        LinearLayout cancel;
        TextView note;
        TextView promo;

        public ViewHolder(View view) {
            super(view);
            recyclerView=view.findViewById(R.id.recyclerView);
            name=view.findViewById(R.id.name);
            address=view.findViewById(R.id.address);
            totalAmount=view.findViewById(R.id.totalAmount);
            time=view.findViewById(R.id.time);
            orderId=view.findViewById(R.id.orderId);
            accept=view.findViewById(R.id.accept);
            cancel=view.findViewById(R.id.cancel);
            note=view.findViewById(R.id.note);
            promo=view.findViewById(R.id.promo);
        }

    }



    public void getDialog(String tittle, String message,Integer position) {
        new AlertDialog.Builder(context)
                .setTitle(tittle)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Map map=new HashMap();
                        map.put("orderId",response.get(position).getOrderId());
                        ApiCallService.action(context, map, ApiCallService.Action.ACTION_CANCEL_ORDER);
                        EventBus.getDefault().post(new EventPushRequest(response.get(position).getMobile(),"Your order no "+response.get(position).getOrderId() +" has been cancelled by MOM Chef"));

                    }
                })
                .setNegativeButton("No", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

}
