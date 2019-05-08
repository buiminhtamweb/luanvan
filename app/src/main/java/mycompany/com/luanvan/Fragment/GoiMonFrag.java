package mycompany.com.luanvan.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mycompany.com.luanvan.Activity.BeginActivity;
import mycompany.com.luanvan.Activity.ChiTietSPActivity;
import mycompany.com.luanvan.Adapter.GioHangRecyclerViewAdapter;
import mycompany.com.luanvan.Constant;
import mycompany.com.luanvan.Data.API;
import mycompany.com.luanvan.Data.ConnectServer;
import mycompany.com.luanvan.Model.DonHang;
import mycompany.com.luanvan.Model.Message;
import mycompany.com.luanvan.Model.SPGioHang;
import mycompany.com.luanvan.Model.SpMua;
import mycompany.com.luanvan.R;
import mycompany.com.luanvan.utils.Number;
import mycompany.com.luanvan.utils.SharedPreferencesHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoiMonFrag extends Fragment implements GioHangRecyclerViewAdapter.onClickListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private List<SPGioHang> mGioHang = new ArrayList<>();
    private GioHangRecyclerViewAdapter mGioHangRecyclerViewAdapter;
    private TextView mTvTongTien, mTvAlert;
    private AlertDialog mAlertDialog;
    private API api;
    private Button mBtnDatHang;
    private int sttBanAn;
    private ProgressDialog mProgressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_giohang, container, false);
        sttBanAn = SharedPreferencesHandler.getInt(getActivity(), Constant.SO_BAN);

        mBtnDatHang = (Button) view.findViewById(R.id.btn_goimon);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mTvTongTien = (TextView) view.findViewById(R.id.textView_tongtien);
        mTvAlert = (TextView) view.findViewById(R.id.tv_alert);

        mGioHangRecyclerViewAdapter = new GioHangRecyclerViewAdapter(getContext(), mGioHang);
        mGioHangRecyclerViewAdapter.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mGioHangRecyclerViewAdapter);

        mBtnDatHang.setOnClickListener(this);


        return view;
    }

    public void layDuLieuGioHang() {

        api = ConnectServer.getInstance(getActivity()).getApi();

        api.layThongTinBanAn(sttBanAn).enqueue(new Callback<List<SPGioHang>>() {
            @Override
            public void onResponse(Call<List<SPGioHang>> call, Response<List<SPGioHang>> response) {
                mGioHang.clear();

                if (response.code() == 400) {
                    try {
                        viewNullGioHang(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (response.code() == 200 && null != response.body()) {
                    for (int i = 0; i < response.body().size(); i++) {

                        mGioHang.add(response.body().get(i));

                    }

                    mGioHangRecyclerViewAdapter.notifyDataSetChanged();
                    mTvTongTien.setText(Number.convertNumber(mGioHangRecyclerViewAdapter.getTongTien()) + "");
                    mTvAlert.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<List<SPGioHang>> call, Throwable t) {
                showErrDisconnect(mBtnDatHang);
            }
        });
    }

    private void viewNullGioHang(String string) {

        mTvAlert.setText(string);
        if (!mTvAlert.isEnabled()) {
            mTvAlert.setVisibility(View.VISIBLE);
        }


    }

    private void datHang() {

        if (mGioHang.size() > 0) {
            viewProgressDialog("Đang đặt hàng ... ");
            List<SpMua> spMuas = new ArrayList<>();
            for (SPGioHang sp : mGioHang) { //Thêm sản phẩm vào giỏ hàng
                SpMua spMua = new SpMua();
                spMua.setIdMonAn(sp.getIdMonAn());
                spMua.setSoLuongMua(sp.getSoLuongMua());
                spMua.setGiaMua(sp.getGiaMA());
                spMuas.add(spMua);
            }
            DonHang donHang = new DonHang();
//            donHang.setSttBanAn(idNguoiDung);
            donHang.setMonAn(spMuas);
            donHang.setTongTien(mGioHangRecyclerViewAdapter.getTongTien());

            ConnectServer.getInstance(getActivity()).getApi().datHang(sttBanAn, donHang).enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {

                    hideProgressDialog();

                    if (response.code() == 200) {//Yêu cầu đặt hàng thành công

                        mGioHang.clear(); //Xóa các dữ liệu trong giỏ hàng
                        mGioHangRecyclerViewAdapter.notifyDataSetChanged();
                        mTvTongTien.setText("" + Number.convertNumber(mGioHangRecyclerViewAdapter.getTongTien()));
                        viewSucc(mTvTongTien, response.body().getMessage());


                    }
                    if (response.isSuccessful() && response.code() == 300) {
                        for (SPGioHang spGH : mGioHang) {
                            if (spGH.getIdMonAn().equals(response.body().getMessage())) {
                                viewError("Sản lượng " + spGH.getTenMA() + " không đủ trong kho không đủ để cung ứng \n" +
                                        " Quí khách vui lòng cập nhật lại sản lượng mua");
                            }
                        }
                        viewError(response.body().getMessage());
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
                public void onFailure(Call<Message> call, Throwable t) {
                    showErrDisconnect(mBtnDatHang);
                }
            });

        } else {
            viewError("Giỏ hàng rỗng !");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        layDuLieuGioHang();
    }

    @Override
    public void onItemClick(int position, String idSanPham) {
        Intent intent = new Intent(getActivity(), ChiTietSPActivity.class);
        intent.putExtra("idSP", idSanPham);
        startActivity(intent);
    }

    @Override
    public void onItemDeleteClick(final int position, final String idSanPham) {

        AlertDialog.Builder builderDialog = new AlertDialog.Builder(getActivity());
        builderDialog.setTitle("Cảnh báo");
        builderDialog.setMessage("Bạn có chắc chắn muốn xóa?");
        builderDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {

                api.xoaMonAn(sttBanAn, idSanPham).enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {

                        if (response.code() == 401) {
                            Intent intent = new Intent(getActivity(), BeginActivity.class);
                            intent.putExtra("message", "Phiên làm việc hết hạn \nVui lòng đăng nhập lại");
                            startActivity(intent);
                            getActivity().finish();
                        }

                        if (response.isSuccessful() && response.code() == 200) {
                            viewSucc(mTvTongTien, response.body().getMessage());
                            mGioHang.remove(position);
                            mGioHangRecyclerViewAdapter.notifyDataSetChanged();
                            mTvTongTien.setText("" + Number.convertNumber(mGioHangRecyclerViewAdapter.getTongTien()));
                            mAlertDialog.dismiss();
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
                    public void onFailure(Call<Message> call, Throwable t) {
                        showErrDisconnect(mBtnDatHang);
                    }
                });

            }
        });
        builderDialog.setPositiveButton("Hủy", null);
        mAlertDialog = builderDialog.create();
        mAlertDialog.show();

    }

    @Override
    public void onEditTextClick(final int position, final String idSanPham, String tenSanPham, int sanLuongMua) {
        //Ver2
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_them_so_luong_monan, null);
        Button btnXacNhan = dialogView.findViewById(R.id.btn_xacnhan);
        Button btnHuy = dialogView.findViewById(R.id.btn_huy);
        final EditText edtSanLuong = dialogView.findViewById(R.id.edt_sanluongmua);

        edtSanLuong.setText(sanLuongMua + "");

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAlertDialog.isShowing()) mAlertDialog.dismiss();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int soLuongMua = Integer.parseInt(edtSanLuong.getText().toString());
                api.capNhatSoLuongMA(sttBanAn, idSanPham, soLuongMua).enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if (null != response.body() && response.code() == 200) {


                            viewSucc(mTvTongTien, "Đã cập nhật thành công");
                            mGioHang.get(position).setSoLuongMua(soLuongMua);
                            mGioHangRecyclerViewAdapter.notifyDataSetChanged();
                            mTvTongTien.setText("" + Number.convertNumber(mGioHangRecyclerViewAdapter.getTongTien()));
                            mAlertDialog.dismiss();

                        }
                        if (response.code() == 400 && null != response.errorBody()) {
                            try {
                                viewError(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        showErrDisconnect(mBtnDatHang);
                    }
                });

            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Cập nhật " + tenSanPham);
        mAlertDialog = dialogBuilder.create();
        mAlertDialog.show();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_goimon) {
            datHang();
        }
    }

    private void viewError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Cảnh báo");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    private void viewSucc(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    private void viewProgressDialog(String message) {
        if (null == mProgressDialog) {
            mProgressDialog = new ProgressDialog(getContext());
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
        }
    }

    public void showErrDisconnect(View view) {
        // Create snackbar
        if (view.isShown()) {
            final Snackbar snackbar = Snackbar.make(view, "Mất kết nối đến máy chủ", Snackbar.LENGTH_INDEFINITE);

            // Set an action on it, and a handler
            snackbar.setAction("Thử lại", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGioHang.clear();
                    layDuLieuGioHang();
                }
            });

            snackbar.show();
        }

    }

}
