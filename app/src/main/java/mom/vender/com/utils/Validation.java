package mom.vender.com.utils;


import android.util.Log;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPwdFormatValid(String pwd) {
        return pwd.matches("^[0-9a-zA-Z\\@\\#\\!\\$\\%\\^\\&\\*\\(\\)\\_\\+\\=\\-\\~\\`\\/\\.]{6,24}$");
    }

    public static boolean isNameFormatValid(String name) {
        return name.matches("^[\\sa-zA-Z]{2,16}$");
    }

    public static boolean isPhoneFormatValid(String number) {
        number = number.replaceAll("[^0-9]", "");
        return number.matches("^[+]?[0-9]{10,11}$");
    }

    public static boolean isCvvValid(String cvv) {
        return cvv.matches("^[+]?[0-9]{3,4}$");
    }

    public static boolean isZipCodeValid(String zipcode) {
        return zipcode.matches("^[+]?[0-9]{5}$");
    }

    public static boolean isExpireDateValid(String expire) {
        return expire.matches("(?:0[1-9]|1[0-2])/[0-9]{2}");
    }

    public static boolean isLicencePlateValid(String info) {
        return info.matches(".{3,8}$");
    }


    public static boolean isValidphone(EditText editText, String errMsg, boolean required)
    {
        String s = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if ( required && !hasText(editText) ) return false;
        // The given argument to compile() method
        // is regular expression. With the help of
        // regular expression we can validate mobile
        // number.
        // 1) Begins with 0 or 91
        // 2) Then contains 7 or 8 or 9.
        // 3) Then contains 9 digits
        Pattern p = Pattern.compile("(0/91)?[6-9][0-9]{9}");
        Matcher m = p.matcher(s);
        boolean  matchboolean = (m.find() && m.group().equals(s));
        Log.d("BooleanTes", String.valueOf(m));
        Log.d("BooleanTesting", String.valueOf(m.matches()));
        if(matchboolean){
            return true ;
        }else {
            if(m.regionEnd()>=11 ){
                editText.setError("enter 10 digit valid mobile number");
            }else {
                editText.setError(errMsg);
            }
        }

        return matchboolean ;

    }

    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError("");
            return false;
        }

        return true;
    }
}
