
package mycompany.com.luanvan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoiMon {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("sttBanAn")
    @Expose
    private Integer sttBanAn;
    @SerializedName("ngayGoiMon")
    @Expose
    private String ngayGoiMon;
    @SerializedName("soluongMA")
    @Expose
    private Integer soluongMA;
    @SerializedName("tongTien")
    @Expose
    private Integer tongTien;
    @SerializedName("daVanChuyen")
    @Expose
    private boolean daVanChuyen;

    public boolean isDaVanChuyen() {
        return daVanChuyen;
    }

    public void setDaVanChuyen(boolean daVanChuyen) {
        this.daVanChuyen = daVanChuyen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSttBanAn() {
        return sttBanAn;
    }

    public void setSttBanAn(Integer sttBanAn) {
        this.sttBanAn = sttBanAn;
    }

    public String getNgayGoiMon() {
        return ngayGoiMon;
    }

    public void setNgayGoiMon(String ngayGoiMon) {
        this.ngayGoiMon = ngayGoiMon;
    }

    public Integer getSoluongMA() {
        return soluongMA;
    }

    public void setSoluongMA(Integer soluongMA) {
        this.soluongMA = soluongMA;
    }

    public Integer getTongTien() {
        return tongTien;
    }

    public void setTongTien(Integer tongTien) {
        this.tongTien = tongTien;
    }

}
