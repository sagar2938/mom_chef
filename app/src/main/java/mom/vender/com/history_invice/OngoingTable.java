package mom.vender.com.history_invice;

public class OngoingTable {

    public String orderId, total_price, mom_mobile, mobile, price, longitude, deliver_number, location
            , latitude, id,createdAt;



    public OngoingTable(String orderId, String total_price, String mom_mobile, String mobile, String price, String longitude, String deliver_number, String location, String latitude, String id,String createdAt) {
        this.orderId = orderId;
        this.total_price = total_price;
        this.mom_mobile = mom_mobile;
        this.mobile = mobile;
        this.price = price;
        this.longitude = longitude;
        this.deliver_number = deliver_number;
        this.location = location;
        this.latitude = latitude;
        this.id = id;
        this.createdAt=createdAt;
    }
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getMom_mobile() {
        return mom_mobile;
    }

    public void setMom_mobile(String mom_mobile) {
        this.mom_mobile = mom_mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDeliver_number() {
        return deliver_number;
    }

    public void setDeliver_number(String deliver_number) {
        this.deliver_number = deliver_number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
