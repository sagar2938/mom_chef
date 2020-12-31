package mom.vender.com.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import mom.vender.com.R;
import mom.vender.com.network.ApiCallService;
import mom.vender.com.network.ThisApp;
import mom.vender.com.network.request.EventPushRequest;
import mom.vender.com.network.response.OrderDatum;
import mom.vender.com.network.response.PushNotificationResponse;
import mom.vender.com.utils.Preferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnGoingAdapter extends RecyclerView.Adapter<OnGoingAdapter.ViewHolder> {

    Activity context;
    List<OrderDatum> response;

    public OnGoingAdapter(Activity mContext, List<OrderDatum> orderDataList) {
        this.context = mContext;
        response = orderDataList;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public OnGoingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_on_going, parent, false);
        return new OnGoingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OnGoingAdapter.ViewHolder viewHolder, int position) {

        OrderDatum orderDatum = response.get(position);
        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.recyclerView.setAdapter(new OrderItemDetailAdapter(context, response.get(position).getProductList()));

        viewHolder.name.setText(response.get(position).getName());
        viewHolder.address.setText(response.get(position).getLocation());
        viewHolder.totalAmount.setText("₹" + response.get(position).getTotalPrice());
        viewHolder.time.setText(response.get(position).getCreatedAt());
        viewHolder.orderId.setText(response.get(position).getOrderId());

        viewHolder.note.setText(response.get(position).getNote());
        if (response.get(position).getNote() == null) {
            viewHolder.note.setVisibility(View.GONE);
        } else {
            if (response.get(position).getNote().trim().equals("")) {
                viewHolder.note.setVisibility(View.GONE);
            } else {
                viewHolder.note.setVisibility(View.VISIBLE);
            }
        }
        if (response.get(position).getPromoCode() == null) {
            viewHolder.promo.setVisibility(View.GONE);
        } else {
            if (response.get(position).getPromoCode().trim().equals("")) {
                viewHolder.promo.setVisibility(View.GONE);
            } else {
                viewHolder.promo.setVisibility(View.VISIBLE);
            }
        }

        if (response.get(position).getPromoCode() == null) {
            viewHolder.promo.setVisibility(View.GONE);
        } else {
            if (response.get(position).getPromoCode().trim().equals("")) {
                viewHolder.promo.setVisibility(View.GONE);
            } else {
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
                Map map = new HashMap();
                Preferences.getInstance(context).setCustomerMobile(orderDatum.getMobile());
                map.put("orderId", response.get(position).getOrderId());

                map.put("mom_mobile", response.get(position).getMomMobile());
                ApiCallService.action(context, map, ApiCallService.Action.ACTION_DELIVER);

                /*EventBus.getDefault().post(new EventPushRequest("Delivery",orderDatum.getDeliver_number(),"You got a new Order"));*/

               /* ThisApp.getApi(context).pushNotification(new EventPushRequest(orderDatum.getMobile(), "Your order has been picked-up for delivery")).enqueue(new Callback<PushNotificationResponse>() {
                    @Override
                    public void onResponse(Call<PushNotificationResponse> call, Response<PushNotificationResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<PushNotificationResponse> call, Throwable t) {

                    }
                });*/
//                ApiCallService.action2(context,request,ApiCallService.Action.ACTION_PUSH_NOTIFICATION);
//                Preferences.getInstance(context).setCustomerMobile(orderDatum.getMobile());
                EventBus.getDefault().post(new EventPushRequest(orderDatum.getMobile(),"Your order has been picked-up for delivery"));
            }
        });
        if (response.get(position).getDeliver_number() != null) {
            if (response.get(position).getOrderStatus() > 2) {
                viewHolder.delivered.setVisibility(View.VISIBLE);
                viewHolder.deliver.setVisibility(View.GONE);
            } else {
                viewHolder.delivered.setVisibility(View.GONE);
                viewHolder.deliver.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.deliveredTxt.setText("Waiting for pilot allotment");
            viewHolder.delivered.setVisibility(View.VISIBLE);
            viewHolder.deliver.setVisibility(View.GONE);
        }

        viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog(response.get(position), "Cancel", "Are you sure you want to cancel this order?", position);
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
        CardView deliver;
        CardView delivered;
        TextView note;
        TextView promo;
        TextView deliveredTxt;

        public ViewHolder(View view) {
            super(view);
            deliveredTxt = view.findViewById(R.id.deliveredTxt);
            recyclerView = view.findViewById(R.id.recyclerView);
            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.address);
            totalAmount = view.findViewById(R.id.totalAmount);
            time = view.findViewById(R.id.time);
            orderId = view.findViewById(R.id.orderId);
            accept = view.findViewById(R.id.accept);
            cancel = view.findViewById(R.id.cancel);
            deliver = view.findViewById(R.id.deliver);
            delivered = view.findViewById(R.id.delivered);
            note = view.findViewById(R.id.note);
            promo = view.findViewById(R.id.promo);

        }

    }


    public void getDialog(OrderDatum orderDatum, String tittle, String message, Integer position) {
        new AlertDialog.Builder(context)
                .setTitle(tittle)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Map map = new HashMap();
                        map.put("orderId", response.get(position).getOrderId());
                        ApiCallService.action(context, map, ApiCallService.Action.ACTION_CANCEL_ORDER);
                        EventPushRequest request = new EventPushRequest(orderDatum.getMobile(), "Your order " + orderDatum.getId() + " is canceled");
                        ApiCallService.action2(context, request, ApiCallService.Action.ACTION_PUSH_NOTIFICATION);
                    }
                })
                .setNegativeButton("No", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

}
