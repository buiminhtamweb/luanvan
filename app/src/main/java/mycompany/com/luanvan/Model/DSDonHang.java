package mycompany.com.luanvan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DSDonHang {

    @SerializedName("page")
    @Expose
    private String page;
    @SerializedName("numpages")
    @Expose

    private Integer numpages;
    @SerializedName("donhangs")
    @Expose
    private List<ItemDonhang> donhangs = null;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Integer getNumpages() {
        return numpages;
    }

    public void setNumpages(Integer numpages) {
        this.numpages = numpages;
    }

    public List<ItemDonhang> getDonhangs() {
        return donhangs;
    }

    public void setDonhangs(List<ItemDonhang> donhangs) {
        this.donhangs = donhangs;
    }

}
