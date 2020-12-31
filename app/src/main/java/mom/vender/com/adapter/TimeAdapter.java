package mom.vender.com.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import mom.vender.com.network.response.Time_data;
import mom.vender.com.R;


public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {

    private Context context;
    private List<Time_data> data;
    Map<Integer,Time_data> map;

    public TimeAdapter(Context context, List<Time_data> data) {

        this.context = context;
        this.data = data;
        map=new HashMap<>();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_time, viewGroup, false);
        return new ViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.time.setText(data.get(position).getTime());
        if (map.containsKey(position)){
            selectedSlot(viewHolder);
        }else {
            freeSlot(viewHolder);
        }
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map.containsKey(position)) {
                    map.remove(position);
                } else {
                    map.put(position, data.get(position));
                }
                notifyDataSetChanged();
            }

        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView time;
        ImageView image;


        public ViewHolder(View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            time=itemView.findViewById(R.id.time);
            layout=itemView.findViewById(R.id.layout);
        }
    }

    String changeAmPm(String time) {
        String am_pm = time.split(" ")[1];
        String t = time;
        if (am_pm.equals("AM")) {
            t = t.replace("AM", "a.m.");
        } else {
            t = t.replace("PM", "p.m.");
        }
        return t;
    }

    /*@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void assignedSlot(ViewHolder viewHolder){
        viewHolder.layout.setBackground(context.getResources().getDrawable(R.drawable.green_bg));
        viewHolder.time.setTextColor(context.getResources().getColor(R.color.white));
        viewHolder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.clock_white));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void pastSlot(ViewHolder viewHolder){
        viewHolder.layout.setBackground(context.getResources().getDrawable(R.drawable.dialog_btn_bg));
        viewHolder.time.setTextColor(context.getResources().getColor(R.color.white));
        viewHolder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.clock_white));
    }*/

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void selectedSlot(ViewHolder viewHolder){
        viewHolder.layout.setBackground(context.getResources().getDrawable(R.drawable.bg_color_primary_lining));
        viewHolder.time.setTextColor(context.getResources().getColor(R.color.white));
        viewHolder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.clock_white));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void freeSlot(ViewHolder viewHolder){
//        viewHolder.layout.setBackground(context.getResources().getDrawable(R.drawable.bg_grey_lining3));
//        viewHolder.time.setTextColor(context.getResources().getColor(R.color.default_text_color));
        viewHolder.time.setTextColor(context.getResources().getColor(R.color.black));
        viewHolder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.clock_grey));
    }



    /*void click(ViewHolder viewHolder,int position){
       try {
           if (!Helper.isAfterFromCurrentDate(dateTime + " " + changeAmPm(data.get(position).getTime())) || data.get(position).getStatus().equals("Assigned")) {
               return;
           }
           appUser.time = viewHolder.time.getText().toString();
           LocalRepositories.saveAppUser(context, appUser);
           row_index = position;
           notifyDataSetChanged();
       }catch (Exception e){
           try {
               if (!Helper.isAfterFromCurrentDate(dateTime + " " + data.get(position).getTime()) || data.get(position).getStatus().equals("Assigned")) {
                   return;
               }
               appUser.time = viewHolder.time.getText().toString();
               LocalRepositories.saveAppUser(context, appUser);
               row_index = position;
               notifyDataSetChanged();
           }catch ( Exception e1){

           }
       }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void equalIndex(ViewHolder viewHolder, int position){
        try {
            if (!Helper.isAfterFromCurrentDate(dateTime + " " + changeAmPm(data.get(position).getTime()))) {
                pastSlot(viewHolder);
            } else {
                if (data.get(position).getStatus().equalsIgnoreCase("assigned")){
                    assignedSlot(viewHolder);
                    return;
                }
                appUser.time=data.get(position).getTime();
                LocalRepositories.saveAppUser(context,appUser);
                b=false;
                selectedSlot(viewHolder);
            }
        }catch (Exception e){
           try {
               if (!Helper.isAfterFromCurrentDate(dateTime + " " + data.get(position).getTime())) {
                   pastSlot(viewHolder);
               } else {
                   if (data.get(position).getStatus().equalsIgnoreCase("assigned")){
                       assignedSlot(viewHolder);
                       return;
                   }
                   appUser.time=data.get(position).getTime();
                   LocalRepositories.saveAppUser(context,appUser);
                   b=false;
                   selectedSlot(viewHolder);
               }
           }catch (Exception e1){
           }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void notEqualIndex(ViewHolder viewHolder, int position){
        try {
            if (!Helper.isAfterFromCurrentDate(dateTime + " " + changeAmPm(data.get(position).getTime()))) {
                pastSlot(viewHolder);
            } else {
                if (data.get(position).getStatus().equals("Assigned")){
                    assignedSlot(viewHolder);
                    return;
                }
                if (b){
                    appUser.time=data.get(position).getTime();
                    LocalRepositories.saveAppUser(context,appUser);
                    selectedSlot(viewHolder);
                    b=false;
                    return;
                }
                freeSlot(viewHolder);
            }
        }catch (Exception e){
           try {
               if (!Helper.isAfterFromCurrentDate(dateTime + " " + data.get(position).getTime())) {
                   pastSlot(viewHolder);
               } else {
                   if (data.get(position).getStatus().equals("Assigned")){
                       assignedSlot(viewHolder);
                       return;
                   }
                   if (b){
                       appUser.time=data.get(position).getTime();
                       LocalRepositories.saveAppUser(context,appUser);
                       selectedSlot(viewHolder);
                       b=false;
                       return;
                   }
                   freeSlot(viewHolder);
               }
           }catch (Exception e1){
           }
        }
    }*/



}