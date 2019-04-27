package mycompany.com.luanvan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemMonAn {

    @SerializedName("idMonAn")
    @Expose
    private String idMonAn;
    @SerializedName("tenMA")
    @Expose
    private String tenMA;
    @SerializedName("giaMA")
    @Expose
    private Integer giaMA;
    @SerializedName("imgurl")
    @Expose
    private String imgurl;
    @SerializedName("soLuongMua")
    @Expose
    private Integer soLuongMua;

    public String getIdMonAn() {
        return idMonAn;
    }

    public void setIdMonAn(String idMonAn) {
        this.idMonAn = idMonAn;
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

    public Integer getSoLuongMua() {
        return soLuongMua;
    }

    public void setSoLuongMua(Integer soLuongMua) {
        this.soLuongMua = soLuongMua;
    }
}
