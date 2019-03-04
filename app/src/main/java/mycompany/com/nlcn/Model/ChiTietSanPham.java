package mycompany.com.nlcn.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChiTietSanPham extends ItemSanpham {

    @SerializedName("chitietsp")
    @Expose
    private String chitietsp;

    public ChiTietSanPham() {
    }

    public ChiTietSanPham(String chitietsp) {
        this.chitietsp = chitietsp;
    }

    public String getChitietsp() {
        return chitietsp;
    }

    public void setChitietsp(String chitietsp) {
        this.chitietsp = chitietsp;
    }
}
