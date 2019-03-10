package mycompany.com.luanvan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResLogin {

    @SerializedName("SERVER_RESPONSE")
    @Expose
    private Integer sERVERRESPONSE;
    @SerializedName("SERVER_MESSAGE")
    @Expose
    private String sERVERMESSAGE;

    @SerializedName("TOKEN")
    @Expose
    private String TOKEN;

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public Integer getSERVERRESPONSE() {
        return sERVERRESPONSE;
    }

    public void setSERVERRESPONSE(Integer sERVERRESPONSE) {
        this.sERVERRESPONSE = sERVERRESPONSE;
    }

    public String getSERVERMESSAGE() {
        return sERVERMESSAGE;
    }

    public void setSERVERMESSAGE(String sERVERMESSAGE) {
        this.sERVERMESSAGE = sERVERMESSAGE;
    }

}
