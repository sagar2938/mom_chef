package mom.vender.com.network;

import android.app.Application;
import android.content.Context;


import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import mom.vender.com.BuildConfig;
import mom.vender.com.utils.Preferences;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ThisApp extends Application {

    private static Api api;
    private static Api api2;
    private static ThisApp mInstance;

    public static synchronized ThisApp getInstance() {
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }



    public static Api getApi(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS);

        client.addInterceptor(chain -> {
            Request.Builder builder = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json");
//            String token = LocalRepositories.getInstance(context).auth_token;
            String token = Preferences.getInstance(context).getToken();
            if (null != token && !token.equals("")) {
                builder.addHeader("x-auth-token", token);
            }
            return chain.proceed(builder.build());
        });

        if (BuildConfig.DEBUG) {
            client.addInterceptor(interceptor);
        }
        client.addInterceptor(interceptor);

        if (api == null) {
            api = new Retrofit.Builder()
                    .baseUrl("https://mom-apicalls.appspot.com")
                    .client(client.build())
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                            .setLenient()
                            .create()))
                    .build()
                    .create(Api.class);
        }
        return api;
    }

    public static Api getApi(Context context, String BASE_URL) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS);

        client.addInterceptor(chain -> {
            Request.Builder builder = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json");
            String token = Preferences.getInstance(context).getToken();
            if (null != token && !token.equals("")) {
                builder.addHeader("x-auth-token", token);
            }
            return chain.proceed(builder.build());
        });

        if (BuildConfig.DEBUG) {
            client.addInterceptor(interceptor);
        }
        client.addInterceptor(interceptor);

        if (api2 == null) {
            api2 = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client.build())
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                            .setLenient()
                            .create()))
                    .build()
                    .create(Api.class);
        }
        return api2;
    }
}