package mom.vender.com.network.response;

import java.util.List;

public class HistoryResponse {
    List<OrderDatum> order_data;
    boolean success;


    public List<OrderDatum> getOrder_data() {
        return order_data;
    }

    public void setOrder_data(List<OrderDatum> order_data) {
        this.order_data = order_data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
