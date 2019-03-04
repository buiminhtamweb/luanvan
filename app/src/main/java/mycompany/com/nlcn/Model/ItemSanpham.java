
package mycompany.com.nlcn.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemSanpham {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("tensp")
    @Expose
    private String tensp;
    @SerializedName("giasp")
    @Expose
    private Integer giasp;
    @SerializedName("sanluong")
    @Expose
    private Integer sanluong;
    @SerializedName("imgurl")
    @Expose
    private String imgurl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public Integer getGiasp() {
        return giasp;
    }

    public void setGiasp(Integer giasp) {
        this.giasp = giasp;
    }

    public Integer getSanluong() {
        return sanluong;
    }

    public void setSanluong(Integer sanluong) {
        this.sanluong = sanluong;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

}
