
package mycompany.com.luanvan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemSPDonHang {

    @SerializedName("tensp")
    @Expose
    private String tensp;
    @SerializedName("imgurl")
    @Expose
    private String imgurl;

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

}
