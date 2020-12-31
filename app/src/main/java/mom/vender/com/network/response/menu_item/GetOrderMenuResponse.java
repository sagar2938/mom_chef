package mom.vender.com.network.response.menu_item;

import java.util.List;

public class GetOrderMenuResponse {

    List<Menu_data> menu_data;

    public List<Menu_data> getMenu_data() {
        return menu_data;
    }

    public void setMenu_data(List<Menu_data> menu_data) {
        this.menu_data = menu_data;
    }
}
