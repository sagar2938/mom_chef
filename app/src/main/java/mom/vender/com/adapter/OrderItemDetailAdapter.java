package mom.vender.com.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import mom.vender.com.R;
import mom.vender.com.network.response.OrderDatum;
import mom.vender.com.network.response.ProductList;

public class OrderItemDetailAdapter extends RecyclerView.Adapter<OrderItemDetailAdapter.ViewHolder> {

    Context context;
    List<ProductList> response;
    public OrderItemDetailAdapter(Context mContext, List<ProductList> orderDataList) {
        response=orderDataList;
        context=mContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public OrderItemDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_product_item, parent, false);
        return new OrderItemDetailAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final OrderItemDetailAdapter.ViewHolder viewHolder, int position) {

        ProductList productList=response.get(position);
        viewHolder.itemName.setText(productList.getFoodItem());
        viewHolder.itemQuantity.setText(productList.getQuantity());
        viewHolder.itemAmount.setText(""+(Double.valueOf(productList.getQuantity())*Double.valueOf(productList.getItemActualPrice())));

        Glide.with(context)
                .load(response.get(position).getImage())
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(new RequestOptions().placeholder(R.drawable.default_food))
                .into(viewHolder.image);

    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        TextView itemQuantity;
        TextView itemAmount;
        ImageView image;

        public ViewHolder(View view) {
            super(view);
            itemName=view.findViewById(R.id.itemName);
            itemQuantity=view.findViewById(R.id.itemQuantity);
            itemAmount=view.findViewById(R.id.itemAmount);
            image=view.findViewById(R.id.image);

        }

    }



}
