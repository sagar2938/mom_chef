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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import mom.vender.com.R;
import mom.vender.com.activity.AddNewOrderActivity;
import mom.vender.com.model.MenuModel;
import mom.vender.com.model.OnNewOrderClick;
import mom.vender.com.network.ApiCallService;
import mom.vender.com.network.ThisApp;
import mom.vender.com.network.response.ChangeStatusResponse;
import mom.vender.com.utils.MyProgressDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    public static int totalNumOfLeads;
    private final List<MenuModel> mValues;
    Activity mContext;
    OnNewOrderClick onNewOrderClick;
    Map map;


    public MenuAdapter(List<MenuModel> items, Activity mContext, OnNewOrderClick onNewOrderClick) {
        this.mValues = items;
        this.mContext = mContext;
        this.onNewOrderClick = onNewOrderClick;
        map = new HashMap();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_menu_list, parent, false);
        return new MenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MenuAdapter.ViewHolder holder, int position) {
        final MenuModel newLeadTable = mValues.get(position);
        holder.itemName.setText(newLeadTable.itemName);
        holder.itemGroup.setText(newLeadTable.itemGroup);
        holder.itemDescription.setText(newLeadTable.itemDescription);
        holder.food_type.setText(newLeadTable.food_type);
        holder.price.setText("₹" + newLeadTable.getFullPrice());
        if (newLeadTable.getStatus()==0){
            holder.status.setText("Disabled");
        }else {
            holder.status.setText("Enabled");
        }
        if (newLeadTable.getStatus()==0) {
            holder.enableDisable.setImageDrawable(mContext.getResources().getDrawable(R.drawable.enable));
        }else {
            holder.enableDisable.setImageDrawable(mContext.getResources().getDrawable(R.drawable.disable));
        }
        Glide.with(mContext)
                .load(newLeadTable.getItem_image())
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(new RequestOptions().placeholder(R.drawable.default_food))
                .into(holder.circleImageView);


        if(newLeadTable.getItem_image() !=null && !newLeadTable.getItem_image().equals("")) {
            Glide.with(mContext)
                    .load(newLeadTable.getItem_image())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .apply(new RequestOptions().placeholder(R.drawable.default_food))
                    .into(holder.circleImageView);
        }else{
            holder.circleImageView.setImageDrawable(null);
        }

       /* Picasso.get()
                .load(newLeadTable.itemUrl)
                .resize(50, 50) // here you resize your image to whatever width and height you like
                .into(holder.circleImageView);*/

        holder.delete.setOnClickListener(new View.OnClickListener() {
            boolean visible;

            @Override
            public void onClick(View v) {
                onNewOrderClick.onClick(newLeadTable.id);

            }
        });


        holder.enableDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newLeadTable.getStatus() == 0) {
                    getDialog("Enable", "Are you sure?", newLeadTable);
                } else {
                    getDialog("Disable", "Are you sure?", newLeadTable);
                }
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            boolean visible;

            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, AddNewOrderActivity.class);
                intent.putExtra("id", newLeadTable.id);
                intent.putExtra("itemName", newLeadTable.itemName);
                intent.putExtra("itemGroup", newLeadTable.itemGroup);
                intent.putExtra("food_type", newLeadTable.food_type);
                intent.putExtra("itemDescription", newLeadTable.itemDescription);
                intent.putExtra("itemPrice", newLeadTable.getFullPrice());
                intent.putExtra("url", newLeadTable.getItem_image());
                intent.putExtra("itemPreparationTime", newLeadTable.getItemPreparationTime());
                intent.putExtra("isUpdate", true);
                context.startActivity(intent);

            }
        });


        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView itemName, food_type, itemGroup, itemDescription, price,status;
        public ImageView circleImageView;
        public ImageView enableDisable;

        ImageView edit, delete;
        CardView cardView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            edit = view.findViewById(R.id.edit);
            delete = view.findViewById(R.id.delete);
            itemName = view.findViewById(R.id.itemName);
            food_type = view.findViewById(R.id.food_type);
            itemGroup = view.findViewById(R.id.itemGroup);
            circleImageView = view.findViewById(R.id.circular_iv);
            itemDescription = view.findViewById(R.id.itemDescription);
            price = view.findViewById(R.id.price);
            cardView = view.findViewById(R.id.cardView);
            enableDisable = view.findViewById(R.id.enableDisable);
            status = view.findViewById(R.id.status);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + itemName.getText() + "'";
        }
    }


    public void getDialog(String tittle, String message,MenuModel menuModel) {
        new AlertDialog.Builder(mContext)
                .setTitle(tittle)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MyProgressDialog.getInstance(mContext);
                        Map map=new HashMap();
                        map.put("item_id",menuModel.id);
                        if (menuModel.getStatus()==0){
                            map.put("status","1");
                        }else {
                            map.put("status","0");
                        }
                        ThisApp.getApi(mContext).changeFoodStatus(map).enqueue(new Callback<ChangeStatusResponse>() {
                            @Override
                            public void onResponse(Call<ChangeStatusResponse> call, Response<ChangeStatusResponse> response) {
                                MyProgressDialog.setDismiss();
                                if (response.code()==200){
                                    if (menuModel.getStatus()==1){
                                        menuModel.setStatus(0);
                                    }else {
                                        menuModel.setStatus(1);
                                    }
                                    notifyDataSetChanged();
                                }else {
                                    Toast.makeText(mContext, "Some thing went wrong "+response.code(), Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<ChangeStatusResponse> call, Throwable t) {
                                MyProgressDialog.setDismiss();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

}
