package mom.vender.com.history_invice;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import mom.vender.com.R;
import mom.vender.com.network.response.OrderDatum;
import mom.vender.com.utils.Preferences;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<OrderDatum> order_data;
    Context mContext;


    public HistoryAdapter(Context mContext,List<OrderDatum> order_data) {
        this.order_data = order_data;
        this.mContext = mContext;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        OrderDatum orderDatum=order_data.get(position);
        viewHolder.name.setText(orderDatum.getName());
        viewHolder.address.setText(orderDatum.getLocation());
        viewHolder.date.setText(orderDatum.getCreatedAt());
        viewHolder.orderId.setText(orderDatum.getOrderId());
        if (orderDatum.getOrderStatus()==4){
            viewHolder.status.setText("Cancelled");
        }else {
            viewHolder.status.setText("Delivered");
        }
        viewHolder.totalItem.setText(""+orderDatum.getProductList().size());

        if (orderDatum.getCustomerRating()==null){
            viewHolder.a.setVisibility(View.GONE);
        }else if (orderDatum.getCustomerRating()==0){
            viewHolder.a.setVisibility(View.GONE);
        }else {
            viewHolder.a.setVisibility(View.VISIBLE);
        }

        viewHolder.rating.setText(""+orderDatum.getCustomerRating());
        viewHolder.price.setText(orderDatum.getTotalPrice());
        viewHolder.customerMobile.setText(orderDatum.getMomMobile());
        if (!Preferences.getInstance(mContext).getProfileStatus().equals("")){
            Glide.with(mContext)
                    .load(Preferences.getInstance(mContext).getProfileImage())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .apply(new RequestOptions().placeholder(R.drawable.user))
                    .into(viewHolder.circleImageView);
        }



    }

    @Override
    public int getItemCount() {
        return order_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView name, address, date, orderId, status, totalItem, price, customerMobile,rating;
        public CircleImageView circleImageView;
        LinearLayout a;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.address);
            date = view.findViewById(R.id.date);
            orderId = view.findViewById(R.id.orderId);
            status = view.findViewById(R.id.status);
            totalItem = view.findViewById(R.id.totalItem);
            price = view.findViewById(R.id.price);
            customerMobile = view.findViewById(R.id.customerMobile);
            circleImageView = view.findViewById(R.id.circular_iv);
            rating = view.findViewById(R.id.rating);
            a = view.findViewById(R.id.a);

        }

    }
}
