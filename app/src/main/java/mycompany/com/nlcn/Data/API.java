package mycompany.com.nlcn.Data;

import java.util.List;

import mycompany.com.nlcn.Model.ChiTietSanPham;
import mycompany.com.nlcn.Model.DSDonHang;
import mycompany.com.nlcn.Model.DataSanPham;
import mycompany.com.nlcn.Model.DonHang;
import mycompany.com.nlcn.Model.DonHangRes;
import mycompany.com.nlcn.Model.ItemSPDonHang;
import mycompany.com.nlcn.Model.Message;
import mycompany.com.nlcn.Model.ResLogin;
import mycompany.com.nlcn.Model.SPGioHang;
import mycompany.com.nlcn.Model.UserAcc;
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
    Call<DataSanPham> getDSSanPham(@Query("page") int page);

    @GET("sanphams")
    Call<DataSanPham> timKiem(@Query("keyWord") String keyWord, @Query("page") int page);

    @GET("sanphams/{idSanPham}")
    Call<ChiTietSanPham> getChiTietSanPham(@Path("idSanPham") String idSanPham);

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
    Call<Message> capNhatThongTinNguoiDung(@Header("Authorization") String token,
                                           @Path("username") String username,
                                           @Field("type") int type,
                                           @Field("data") String data);

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
