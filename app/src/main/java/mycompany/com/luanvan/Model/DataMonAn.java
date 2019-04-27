
package mycompany.com.luanvan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataMonAn {

    @SerializedName("page")
    @Expose
    private String page;
    @SerializedName("numpages")
    @Expose
    private Integer numpages;
    @SerializedName("MonAns")
    @Expose
    private List<MonAn> itemMonAns = null;

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

    public List<MonAn> getItemMonAns() {
        return itemMonAns;
    }

    public void setItemMonAns(List<MonAn> itemMonAns) {
        this.itemMonAns = itemMonAns;
    }

}
