package mycompany.com.luanvan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemDonhang {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("idNguoiMua")
    @Expose
    private String idNguoiMua;
    @SerializedName("ngayDatHang")
    @Expose
    private String ngayDatHang;
    
    @SerializedName("tongTien")
    @Expose
    private Integer tongTien;

    @SerializedName("soluongspmua")
    @Expose
    private Integer soluongspmua;

    @SerializedName("ngayDuyetDH")
    @Expose
    private String ngayDuyetDH;

    public String getNgayDuyetDH() {
        return ngayDuyetDH;
    }

    public void setNgayDuyetDH(String ngayDuyetDH) {
        this.ngayDuyetDH = ngayDuyetDH;
    }

    public Integer getSoluongspmua() {
        return soluongspmua;
    }

    public void setSoluongspmua(Integer soluongspmua) {
        this.soluongspmua = soluongspmua;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdNguoiMua() {
        return idNguoiMua;
    }

    public void setIdNguoiMua(String idNguoiMua) {
        this.idNguoiMua = idNguoiMua;
    }

    public String getNgayDatHang() {
        return ngayDatHang;
    }

    public void setNgayDatHang(String ngayDatHang) {
        this.ngayDatHang = ngayDatHang;
    }

    public Integer getTongTien() {
        return tongTien;
    }

    public void setTongTien(Integer tongTien) {
        this.tongTien = tongTien;
    }

}
