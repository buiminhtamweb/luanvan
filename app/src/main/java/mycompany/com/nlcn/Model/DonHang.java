
package mycompany.com.nlcn.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DonHang {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("idNguoiMua")
    @Expose
    private String idNguoiMua;
    @SerializedName("spMua")
    @Expose
    private List<SpMua> spMua = null;
    @SerializedName("tongTien")
    @Expose
    private Integer tongTien;
    @SerializedName("ngayDatHang")
    @Expose
    private String ngayDatHang;
    @SerializedName("daDuyet")
    @Expose
    private Boolean daDuyet;
    @SerializedName("__v")
    @Expose
    private Integer v;

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

    public List<SpMua> getSpMua() {
        return spMua;
    }

    public void setSpMua(List<SpMua> spMua) {
        this.spMua = spMua;
    }

    public Integer getTongTien() {
        return tongTien;
    }

    public void setTongTien(Integer tongTien) {
        this.tongTien = tongTien;
    }

    public String getNgayDatHang() {
        return ngayDatHang;
    }

    public void setNgayDatHang(String ngayDatHang) {
        this.ngayDatHang = ngayDatHang;
    }

    public Boolean getDaDuyet() {
        return daDuyet;
    }

    public void setDaDuyet(Boolean daDuyet) {
        this.daDuyet = daDuyet;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}
