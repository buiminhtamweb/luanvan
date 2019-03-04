package mycompany.com.nlcn.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DonHangRes {

    @SerializedName("daDuyet")
    @Expose
    private Boolean daDuyet;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("spMua")
    @Expose
    private List<SpMua> spMua = null;
    @SerializedName("ngayDatHang")
    @Expose
    private String ngayDatHang;
    @SerializedName("idNguoiMua")
    @Expose
    private String idNguoiMua;
    @SerializedName("ngayDuyetDH")
    @Expose
    private String ngayDuyetDH;
    @SerializedName("tongTien")
    @Expose
    private Integer tongTien;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getNgayDuyetDH() {
        return ngayDuyetDH;
    }

    public void setNgayDuyetDH(String ngayDuyetDH) {
        this.ngayDuyetDH = ngayDuyetDH;
    }

    public Boolean getDaDuyet() {
        return daDuyet;
    }

    public void setDaDuyet(Boolean daDuyet) {
        this.daDuyet = daDuyet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SpMua> getSpMua() {
        return spMua;
    }

    public void setSpMua(List<SpMua> spMua) {
        this.spMua = spMua;
    }

    public String getNgayDatHang() {
        return ngayDatHang;
    }

    public void setNgayDatHang(String ngayDatHang) {
        this.ngayDatHang = ngayDatHang;
    }

    public String getIdNguoiMua() {
        return idNguoiMua;
    }

    public void setIdNguoiMua(String idNguoiMua) {
        this.idNguoiMua = idNguoiMua;
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
