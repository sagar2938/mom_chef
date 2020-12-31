package mom.vender.com.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import mom.vender.com.R;
import mom.vender.com.adapter.OnGoingAdapter;
import mom.vender.com.base.BaseFragment;
import mom.vender.com.network.ApiCallService;
import mom.vender.com.network.request.EventPushRequest;
import mom.vender.com.network.response.GetNewOrderResponse;
import mom.vender.com.network.response.PushNotificationResponse;
import mom.vender.com.network.response.SuccessResponse;
import mom.vender.com.utils.Preferences;

public class OnGoingFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_targets, null);

        recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Map map = new HashMap();
        map.put("mobile", Preferences.getInstance(getContext()).getMobileNumber());
        ApiCallService.action(getActivity(), map, ApiCallService.Action.ACTION_GET_ON_GOING);
    }

    @Subscribe
    public void getNewLead(GetNewOrderResponse response) {
        swipeRefreshLayout.setRefreshing(false);
        if (response.getSuccess()) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new OnGoingAdapter(getActivity(), response.getOrderData()));
        } else {
            getDialogSorry();
        }

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        Map map = new HashMap();
        map.put("mobile", Preferences.getInstance(getContext()).getMobileNumber());
        ApiCallService.action(getActivity(), map, ApiCallService.Action.ACTION_GET_ON_GOING);
    }


    @Subscribe
    public void AcceptCancel(SuccessResponse response) {
        if (response.getResponse().getConfirmation() == 1) {
            getDialogSuccess(response.getResponse().getOrderId() + " " + response.getResponse().getMessage());
            onRefresh();
        } else {
            getDialogSorry();
        }

    }

}
