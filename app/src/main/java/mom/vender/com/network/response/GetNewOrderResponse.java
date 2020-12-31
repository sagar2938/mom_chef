package mom.vender.com.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetNewOrderResponse {
    @SerializedName("order_data")
    @Expose
    private List<OrderDatum> orderData = null;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public List<OrderDatum> getOrderData() {
        return orderData;
    }

    public void setOrderData(List<OrderDatum> orderData) {
        this.orderData = orderData;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
