
package mycompany.com.luanvan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpMua {

    @SerializedName("idMonAn")
    @Expose
    private String idMonAn;
    @SerializedName("giaMua")
    @Expose
    private Integer giaMua;
    @SerializedName("soLuongMua")
    @Expose
    private Integer soLuongMua;

    public String getIdMonAn() {
        return idMonAn;
    }

    public void setIdMonAn(String idMonAn) {
        this.idMonAn = idMonAn;
    }

    public Integer getGiaMua() {
        return giaMua;
    }

    public void setGiaMua(Integer giaMua) {
        this.giaMua = giaMua;
    }

    public Integer getSoLuongMua() {
        return soLuongMua;
    }

    public void setSoLuongMua(Integer soLuongMua) {
        this.soLuongMua = soLuongMua;
    }

}
