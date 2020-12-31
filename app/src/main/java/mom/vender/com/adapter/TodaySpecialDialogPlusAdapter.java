package mom.vender.com.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import mom.vender.com.R;
import mom.vender.com.network.response.menu_item.Menu_data;

/**
 * Created by pc patidar on 09/04/19.
 */

public class TodaySpecialDialogPlusAdapter extends BaseAdapter {
    Context context;
    List<Menu_data> response;

    public TodaySpecialDialogPlusAdapter(Context context, List<Menu_data> menu_data) {
        this.context = context;
        this.response = menu_data;
    }


    @Override
    public int getCount() {
        return response.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {// inflate the layout for each list row
        ViewHolder viewHolder;
        try {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.row_special_order, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // get the TextView for item name and item description

            Menu_data menu_data=response.get(position);
            viewHolder.itemName.setText(menu_data.getItemName());
            viewHolder.food_type.setText(""+menu_data.getFood_type());
            viewHolder.itemGroup.setText(""+menu_data.getItemGroup());
            viewHolder.itemDescription.setText(""+menu_data.getItemDescription());
            viewHolder.price.setText(""+menu_data.getFullPrice());


            Glide.with(context)
                    .load(menu_data.getItem_image())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .apply(new RequestOptions().placeholder(R.drawable.default_food))
                    .into(viewHolder.circleImageView);






        }catch (Exception e){

        }
            return convertView;
    }

    private class ViewHolder {
        public TextView  itemName, food_type, itemGroup, itemDescription,price;
        public ImageView circleImageView;


        public ViewHolder(View view) {
            itemName =  view.findViewById(R.id.itemName);
            food_type = view.findViewById(R.id.food_type);
            itemGroup = view.findViewById(R.id.itemGroup);
            circleImageView = view.findViewById(R.id.circular_iv);
            itemDescription = view.findViewById(R.id.itemDescription);
            price = view.findViewById(R.id.price);
        }
    }


}
