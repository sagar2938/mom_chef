package mom.vender.com.network.request;

public class RequestSignUp {
    String firstName;
    String middleName;
    String lastName;
    String email;
    String mobile;
    String otp;
    String image_name;
    String about_mom;
    String referal_code="";

    public String getAbout_mom() {
        return about_mom;
    }

    public void setAbout_mom(String about_mom) {
        this.about_mom = about_mom;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getUrl() {
        return image_name;
    }

    public void setUrl(String url) {
        this.image_name = url;
    }
}
