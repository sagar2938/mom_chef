package mom.vender.com.utils;

import android.content.Context;
import android.content.SharedPreferences;

import retrofit2.http.Body;

/**
 * Created by pc on 10/7/2016.
 */
public class Preferences {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    private static Preferences instance;

    private static final String BASE_URL = "base_url";
    private static final String login = "login";
    private static final String token = "token";
    private static final String OTP = "otp";
    private final String ROLE = "role";
    private final String CODE = "code";
    private final String SCHOOL_ID = "school_id";
    private final String pageNo1 = "pageNo1";
    private final String pageNo2 = "pageNo2";
    private final String userId="userId";
    private final String firstTimeAdminLogin = "firstTimeAdminLogin";
    private final String classId = "classId";
    private final String teacherId = "teacherId";
    private final String mobileNumber = "mobile";
    private final String email = "email";
    private final String profileStatus = "profileStatus";
    private final String firstName = "firstName";
    private final String middleName = "middleName";
    private final String lastName = "lastName";
    private final String image_name = "image_name";
    private final String profileCompleteStatus = "completeStatus" ;
    private final String profileImage = "profileImage" ;
    private final String todayDate = "todayDate" ;
    private final String todaySpecial = "todaySpecial" ;
    private final String customerMobile = "customerMobile" ;

    private static final String KeyIdType = "idType";

    public  final String KEY_SAVED_OTP = "saved_otp";


    private Preferences(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences("My_pref", 0);
        editor = pref.edit();
    }


    public static Preferences getInstance(Context context) {
        if (instance == null) {
            instance = new Preferences(context);
        }
        return instance;
    }


    public void setToken(String cname) {
        editor.putString(token, cname);
        editor.commit();
    }

    public String getToken() {
        return pref.getString(token, "");
    }


    public void setLogin(Boolean cname) {
        editor.putBoolean(login, cname);
        editor.commit();
    }

    public boolean isLogin() {
        return pref.getBoolean(login, false);
    }


    public void setOtp(String cname) {
        editor.putString(OTP, cname);
        editor.commit();
    }

    public String getOtp() {
        return pref.getString(OTP, "");
    }

    public void setROLE(String role) {
        editor.putString(ROLE, role);
        editor.commit();
    }

    public String getROLE() {
        return pref.getString(ROLE, "");
    }

    public void setBaseUrl(String baseUrl) {
        editor.putString(BASE_URL, baseUrl);
        editor.commit();
    }

    public String getBaseUrl() {
        return pref.getString(BASE_URL, "");
    }


    public void setCode(String cname) {
        editor.putString(CODE, cname);
        editor.commit();
    }

    public String getCode() {
        return pref.getString(CODE, "");
    }

    public void setPageNo1(int cname) {
        editor.putInt(pageNo1, cname);
        editor.commit();
    }

    public int getPageNo1() {
        return pref.getInt(pageNo1, 0);
    }


    public void setPageNo2(int cname) {
        editor.putInt(pageNo2, cname);
        editor.commit();
    }

    public int getPageNo2() {
        return pref.getInt(pageNo2, 0);
    }

    public void setSchoolId(String cname) {
        editor.putString(SCHOOL_ID, cname);
        editor.commit();
    }

    public String getSchoolId() {
        return pref.getString(SCHOOL_ID, "");
    }

public void setUserId(String cname) {
        editor.putString(userId, cname);
        editor.commit();
    }

    public String getUserId() {
        return pref.getString(userId, "");
    }


    public void setClassId(String cname) {
        editor.putString(classId, cname);
        editor.commit();
    }

    public String getClassId() {
        return pref.getString(classId, "");
    }


    public void setTeacherId(String cname) {
        editor.putString(teacherId, cname);
        editor.commit();
    }

    public String getTeacherId() {
        return pref.getString(teacherId, "");
    }


    public void setFirstTimeAdminLogin(int cname) {
        editor.putInt(firstTimeAdminLogin, cname);
        editor.commit();
    }

    public int getFirstTimeAdminLogin() {
        return pref.getInt(firstTimeAdminLogin, 0);
    }

    public void setMobileNumber(String cname) {
        editor.putString(mobileNumber, cname);
        editor.commit();
    }

    public String getMobileNumber() {
        return pref.getString(mobileNumber, "");
    }

    public void setProfileStatus(int cname) {
        editor.putInt(profileStatus,0);
        editor.commit();
    }


    public void setKeyIdType(String cname) {
        editor.putString(KeyIdType, cname);
        editor.commit();
    }

    public String getKeyIdType() {
        return pref.getString(KeyIdType, "");
    }

    public void setProfileCompleteStatus(String cname) {
        editor.putString(profileCompleteStatus, cname);
        editor.commit();
    }

    public String getProfileCompleteStatus() {
        return pref.getString(profileCompleteStatus, "");
    }

    public void setProfileStatus(String cname) {
        editor.putString(profileStatus, cname);
        editor.commit();
    }

    public String getProfileStatus() {
        return pref.getString(profileStatus, "");
    }



    public void setFirstName(String cname) {
        editor.putString(firstName, cname);
        editor.commit();
    }

    public String getFirstName() {
        return pref.getString(firstName, "");
    }



    public void setMiddleName(String cname) {
        editor.putString(middleName, cname);
        editor.commit();
    }

    public String getMiddleName() {
        return pref.getString(middleName, "");
    }


    public void setLastName(String cname) {
        editor.putString(lastName, cname);
        editor.commit();
    }

    public String getLastName() {
        return pref.getString(lastName, "");
    }


    public void setImageName(String cname) {
        editor.putString(image_name, cname);
        editor.commit();
    }

    public String getImageName() {
        return pref.getString(image_name, "");
    }





    public void setEmail(String cname) {
        editor.putString(email, cname);
        editor.commit();
    }

    public String getEmail() {
        return pref.getString(email, "");
    }



    public void setProfileImage(String cname) {
        editor.putString(profileImage, cname);
        editor.commit();
    }

    public String getProfileImage() {
        return pref.getString(profileImage, "");
    }



    public void setTodayDate(String cname) {
        editor.putString(todayDate, cname);
        editor.commit();
    }

    public String getTodayDate() {
        return pref.getString(todayDate, "");
    }




    public void setTodaySpecial(boolean cname) {
        editor.putBoolean(todaySpecial, cname);
        editor.commit();
    }

    public boolean getTodaySpecial() {
        return pref.getBoolean(todaySpecial, false);
    }


    public void setCustomerMobile(String cname) {
        editor.putString(customerMobile, cname);
        editor.commit();
    }

    public String getCustomerMobile() {
        return pref.getString(customerMobile, "");
    }






}