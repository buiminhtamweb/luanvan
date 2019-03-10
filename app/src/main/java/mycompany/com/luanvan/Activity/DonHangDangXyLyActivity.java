package mycompany.com.luanvan.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mycompany.com.luanvan.Adapter.DonHangRecyclerViewAdapter;
import mycompany.com.luanvan.Adapter.SPDonHangRecyclerViewAdapter;
import mycompany.com.luanvan.Constant;
import mycompany.com.luanvan.Data.ConnectServer;
import mycompany.com.luanvan.Model.DSDonHang;
import mycompany.com.luanvan.Model.DonHangRes;
import mycompany.com.luanvan.Model.ItemDonhang;
import mycompany.com.luanvan.Model.ItemMonAn;
import mycompany.com.luanvan.Model.ItemSPDonHang;
import mycompany.com.luanvan.Model.SpMua;
import mycompany.com.luanvan.R;
import mycompany.com.luanvan.utils.SharedPreferencesHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonHangDangXyLyActivity extends AppCompatActivity implements DonHangRecyclerViewAdapter.onClickListener,
        DonHangRecyclerViewAdapter.onScrollListener {


    private RecyclerView mRecyclerView;
    private List<ItemDonhang> mDonHangs = new ArrayList<>();
    private DonHangRecyclerViewAdapter mDonHangRecyclerViewAdapter;

    private String mIDNguoiDung;
    private int mPageCurrent;
    private int mNumPage;
    private String mToken;
    private AlertDialog mDialogAgriList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_hang_dang_xu_ly);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mDonHangRecyclerViewAdapter = new DonHangRecyclerViewAdapter(mDonHangs);
        mDonHangRecyclerViewAdapter.setOnClickListener(this);
        mDonHangRecyclerViewAdapter.setOnScrollListener(this);
        mRecyclerView.setAdapter(mDonHangRecyclerViewAdapter);

        mToken = SharedPreferencesHandler.getString(getBaseContext(), Constant.TOKEN);
        layDonHang(1);

    }


    private void layDonHang(int page) {
        mIDNguoiDung = SharedPreferencesHandler.getString(this, "id");
        ConnectServer.getInstance(this).getApi().layDSDonHang(mToken, false, page).enqueue(new Callback<DSDonHang>() {
            @Override
            public void onResponse(Call<DSDonHang> call, @NonNull Response<DSDonHang> response) {

                if (response.code() == 401) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    intent.putExtra("message", "Phiên làm việc hết hạn \n Vui lòng đăng nhập lại");
                    startActivity(intent);
                    finish();
                }

                if (response.code() == 400) {
                    try {
                        viewSucc(mRecyclerView, response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (response.isSuccessful() && response.code() == 200) {
                    DSDonHang mDSDonHang = response.body();

                    for (ItemDonhang item : response.body().getDonhangs()
                            ) {

                        Log.e("LS_DH", "onResponse: " + item.getId());
//                        Log.e("LS_DH", "onResponse: " + item.getTongTien());
                        mDonHangs.add(item);

                    }

                    assert mDSDonHang != null;
                    mPageCurrent = Integer.parseInt(mDSDonHang.getPage());
                    mNumPage = mDSDonHang.getNumpages();
                    Log.e("DH_XULY", "onResponse: " + mPageCurrent + "___" + mNumPage);
                    mDonHangRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<DSDonHang> call, Throwable t) {

            }
        });
    }

    private void loadAgriOfOderList(String idDonHang) {

        //Khởi tạo Dialog chứa Danh sách

        final List<ItemMonAn> itemMonAns = new ArrayList<>();

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = this.getLayoutInflater();

        //Tham chieu layout
        final View dialogView = inflater.inflate(R.layout.dialog_bill_order_recy, null);
        dialogBuilder.setView(dialogView);
        RecyclerView recyAgriOrder = (RecyclerView) dialogView.findViewById(R.id.recycler_view_dialog);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyAgriOrder.setLayoutManager(layoutManager);
        final SPDonHangRecyclerViewAdapter recyAgriOrderAdapter = new SPDonHangRecyclerViewAdapter(itemMonAns);
        recyAgriOrder.setAdapter(recyAgriOrderAdapter);

        //Sự kiện Click vào Item SN
        final Intent intent = new Intent(this, ChiTietSPActivity.class);
        recyAgriOrderAdapter.setOnClickListener(new SPDonHangRecyclerViewAdapter.onClickListener() {
            @Override
            public void onItemClick(int position, String idSanPham) {
                intent.putExtra("idSP", idSanPham);
                startActivity(intent);
                finish();
            }
        });

        Button btnHuy = (Button) dialogView.findViewById(R.id.btn_close);
        dialogBuilder.setTitle("Đơn hàng đang xử lý");

        final TextView tvIDDonHang = (TextView) dialogView.findViewById(R.id.textView_id_don_hang);
//        TextView tvSoLuongSPMua = (TextView) dialogView.findViewById(R.id.textView_so_luong_sp_mua);
        final TextView tvTongTien = (TextView) dialogView.findViewById(R.id.textView_tong_gia_don_hang);
        final TextView tvNgayMua = (TextView) dialogView.findViewById(R.id.textView_ngay_mua);
        //Lấy thông tin giỏ hàng
        ConnectServer.getInstance(getBaseContext()).getApi().layChiTietDonHang(mToken, idDonHang).enqueue(new Callback<DonHangRes>() {
            @Override
            public void onResponse(Call<DonHangRes> call, Response<DonHangRes> response) {
                if (response.isSuccessful() && response.code() == 200) {

                    tvIDDonHang.setText("Mã đơn hàng: " + response.body().getId());
                    tvNgayMua.setText("Ngày đặt hàng: " + response.body().getNgayDatHang());
                    tvTongTien.setText("Tổng tiền đơn hàng: " + response.body().getTongTien() + " VND");
                    for (final SpMua spMua : response.body().getSpMua()) {

                        ConnectServer.getInstance(getBaseContext()).getApi().layItemSPDonHang(mToken, spMua.getIdSpMua()).enqueue(new Callback<ItemSPDonHang>() {
                            @Override
                            public void onResponse(Call<ItemSPDonHang> call, Response<ItemSPDonHang> response) {
                                if (response.code() == 400) {
                                    try {
                                        viewSucc(mRecyclerView, response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (response.code() == 200 && null != response.body()) {
                                    ItemMonAn itemMonAn = new ItemMonAn();
                                    itemMonAn.setId(spMua.getIdSpMua());
//                                    itemMonAn.setSanluong(spMua.getSanLuongMua());
                                    itemMonAn.setGiasp(spMua.getGiaMua());
                                    itemMonAn.setTensp(response.body().getTensp());
                                    itemMonAn.setImgurl(response.body().getImgurl());
                                    itemMonAns.add(itemMonAn);
                                    recyAgriOrderAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<ItemSPDonHang> call, Throwable t) {

                            }
                        });
                    }
                }

                if (response.code() == 400) {
                    try {
                        viewError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DonHangRes> call, Throwable t) {
                viewErrorExitApp();
            }
        });

        //Show Dialog
        mDialogAgriList = dialogBuilder.create();
        mDialogAgriList.show();
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogAgriList.dismiss();
            }
        });
    }


    @Override
    public void onItemClick(int position, String idDonHang) {
        loadAgriOfOderList(idDonHang);
    }

    @Override
    public void onScroll(int position) {
        if (mPageCurrent < mNumPage && (position + 3) == mDonHangs.size()) {
            layDonHang(mPageCurrent + 1);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDialogAgriList != null && mDialogAgriList.isShowing()) {
            mDialogAgriList.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mDialogAgriList != null && mDialogAgriList.isShowing()) {
            mDialogAgriList.cancel();
        } else {
            finish();
        }

    }

    private void viewSucc(View view, String message) {
        Snackbar mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        mSnackbar.show();
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


}
