package mycompany.com.luanvan.Service;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import mycompany.com.luanvan.Constant;
import mycompany.com.luanvan.utils.SharedPreferencesHandler;

import static mycompany.com.luanvan.Constant.STATUS;
import static mycompany.com.luanvan.Constant.STT_BAN_AN;


public class SocketIO extends Service implements SocketIOInterface {
    private Socket mSocket;
    private PowerManager.WakeLock mWakeLock;

    private static final String STATIC_CHANGE = "online-table-";
    private static final String DISCONNECT = "disconnect";
    private static final String CONNECTED = "connected";
    private static final String SLEEP = "sleep";
    private static final String TAG = "SOCKET-IO";

    private int mSTTBA;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        if (pm != null) {
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "12345");
        }
        mWakeLock.acquire();
        Toast.makeText(this, "on created", Toast.LENGTH_SHORT).show();
        mSTTBA = SharedPreferencesHandler.getInt(getApplicationContext(), Constant.SO_BAN);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "start command", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "onStartCommand: Socket IO");
        try {
            mSocket = IO.socket(Constant.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
//        mSocket.on("newMessageReceived", onNewMessage);
        mSocket.connect();
        if (mSocket.connected()) {
            Log.e(TAG, "onStartCommand: Socket IO --> Connected");
        }

        JSONObject data = new JSONObject();
        try {
            data.put(STT_BAN_AN, mSTTBA);
            data.put(STATUS, CONNECTED);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.emit(STATIC_CHANGE, data);

        return START_NOT_STICKY;
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

//                    JSONObject data = (JSONObject) args[0];
//                    String username;
//                    String message;
//                    try {
//                        username = data.getString("username");
//                        message = data.getString("message");
//                    } catch (JSONException e) {
//                        return;
//                    }
//

            String message = args[0].toString();
            Log.d(TAG, "call: new message ");
//            sendGeneralNotification(getApplicationContext(), "1", "new message", message, null);
        }
    };

//    private void sendGeneralNotification(Context context, String uniqueId,
//                                         String title, String contentText,
//                                         @Nullable Class<?> resultClass) {
//
//        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//
//        android.support.v7.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(context);
//        builder.setAutoCancel(true);
//
//
//        builder.setContentTitle(title);
//        builder.setContentText(contentText);
//        builder.setGroup("faskfjasfa");
//        builder.setDefaults(android.app.Notification.DEFAULT_ALL);
//        builder.setStyle(new NotificationCompat.BigTextStyle()
//                .setSummaryText(title)
//                .setBigContentTitle(title)
//                .bigText(contentText)
//        );
//
//        Intent requestsViewIntent = new Intent(context, MainActivity.class);
//        requestsViewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        requestsViewIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent requestsViewPending = PendingIntent.getActivity(context, Integer.valueOf(uniqueId), requestsViewIntent, 0);
//        builder.setContentIntent(requestsViewPending);
//        builder.setSmallIcon(R.drawable.ic_launcher);
//
//        builder.setShowWhen(true);
//        android.app.Notification notification = builder.build();
//        notificationManagerCompat.notify(Integer.valueOf(uniqueId), notification);
//    }
//
//    private Notification getNotification() {
//        Notification notification;
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setColor(getResources()
//                .getColor(R.color.material_deep_teal_500))
//                .setAutoCancel(true);
//
//        notification = builder.build();
//        notification.flags = Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_AUTO_CANCEL;
//
//        return notification;
//    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);


//        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
//
//        restartServiceIntent.setPackage(getPackageName());
//        restartServiceIntent.putExtra("NODE CONNECTED", true);
//        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
//        AlarmManager alarmService = (AlarmManager) getApplicationContext().
//                getSystemService(Context.ALARM_SERVICE);
//        alarmService.set(
//                AlarmManager.ELAPSED_REALTIME,
//                SystemClock.elapsedRealtime() + 1000,
//                restartServicePendingIntent);

        JSONObject data = new JSONObject();
        try {
            data.put("sttBanAn", mSTTBA);
            data.put("status", DISCONNECT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit(STATIC_CHANGE, data);
        mSocket.disconnect();
        mSocket.close();
        Log.e(TAG, "onTaskRemoved: Service SOCKET IO");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        JSONObject data = new JSONObject();
        try {
            data.put("sttBanAn", mSTTBA);
            data.put("status", DISCONNECT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit(STATIC_CHANGE, data);
        mSocket.disconnect();
        mSocket.close();
        Log.e(TAG, "onDestroy: Service SOCKET IO");
    }

    @Override
    public void sendData(String event, JSONObject data) {
        Log.e(TAG, "IO->sendData: " + event);
        mSocket.emit(event, data);
    }
}
