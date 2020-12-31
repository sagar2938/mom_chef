package mom.vender.com.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductList {
    @SerializedName("itemId")
    @Expose
    private String itemId;
    @SerializedName("itemActualPrice")
    @Expose
    private String itemActualPrice;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("pType")
    @Expose
    private String pType;
    @SerializedName("food_item")
    @Expose
    private String foodItem;
    String image;

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemActualPrice() {
        return itemActualPrice;
    }

    public void setItemActualPrice(String itemActualPrice) {
        this.itemActualPrice = itemActualPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPType() {
        return pType;
    }

    public void setPType(String pType) {
        this.pType = pType;
    }

    public String getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }


}
