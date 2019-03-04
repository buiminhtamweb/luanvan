package mycompany.com.nlcn.Fragment;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mycompany.com.nlcn.Activity.ChiTietSPActivity;
import mycompany.com.nlcn.Activity.LoginActivity;
import mycompany.com.nlcn.Adapter.GioHangRecyclerViewAdapter;
import mycompany.com.nlcn.Constant;
import mycompany.com.nlcn.Data.API;
import mycompany.com.nlcn.Data.ConnectServer;
import mycompany.com.nlcn.Model.DonHang;
import mycompany.com.nlcn.Model.Message;
import mycompany.com.nlcn.Model.SPGioHang;
import mycompany.com.nlcn.Model.SpMua;
import mycompany.com.nlcn.R;
import mycompany.com.nlcn.utils.Number;
import mycompany.com.nlcn.utils.SharedPreferencesHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GioHangFrag extends Fragment implements GioHangRecyclerViewAdapter.onClickListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private List<SPGioHang> mGioHang = new ArrayList<>();
    private GioHangRecyclerViewAdapter mGioHangRecyclerViewAdapter;
    private TextView mTvTongTien;
    private AlertDialog mAlertDialog;
    private API api;
    private Button mBtnDatHang;
    private String mToken;
    private ProgressDialog mProgressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_giohang, container, false);
        mToken = SharedPreferencesHandler.getString(getActivity(), Constant.TOKEN);

        mBtnDatHang = (Button) view.findViewById(R.id.btn_dathang);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mTvTongTien = (TextView) view.findViewById(R.id.textView_tongtien);

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

        api.layGioHang(mToken).enqueue(new Callback<List<SPGioHang>>() {
            @Override
            public void onResponse(Call<List<SPGioHang>> call, Response<List<SPGioHang>> response) {
                mGioHang.clear();
                if (response.code() == 401) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.putExtra("message", "Phiên làm việc hết hạn \n Vui lòng đăng nhập lại");
                    startActivity(intent);
                    getActivity().finish();
                }

                if (response.code() == 400) {
                    try {
                        viewSucc(mRecyclerView, response.errorBody().string());
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
                }

            }

            @Override
            public void onFailure(Call<List<SPGioHang>> call, Throwable t) {

            }
        });
    }

    private void datHang() {

        if (mGioHang.size() > 0) {
            viewProgressDialog("Đang đặt hàng ... ");
            List<SpMua> spMuas = new ArrayList<>();
            for (SPGioHang sp : mGioHang) { //Thêm sản phẩm vào giỏ hàng
                SpMua spMua = new SpMua();
                spMua.setIdSpMua(sp.getIdSpMua());
                spMua.setSanLuongMua(sp.getSanLuongMua());
                spMua.setGiaMua(sp.getGiasp());
                spMuas.add(spMua);
            }
            DonHang donHang = new DonHang();
//            donHang.setIdNguoiMua(idNguoiDung);
            donHang.setSpMua(spMuas);
            donHang.setTongTien(mGioHangRecyclerViewAdapter.getTongTien());

            ConnectServer.getInstance(getActivity()).getApi().datHang(mToken, donHang).enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {

                    hideProgressDialog();

                    if (response.isSuccessful() && response.code() == 401) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.putExtra("message", "Phiên làm việc hết hạn \n Vui lòng đăng nhập lại");
                        startActivity(intent);
                        getActivity().finish();
                    }

                    if (response.code() == 200) {//Yêu cầu đặt hàng thành công

                        mGioHang.clear(); //Xóa các dữ liệu trong giỏ hàng
                        mGioHangRecyclerViewAdapter.notifyDataSetChanged();
                        mTvTongTien.setText("" + Number.convertNumber(mGioHangRecyclerViewAdapter.getTongTien()));
                        viewSucc(mTvTongTien, response.body().getMessage());


                    }
                    if (response.isSuccessful() && response.code() == 300) {
                        for (SPGioHang spGH : mGioHang) {
                            if (spGH.getIdSpMua().equals(response.body().getMessage())) {
                                viewError("Sản lượng " + spGH.getTensp() + " không đủ trong kho không đủ để cung ứng \n" +
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
                    viewErrorExitApp();
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

                api.xoaSPGioHang(mToken, idSanPham).enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {

                        if (response.code() == 401) {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
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
                        viewErrorExitApp();
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
        View dialogView = inflater.inflate(R.layout.dialog_them_san_luong_sp, null);
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

                final int sanLuongMua = Integer.parseInt(edtSanLuong.getText().toString());
                api.capNhatSanLuongMuaSP(mToken, idSanPham, sanLuongMua).enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if (null != response.body() && response.code() == 200) {


                                viewSucc(mTvTongTien, "Đã cập nhật thành công");
                                mGioHang.get(position).setSanLuongMua(sanLuongMua);
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
                        viewErrorExitApp();
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
        if (view.getId() == R.id.btn_dathang) {
            datHang();
        }
    }

    private void viewErrorExitApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

}
