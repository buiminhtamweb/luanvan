
package mycompany.com.nlcn.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpMua {

    @SerializedName("idSpMua")
    @Expose
    private String idSpMua;
    @SerializedName("giaMua")
    @Expose
    private Integer giaMua;
    @SerializedName("sanLuongMua")
    @Expose
    private Integer sanLuongMua;

    public String getIdSpMua() {
        return idSpMua;
    }

    public void setIdSpMua(String idSpMua) {
        this.idSpMua = idSpMua;
    }

    public Integer getGiaMua() {
        return giaMua;
    }

    public void setGiaMua(Integer giaMua) {
        this.giaMua = giaMua;
    }

    public Integer getSanLuongMua() {
        return sanLuongMua;
    }

    public void setSanLuongMua(Integer sanLuongMua) {
        this.sanLuongMua = sanLuongMua;
    }

}
