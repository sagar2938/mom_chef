package mom.vender.com.model;

public class MenuModel {
   public int id;
   public String itemDescription;
    public String food_type;
    public String itemGroup;
    public String itemName;
    public String itemUrl ;
    String fullPrice;
    String item_image;
    String itemPreparationTime;
    int status;


    public String getItemPreparationTime() {
        return itemPreparationTime;
    }

    public void setItemPreparationTime(String itemPreparationTime) {
        this.itemPreparationTime = itemPreparationTime;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public MenuModel(int id, String itemDescription, String food_type, String itemGroup, String itemName) {
        this.id = id;
        this.itemDescription = itemDescription;
        this.food_type = food_type;
        this.itemGroup = itemGroup;
        this.itemName = itemName;
    }

    public MenuModel(int id, String itemDescription, String food_type, String itemGroup, String itemName,String itemUrl) {
        this.id = id;
        this.itemDescription = itemDescription;
        this.food_type = food_type;
        this.itemGroup = itemGroup;
        this.itemName = itemName;
        this.itemUrl = itemUrl ;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public String getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(String fullPrice) {
        this.fullPrice = fullPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
