package mom.vender.com.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import mom.vender.com.R;
import mom.vender.com.network.response.ProductList;
import mom.vender.com.network.response.menu_item.Menu_data;

public class TodaySpecialAdapter extends RecyclerView.Adapter<TodaySpecialAdapter.ViewHolder> {

    Context context;
    List<Menu_data> response;
    public TodaySpecialAdapter(Context mContext, List<Menu_data> menu_data) {
        response=menu_data;
        context=mContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public TodaySpecialAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_special_order, parent, false);
        return new TodaySpecialAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final TodaySpecialAdapter.ViewHolder viewHolder, int position) {

        Menu_data menu_data=response.get(position);
        viewHolder.itemName.setText(menu_data.getItemName());
        viewHolder.food_type.setText(""+menu_data.getFood_type());
        viewHolder.itemGroup.setText(""+menu_data.getItemGroup());
        viewHolder.itemDescription.setText(""+menu_data.getItemDescription());
        viewHolder.price.setText(""+menu_data.getFullPrice());


        Glide.with(context)
                .load(menu_data.getImage_path()+""+menu_data.getItem_image())
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(new RequestOptions().placeholder(R.drawable.default_food))
                .into(viewHolder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView  itemName, food_type, itemGroup, itemDescription,price;
        public ImageView circleImageView;

        public ViewHolder(View view) {
            super(view);
            itemName =  view.findViewById(R.id.itemName);
            food_type = view.findViewById(R.id.food_type);
            itemGroup = view.findViewById(R.id.itemGroup);
            circleImageView = view.findViewById(R.id.circular_iv);
            itemDescription = view.findViewById(R.id.itemDescription);
            price = view.findViewById(R.id.price);

        }

    }



}
