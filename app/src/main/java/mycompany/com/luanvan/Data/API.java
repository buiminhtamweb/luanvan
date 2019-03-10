package mycompany.com.luanvan.Data;

import java.util.List;

import mycompany.com.luanvan.Model.ChiTietMonAn;
import mycompany.com.luanvan.Model.DSDonHang;
import mycompany.com.luanvan.Model.DataMonAn;
import mycompany.com.luanvan.Model.DonHang;
import mycompany.com.luanvan.Model.DonHangRes;
import mycompany.com.luanvan.Model.ItemSPDonHang;
import mycompany.com.luanvan.Model.Message;
import mycompany.com.luanvan.Model.ResLogin;
import mycompany.com.luanvan.Model.SPGioHang;
import mycompany.com.luanvan.Model.UserAcc;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {


    //Sản phẩm
    @GET("sanphams")
    Call<DataMonAn> getDSMonAn(@Query("page") int page);

    @GET("sanphams")
    Call<DataMonAn> timKiem(@Query("keyWord") String keyWord, @Query("page") int page);

    @GET("sanphams/{idSanPham}")
    Call<ChiTietMonAn> getChiTietSanPham(@Path("idSanPham") String idSanPham);

    //Tài khoản người dùng
    @FormUrlEncoded
    @POST("signup")
    Call<Message> registerAcc(@Field("name") String name,
                              @Field("username") String username,
                              @Field("password") String password,
                              @Field("sdt") String sdt,
                              @Field("diachi") String diachi);

    @FormUrlEncoded
    @POST("users/login")
    Call<ResLogin> signInAcc(@Header("Authorization") String token,
                             @Field("username") String username,
                             @Field("password") String password);

    @GET("users/{username}")
    Call<UserAcc> layThongTinNguoiDung(@Header("Authorization") String token,
                                       @Path("username") String username);


    @FormUrlEncoded
    @PUT("users/{username}")
    Call<Message> capNhatMatKhau(@Header("Authorization") String token,
                                 @Path("username") String username,
                                 @Field("matKhauCu") String matKhauCu,
                                 @Field("matKhauMoi") String matKhauMoi);


    //Giỏ hàng
    @FormUrlEncoded
    @POST("giohang")
    Call<Message> themSPVaoGioHang(@Header("Authorization") String token,
                                   @Field("idSanPham") String idSanPham,
                                   @Field("sanLuongMua") int sanLuongMua);

    @GET("giohang")
    Call<List<SPGioHang>> layGioHang(@Header("Authorization") String token);

    @FormUrlEncoded
    @PUT("giohang/{idSanPham}")
    Call<Message> capNhatSanLuongMuaSP(@Header("Authorization") String token,
                                       @Path("idSanPham") String idSanPham,
                                       @Field("sanLuongMua") int sanLuongMua);


    @DELETE("giohang/{idSanPham}")
    Call<Message> xoaSPGioHang(@Header("Authorization") String token,
                               @Path("idSanPham") String idSanPham
    );

    //Đơn hàng
    @POST("donhangs")
    Call<Message> datHang(@Header("Authorization") String token,
                          @Body DonHang donHang);

    @GET("donhangs")
    Call<DSDonHang> layDSDonHang(@Header("Authorization") String token,
                                 @Query("daDuyet") Boolean daDuyet, @Query("page") int page);

    @GET("donhangs/{idDonHang}")
    Call<DonHangRes> layChiTietDonHang(@Header("Authorization") String token,
                                       @Path("idDonHang") String idDonHang);

    @GET("sanphams/{idSanPham}/item")
    Call<ItemSPDonHang> layItemSPDonHang(@Header("Authorization") String token,
                                         @Path("idSanPham") String idSanPham);


}
