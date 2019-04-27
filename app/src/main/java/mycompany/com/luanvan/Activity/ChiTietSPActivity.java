package mycompany.com.luanvan.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import mycompany.com.luanvan.Constant;
import mycompany.com.luanvan.Data.ConnectServer;
import mycompany.com.luanvan.MainActivity;
import mycompany.com.luanvan.Model.ChiTietMonAn;
import mycompany.com.luanvan.Model.Message;
import mycompany.com.luanvan.R;
import mycompany.com.luanvan.utils.SharedPreferencesHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietSPActivity extends AppCompatActivity {

    String idNguoiDung = "";
    private String mIdSP;
    private ImageView mImageView;
    private TextView mTenSanPham, mGiaSanPham, mSanLuongSP, mChiTietSP;
    private AlertDialog alertDialog = null;
    private AlertDialog mAlertDialog;
    private int sttBanAn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_monan);

        Intent intent = getIntent();
        if (null != intent) {
            mIdSP = intent.getExtras().getString("idSP", "");
        } else finish();
        sttBanAn = SharedPreferencesHandler.getInt(getBaseContext(), Constant.SO_BAN);
        idNguoiDung = SharedPreferencesHandler.getString(this, "id");

        mImageView = (ImageView) findViewById(R.id.img_agri);
        mTenSanPham = (TextView) findViewById(R.id.tv_ten_ns);
        mGiaSanPham = (TextView) findViewById(R.id.tv_gia);
        mSanLuongSP = (TextView) findViewById(R.id.tv_sl_conlai);
        mChiTietSP = (TextView) findViewById(R.id.tv_nd_chitiet_ns);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            setSupportActionBar(toolbar);
            toolbar.setTitle("Chi tiết sản phẩm");
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        ConnectServer.getInstance(getApplicationContext()).getApi().getChiTietSanPham(mIdSP).enqueue(new Callback<ChiTietMonAn>() {
            @Override
            public void onResponse(Call<ChiTietMonAn> call, Response<ChiTietMonAn> response) {

                if (response.code() == 200 && null != response.body()) {
                    ChiTietMonAn chiTietMonAn = response.body();
                    Picasso.get().load(Constant.URL_IMG + chiTietMonAn.getImgurl()).fit().centerCrop().into(mImageView);
                    mTenSanPham.setText(chiTietMonAn.getTenMA());
                    mGiaSanPham.setText("Giá: " + chiTietMonAn.getGiaMA().toString());
//                    mSanLuongSP.setText("Tồn kho: " + chiTietMonAn.getSanluong().toString());
                    mChiTietSP.setText(chiTietMonAn.getChitietsp().toString());
                }
                if (response.code() == 400 && response.errorBody() != null) {
                    try {
                        viewError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ChiTietMonAn> call, Throwable t) {
                viewErrorExitApp();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void viewErrorExitApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cảnh báo");
        builder.setMessage("Không thể kết nối đến máy chủ ! \nThoát ứng dụng.");
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                System.exit(1);
            }
        });
        AlertDialog mAlertDialog = builder.create();
        mAlertDialog.show();
    }


    //Sự kiện nút Thêm vào giỏ hàng
    public void themVaoGioHang(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_them_so_luong_monan, null);
        Button btnXacNhan = dialogView.findViewById(R.id.btn_xacnhan);
        Button btnHuy = dialogView.findViewById(R.id.btn_huy);
        final EditText edtSanLuong = dialogView.findViewById(R.id.edt_sanluongmua);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alertDialog.isShowing()) alertDialog.dismiss();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int sanLuongMua = Integer.parseInt(edtSanLuong.getText().toString());

                ConnectServer.getInstance(getApplicationContext()).getApi().themMonAn(sttBanAn, mIdSP, sanLuongMua)
                        .enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                mAlertDialog.dismiss();
                                try {
                                    if (response.code() == 401) {
                                        Intent intent = new Intent(getBaseContext(), BeginActivity.class);
                                        intent.putExtra("message", "Phiên làm việc hết hạn \n Vui lòng đăng nhập lại");
                                        startActivity(intent);
                                        finish();
                                    }
                                    if (response.code() == 200) {
                                        viewSucc(mImageView, "Đã đặt hàng thành công !");
                                    }
                                    if (response.code() == 400 && null != response.errorBody()) {
                                        String err = "";
                                        err += response.errorBody().string();
                                        viewError(err);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {
                                mAlertDialog.dismiss();
                                viewError(getString(R.string.err_connect_to_server));
                            }
                        });
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Đặt hàng");
        mAlertDialog = dialogBuilder.create();
        mAlertDialog.show();
    }


    private void viewError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cảnh báo");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void viewSucc(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        snackbar.setAction("Đi đến giỏ hàng", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("position", 1);
                startActivity(intent);
                finish();
            }
        });
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
