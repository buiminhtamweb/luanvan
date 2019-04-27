
package mycompany.com.luanvan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoiMonDetail {

    @SerializedName("daVanChuyen")
    @Expose
    private Boolean daVanChuyen;
    @SerializedName("daThanhToan")
    @Expose
    private Boolean daThanhToan;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("monAn")
    @Expose
    private List<ItemMonAn> monAn = null;
    @SerializedName("ngayGoiMon")
    @Expose
    private String ngayGoiMon;
    @SerializedName("sttBanAn")
    @Expose
    private Integer sttBanAn;
    @SerializedName("tongTien")
    @Expose
    private Integer tongTien;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public Boolean getDaVanChuyen() {
        return daVanChuyen;
    }

    public void setDaVanChuyen(Boolean daVanChuyen) {
        this.daVanChuyen = daVanChuyen;
    }

    public Boolean getDaThanhToan() {
        return daThanhToan;
    }

    public void setDaThanhToan(Boolean daThanhToan) {
        this.daThanhToan = daThanhToan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ItemMonAn> getMonAn() {
        return monAn;
    }

    public void setMonAn(List<ItemMonAn> monAn) {
        this.monAn = monAn;
    }

    public String getNgayGoiMon() {
        return ngayGoiMon;
    }

    public void setNgayGoiMon(String ngayGoiMon) {
        this.ngayGoiMon = ngayGoiMon;
    }

    public Integer getSttBanAn() {
        return sttBanAn;
    }

    public void setSttBanAn(Integer sttBanAn) {
        this.sttBanAn = sttBanAn;
    }

    public Integer getTongTien() {
        return tongTien;
    }

    public void setTongTien(Integer tongTien) {
        this.tongTien = tongTien;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}
