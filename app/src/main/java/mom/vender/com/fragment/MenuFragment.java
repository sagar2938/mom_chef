package mom.vender.com.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import mom.vender.com.R;
import mom.vender.com.activity.AddNewOrderActivity;
import mom.vender.com.adapter.MenuAdapter;
import mom.vender.com.base.BaseActivity;
import mom.vender.com.base.BaseFragment;
import mom.vender.com.model.MenuModel;
import mom.vender.com.model.OnNewOrderClick;
import mom.vender.com.network.response.ChangeStatusResponse;
import mom.vender.com.network.volley.NetworkCallBacks.ResponseCallback;
import mom.vender.com.network.volley.WebserviceHelper;
import mom.vender.com.utils.Constants;
import mom.vender.com.utils.CustomProgressDialog;
import mom.vender.com.utils.Preferences;


public class MenuFragment extends BaseFragment implements OnNewOrderClick, SwipeRefreshLayout.OnRefreshListener {



    private List<MenuModel> newLeadTables;
    private MenuAdapter allNewLeadAdapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;


    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, null);
        recyclerView = view.findViewById(R.id.list);
        swipeRefreshLayout = view.findViewById(R.id.swipe_rf);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimaryDark, R.color.colorPrimary);
        return view;

    }


    @Override
    public void onResume() {
        super.onResume();
        fetchMenuItemsList();

    }

    private void fetchMenuItemsList() {
        newLeadTables = new ArrayList<>();
        CustomProgressDialog.getInstance(getActivity()).show();
        JSONObject jsonObject = new JSONObject();
        String url = Constants.BASE_URL + "api/get/menu/list/";
        try {
            jsonObject.put("mobile", Preferences.getInstance(getContext()).getMobileNumber());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        WebserviceHelper.getInstance().PostCall(getContext(), url, jsonObject, new ResponseCallback() {
            @Override
            public void OnSuccessFull(JSONObject Response) {
                CustomProgressDialog.setDismiss();
                System.out.println("http request : "+jsonObject);
                System.out.println("http url : "+url);
                System.out.println("http response : "+Response);
                if (Response != null) {
                    Log.d("PendingResponse", String.valueOf(Response));
                    try {
                        swipeRefreshLayout.setRefreshing(false);

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONArray jsonArray = Response.getJSONArray("menu_data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String imageUrl="";
                            try {
                                imageUrl = jsonObject1.getString("image_path")+jsonObject1.getString("item_image");
                            }catch (Exception e){

                            }
                            MenuModel menuModel = new MenuModel(jsonObject1.getInt("id"), jsonObject1.getString("itemDescription"),
                                    jsonObject1.getString("food_type"), jsonObject1.getString("itemGroup"), jsonObject1.getString("itemName"),imageUrl);
                            menuModel.setItem_image(jsonObject1.getString("item_image"));
                            menuModel.setFullPrice(jsonObject1.getString("fullPrice"));
                            menuModel.setItemPreparationTime(jsonObject1.getString("itemPreparationTime"));
                            menuModel.setStatus(jsonObject1.getInt("status"));
                            newLeadTables.add(menuModel);
                        }


                        allNewLeadAdapter = new MenuAdapter(newLeadTables, getActivity(), MenuFragment.this);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(allNewLeadAdapter);
                        allNewLeadAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    swipeRefreshLayout.setRefreshing(false);

                }

            }
        });
    }

    private void deleteItem(int id) {
        JSONObject jsonObject = new JSONObject();
        String url = Constants.BASE_URL + "api/delete/vendor/menu/";
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        WebserviceHelper.getInstance().PostCall(getContext(), url, jsonObject, new ResponseCallback() {
            @Override
            public void OnSuccessFull(JSONObject Response) {
                if (Response != null) {
                    fetchMenuItemsList();
                    Toast.makeText(getContext(), "Item deleted successfully !", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Item delete Some thing went wrong !", Toast.LENGTH_SHORT).show();


                }

            }
        });
    }


    @Override
    public void onClick(int id) {
        getDialog("Delete","Do you really want to delete this item?",id);
//        deleteItem(id);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        fetchMenuItemsList();

    }


    public void getDialog(String tittle, String message,int id) {
        new AlertDialog.Builder(getContext())
                .setTitle(tittle)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem(id);
                    }
                })
                .setNegativeButton("Cancel", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }


    @Subscribe
    public void changeStatus(ChangeStatusResponse response) {

//        Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
    }
}
