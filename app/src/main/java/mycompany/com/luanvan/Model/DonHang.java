
package mycompany.com.luanvan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DonHang {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("sttBanAn")
    @Expose
    private String sttBanAn;
    @SerializedName("monAn")
    @Expose
    private List<SpMua> monAn = null;
    @SerializedName("tongTien")
    @Expose
    private Integer tongTien;
    @SerializedName("ngayGoiMon")
    @Expose
    private String ngayGoiMon;
    @SerializedName("daThanhToan")
    @Expose
    private Boolean daThanhToan;
    @SerializedName("ngayThanhToan")
    @Expose
    private Boolean ngayThanhToan;

    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSttBanAn() {
        return sttBanAn;
    }

    public void setSttBanAn(String sttBanAn) {
        this.sttBanAn = sttBanAn;
    }

    public List<SpMua> getMonAn() {
        return monAn;
    }

    public void setMonAn(List<SpMua> monAn) {
        this.monAn = monAn;
    }

    public Integer getTongTien() {
        return tongTien;
    }

    public void setTongTien(Integer tongTien) {
        this.tongTien = tongTien;
    }

    public String getNgayGoiMon() {
        return ngayGoiMon;
    }

    public void setNgayGoiMon(String ngayGoiMon) {
        this.ngayGoiMon = ngayGoiMon;
    }

    public Boolean getDaThanhToan() {
        return daThanhToan;
    }

    public void setDaThanhToan(Boolean daThanhToan) {
        this.daThanhToan = daThanhToan;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}
