//package mycompany.com.nlcn.Data;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.preference.PreferenceManager;
//import android.util.Log;
//
//import java.io.IOException;
//import java.util.HashSet;
//
//import mycompany.com.nlcn.Constant;
//import mycompany.com.nlcn.utils.SharedPreferencesHandler;
//import okhttp3.Interceptor;
//import okhttp3.Request;
//import okhttp3.Response;
//
//public class ReceivedCookiesInterceptor implements Interceptor {
//    private Context context;
//
//    public ReceivedCookiesInterceptor(Context context) {
//        this.context = context;
//    }
//
//    // AddCookiesInterceptor()
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Response originalResponse = chain.proceed(chain.request());
//
//        HashSet<String> cookies = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).getStringSet("PREF_COOKIES", new HashSet<String>());
//
//        if (!originalResponse.headers("Set-Cookie").isEmpty()&&cookies.isEmpty()) {
////            HashSet<String> cookies = (HashSet<String>) SharedPreferencesHandler.writeString(context, Constant.PREF_COOKIES);
//
//
//            for (String header : originalResponse.headers("Set-Cookie")) {
//                cookies.add(header);
//
////                Log.e("COOKIE", "ReceivedCookiesInterceptor: " + header );
//            }
//
//            SharedPreferences.Editor memes = PreferenceManager.getDefaultSharedPreferences(context).edit();
//            memes.putStringSet("PREF_COOKIES", cookies).apply();
//            memes.commit();
//            return originalResponse;
//
//        }else {
//            Request.Builder builder = chain.request().newBuilder();
//
//            HashSet<String> preferences = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).getStringSet(Constant.PREF_COOKIES, new HashSet<String>());
//
//            for (String cookie : preferences) {
//                builder.addHeader("Cookie", cookie);
////                Log.e("COOKIE", "ReceivedCookiesInterceptor: + cookies local " + cookie);
//            }
//
//            return chain.proceed(builder.build());
//        }
//
//
//    }
//}
