
package mycompany.com.luanvan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MonAn {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("tenMA")
    @Expose
    private String tenMA;
    @SerializedName("giaMA")
    @Expose
    private Integer giaMA;
    @SerializedName("imgurl")
    @Expose
    private String imgurl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenMA() {
        return tenMA;
    }

    public void setTenMA(String tenMA) {
        this.tenMA = tenMA;
    }

    public Integer getGiaMA() {
        return giaMA;
    }

    public void setGiaMA(Integer giaMA) {
        this.giaMA = giaMA;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
