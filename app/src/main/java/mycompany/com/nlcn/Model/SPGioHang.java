
package mycompany.com.nlcn.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SPGioHang {

    @SerializedName("idSpMua")
    @Expose
    private String idSpMua;
    @SerializedName("tensp")
    @Expose
    private String tensp;
    @SerializedName("giasp")
    @Expose
    private Integer giasp;
    @SerializedName("imgurl")
    @Expose
    private String imgurl;
    @SerializedName("sanluong")
    @Expose
    private Integer sanluong;
    @SerializedName("sanLuongMua")
    @Expose
    private Integer sanLuongMua;

    public String getIdSpMua() {
        return idSpMua;
    }

    public void setIdSpMua(String idSpMua) {
        this.idSpMua = idSpMua;
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

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public Integer getSanluong() {
        return sanluong;
    }

    public void setSanluong(Integer sanluong) {
        this.sanluong = sanluong;
    }

    public Integer getSanLuongMua() {
        return sanLuongMua;
    }

    public void setSanLuongMua(Integer sanLuongMua) {
        this.sanLuongMua = sanLuongMua;
    }

}
