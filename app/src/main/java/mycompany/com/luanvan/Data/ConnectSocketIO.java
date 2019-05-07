package mycompany.com.luanvan.Data;

import android.content.Context;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import mycompany.com.luanvan.Constant;
import mycompany.com.luanvan.Fragment.ThanhToanInterface;
import mycompany.com.luanvan.R;
import mycompany.com.luanvan.utils.SharedPreferencesHandler;

import static mycompany.com.luanvan.Constant.NOTIFI_ID;
import static mycompany.com.luanvan.Constant.SOCKET_DA_THANH_TOAN;
import static mycompany.com.luanvan.Constant.SOCKET_VAN_CHUYEN;
import static mycompany.com.luanvan.Constant.SOCKET_VAN_CHUYEN_HOAN_THANH;
import static mycompany.com.luanvan.Constant.STATUS;
import static mycompany.com.luanvan.Constant.STT_BAN_AN;


public class ConnectSocketIO {

    private static final String STATIC_CHANGE = "online-table-";
    private static final String DISCONNECT = "disconnect";
    private static final String CONNECTED = "connected";
    private static final String SLEEP = "sleep";
    private static final String TAG = "ConnectSocketIO";
    private static int mSTTBA;
    private static NotificationManagerCompat notificationManager;
    private NotificationCompat.Builder mBuilder;
    private ThanhToanInterface mThanhToanInterface;

    private static ConnectSocketIO mConnectSocketIO;
    private static Socket mSocket;

    private ConnectSocketIO(final Context context) {

        try {
            mSocket = IO.socket(Constant.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.connect();
        mSTTBA = SharedPreferencesHandler.getInt(context, Constant.SO_BAN);
        JSONObject data = new JSONObject();
        try {
            data.put(STT_BAN_AN, mSTTBA);
            data.put(STATUS, CONNECTED);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        notificationManager = NotificationManagerCompat.from(context);
        mBuilder = new NotificationCompat.Builder(context, NOTIFI_ID + "")
                .setSmallIcon(R.drawable.logo_white)
                .setContentTitle("Bàn số " + mSTTBA)
                .setContentText("Thực phẩm đang được vận chuyển")
                .setVibrate(new long[]{1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        mSocket.on(SOCKET_VAN_CHUYEN + mSTTBA, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                notificationManager.notify(NOTIFI_ID, mBuilder.build());
            }
        });

        mSocket.on(SOCKET_VAN_CHUYEN_HOAN_THANH + mSTTBA, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                notificationManager.cancelAll();
            }
        });

        mSocket.on(SOCKET_DA_THANH_TOAN + mSTTBA, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                mThanhToanInterface.reloadDataServer();
            }
        });


        mSocket.emit(STATIC_CHANGE, data);


    }

    public static ConnectSocketIO getInstance(Context context) {

        if (mConnectSocketIO == null) {
            Log.e(TAG, "getInstance: new ConnectServer ");
            mConnectSocketIO = new ConnectSocketIO(context);
        }
        return mConnectSocketIO;
    }

    public static void destroy() {
        JSONObject data = new JSONObject();
        try {
            data.put(STT_BAN_AN, mSTTBA);
            data.put(STATUS, DISCONNECT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mSocket.connected()) {
            mSocket.emit(STATIC_CHANGE, data);
        }

        mSocket.disconnect();
        mSocket.close();
        mSocket = null;
        mConnectSocketIO = null;
        notificationManager.cancel(NOTIFI_ID);
        Log.e(TAG, "Destroy mConnectSocketIO ");
    }

    public void sendData(String event, String key, String value) {
        Log.e(TAG, "IO->sendData: " + event);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(STT_BAN_AN, mSTTBA);
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mSocket.connected()) {
            mSocket.emit(event, jsonObject);
        }

    }

    public void sendData(String event, String key, boolean value) {
        Log.e(TAG, "IO->sendData: " + event);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(STT_BAN_AN, mSTTBA);
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mSocket.connected()) {
            mSocket.emit(event, jsonObject);
        }
    }

    public void sendData(String event, String key, int value) {
        Log.e(TAG, "IO->sendData: " + event);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(STT_BAN_AN, mSTTBA);
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mSocket.connected()) {
            mSocket.emit(event, jsonObject);
        }
    }

}
