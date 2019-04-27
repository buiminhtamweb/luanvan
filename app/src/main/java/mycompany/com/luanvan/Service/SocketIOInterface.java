package mycompany.com.luanvan.Service;

import org.json.JSONObject;

public interface SocketIOInterface {
    void sendData(String event, JSONObject data);
}
