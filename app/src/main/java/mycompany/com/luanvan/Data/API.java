package mycompany.com.luanvan.Data;

import java.util.List;

import mycompany.com.luanvan.Model.ChiTietMonAn;
import mycompany.com.luanvan.Model.DataMonAn;
import mycompany.com.luanvan.Model.DonHang;
import mycompany.com.luanvan.Model.GoiMon;
import mycompany.com.luanvan.Model.GoiMonDetail;
import mycompany.com.luanvan.Model.Message;
import mycompany.com.luanvan.Model.SPGioHang;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {

    //Sản phẩm
    @GET("MonAns")
    Call<DataMonAn> getDSMonAn(@Query("page") int page);

    @GET("MonAns")
    Call<DataMonAn> timKiem(@Query("keyWord") String keyWord, @Query("page") int page);

    @GET("MonAns/{idMonAn}")
    Call<ChiTietMonAn> getChiTietSanPham(@Path("idMonAn") String idMonAn);


    //Bàn ăn giỏ hàng
    @GET("BanAns/{sttBanAn}")
    Call<List<SPGioHang>> layThongTinBanAn(@Path("sttBanAn") int sttBanAn);

    @FormUrlEncoded
    @POST("BanAns/{sttBanAn}")
    Call<Message> themMonAn(@Path("sttBanAn") int sttBanAn,
                            @Field("idMonAn") String idMonAn,
                            @Field("soLuongMua") int soLuongMua);

    @FormUrlEncoded
    @PUT("BanAns/{sttBanAn}/{idMonAn}")
    Call<Message> capNhatSoLuongMA(@Path("sttBanAn") int sttBanAn,
                                   @Path("idMonAn") String idMonAn,
                                   @Field("soLuongMua") int soLuongMua);


    @DELETE("BanAns/{sttBanAn}/{idMonAn}")
    Call<Message> xoaMonAn(@Path("sttBanAn") int sttBanAn,
                           @Path("idMonAn") String idMonAn
    );

    //Gọi món
    @POST("GoiMons/{sttBanAn}")
    Call<Message> datHang(@Path("sttBanAn") int sttBanAn,
                          @Body DonHang donHang);

    @GET("GoiMons/{sttBanAn}")
    Call<List<GoiMon>> layDSGoiMon(@Path("sttBanAn") int sttBanAn);

    @GET("GoiMons/{sttBanAn}/{idGoiMon}")
    Call<GoiMonDetail> layChiTietGoiMon(@Path("idGoiMon") String idGoiMon,
                                        @Path("sttBanAn") int sttBanAn);

//    @GET("sanphams/{idSanPham}/item")
//    Call<ItemSPDonHang> layItemSPGoiMons(@Field("sttBanAn") int sttBanAn,
//                                         @Path("idSanPham") String idSanPham);


}
