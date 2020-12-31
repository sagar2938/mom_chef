package mom.vender.com.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProfileResponse {

    @SerializedName("user_data")
    @Expose
    private List<User_Data> userData = null;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public List<User_Data> getUserData() {
        return userData;
    }

    public void setUserData(List<User_Data> userData) {
        this.userData = userData;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
