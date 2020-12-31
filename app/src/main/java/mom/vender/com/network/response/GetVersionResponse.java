package mom.vender.com.network.response;

import java.util.List;

public class GetVersionResponse {


    boolean success;
    List<App_data> app_data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<App_data> getApp_data() {
        return app_data;
    }

    public void setApp_data(List<App_data> app_data) {
        this.app_data = app_data;
    }


    /*
{"app_data": [{"appName": "MOM", "updateOn": "2020-02-18 11:55:00", "flag": 0, "version": "1.0.23",
 "versionCode": "23", "id": 1}], "success": true}

 */

    public class App_data{
        String appName;
        String updateOn;
        int flag;
        String version;
        String versionCode;
        String id;
        String momCheffVersion;


        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getUpdateOn() {
            return updateOn;
        }

        public void setUpdateOn(String updateOn) {
            this.updateOn = updateOn;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMomCheffVersion() {
            return momCheffVersion;
        }

        public void setMomCheffVersion(String momCheffVersion) {
            this.momCheffVersion = momCheffVersion;
        }
    }
}
