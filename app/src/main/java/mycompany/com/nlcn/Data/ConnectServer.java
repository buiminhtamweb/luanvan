package mycompany.com.nlcn.Data;

import android.content.Context;
import android.util.Log;

import java.net.CookieHandler;

import mycompany.com.nlcn.Constant;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectServer {
    private static ConnectServer mConnectServer;
    private Retrofit mRetrofit;
    private API mApi;


    private ConnectServer(Context context) {
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.addInterceptor(new AddCookiesInterceptor(context)); // VERY VERY IMPORTANT
//        builder.addInterceptor(new ReceivedCookiesInterceptor(context)); // VERY VERY IMPORTANT
//        OkHttpClient mClient = builder.build();
//        CookieJar cookieHandler = mClient.cookieJar();
//        cookieHandler.toString();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL_SERVER + "api/")
//                .client(mClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApi = mRetrofit.create(API.class);
    }

    public static ConnectServer getInstance(Context context) {

        if (mConnectServer == null) {
            Log.e("SV", "getInstance: new ConnectServer "  );
            mConnectServer = new ConnectServer(context);
        }
        return mConnectServer;
    }

    public static void destroy() {
        mConnectServer = null;
        Log.e("SV", "Destroy ConnectServer "  );
    }

    public API getApi() {
        return mApi;
    }

}
