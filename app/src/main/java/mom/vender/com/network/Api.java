package mom.vender.com.network;

import java.util.Map;

import mom.vender.com.network.request.EventPushRequest;
import mom.vender.com.network.response.AddResponse;
import mom.vender.com.network.response.ChangeStatusResponse;
import mom.vender.com.network.response.GetNewOrderResponse;
import mom.vender.com.network.response.GetProfileResponse;
import mom.vender.com.network.request.RequestSignUp;
import mom.vender.com.network.response.GetStatusResponse;
import mom.vender.com.network.response.GetVersionResponse;
import mom.vender.com.network.response.HistoryResponse;
import mom.vender.com.network.response.PushNotificationResponse;
import mom.vender.com.network.response.SignUpResponse;
import mom.vender.com.network.response.SuccessResponse;
import mom.vender.com.network.response.menu_item.GetOrderMenuResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {

    @POST("/api/add/vendor/")
    Call<SignUpResponse> signUp(@Body RequestSignUp signUp);

    @POST("api/update/vendor/profile/")
    Call<SignUpResponse> update(@Body Map signUp);

    @POST("api/get/vendor/profile/")
    Call<GetProfileResponse> getProfile(@Body Map signUp);

    @POST("api/get/new/order/list/")
    Call<GetNewOrderResponse> getNewLeadOrder(@Body Map signUp);

    @POST("api/fcm/confirm/order/ ")
    Call<SuccessResponse> acceptOrder(@Body Map signUp);


    @POST("api/fcm/cancel/order/")
    Call<SuccessResponse> cancelOrder(@Body Map signUp);

    @POST("api/deliver/order/")
    Call<SuccessResponse> deliver(@Body Map signUp);

    @POST("/api/get/running/order/list/")
    Call<GetNewOrderResponse> onGoingOrder(@Body Map signUp);

    @POST("/api/get/menu/list/")
    Call<GetOrderMenuResponse> getMenuList(@Body Map signUp);

    @POST("/api/special/item/")
    Call<AddResponse> addSpecialItem(@Body Map signUp);


    @POST("api/mom/add/token/")
    Call<SuccessResponse> saveToken(@Body Map request);

    @POST("api/user/fcm/updated/")
    Call<PushNotificationResponse> pushNotification(@Body EventPushRequest request);

    @POST("/api/get/complete/order/list/")
    Call<HistoryResponse> history(@Body Map request);

    @POST("/api/check/vendor/license/")
    Call<SuccessResponse> checkLicence(@Body Map request);

    @POST("/api/status/online/change/")
    Call<ChangeStatusResponse> changeStatus(@Body Map request);

    @POST("/api/get/online/status/")
    Call<GetStatusResponse> getStatus(@Body Map request);

@POST("/api/food/status/change/")
    Call<ChangeStatusResponse> changeFoodStatus(@Body Map request);

    @POST("/customer/latest/app/version/")
    Call<GetVersionResponse> getVersion();


//    url:  https://mom-apicalls.appspot.com/api/user/fcm/
//    Method: Post
//    RequestedData: {"to":"9506228028","senderType":"","message":"","title":""}


}
