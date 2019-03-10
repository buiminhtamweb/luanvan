
package mycompany.com.luanvan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAcc {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("sdt")
    @Expose
    private String sdt;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("diachi")
    @Expose
    private String diachi;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("namsinh")
    @Expose
    private String namsinh;

    @SerializedName("gioitinh")
    @Expose
    private String gioitinh;

    public String getNamsinh() {
        return namsinh;
    }

    public void setNamsinh(String namsinh) {
        this.namsinh = namsinh;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
