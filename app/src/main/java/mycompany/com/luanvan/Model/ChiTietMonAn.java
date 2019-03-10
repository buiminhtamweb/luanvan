package mycompany.com.luanvan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChiTietMonAn extends ItemMonAn {

    @SerializedName("chitietMA")
    @Expose
    private String chitietsp;

    public ChiTietMonAn() {
    }

    public ChiTietMonAn(String chitietsp) {
        this.chitietsp = chitietsp;
    }

    public String getChitietsp() {
        return chitietsp;
    }

    public void setChitietsp(String chitietsp) {
        this.chitietsp = chitietsp;
    }
}
