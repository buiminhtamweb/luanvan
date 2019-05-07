package mycompany.com.luanvan.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mycompany.com.luanvan.Adapter.GoiMonRecyclerViewAdapter;
import mycompany.com.luanvan.Adapter.MADonHangRecyclerViewAdapter;
import mycompany.com.luanvan.Constant;
import mycompany.com.luanvan.Data.ConnectServer;
import mycompany.com.luanvan.Data.ConnectSocketIO;
import mycompany.com.luanvan.Model.GoiMon;
import mycompany.com.luanvan.Model.GoiMonDetail;
import mycompany.com.luanvan.Model.ItemMonAn;
import mycompany.com.luanvan.R;
import mycompany.com.luanvan.utils.Number;
import mycompany.com.luanvan.utils.SharedPreferencesHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mycompany.com.luanvan.Constant.CALL;


public class ThanhToanFrag extends Fragment implements ThanhToanInterface {

    private static final String TABLE_CALL = "call-table-";
    private static final String TAG = "ThanhToanFrag";

    private AlertDialog mAlertDialog;
    private TextView mTvSoBan;
    private TextView mTvTongCong;

    private int mSttBanAn;
    private FloatingActionButton mFAB;
    private RecyclerView mRecyDSGoiMon;
    private RelativeLayout mReViewCall;
    private TextView mTvCall;
    private List<GoiMon> mGoiMonList = new ArrayList<>();
    private List<ItemMonAn> itemMonAns = new ArrayList<>();
    private GoiMonRecyclerViewAdapter mGoiMonRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_thanhtoan, container, false);

        mTvSoBan = view.findViewById(R.id.tv_so_ban);
        mTvTongCong = view.findViewById(R.id.tv_tong_cong);
        mSttBanAn = SharedPreferencesHandler.getInt(getActivity(), Constant.SO_BAN);
        mTvSoBan.setText("Số bàn: " + mSttBanAn);
        mFAB = (FloatingActionButton) view.findViewById(R.id.fab_thanhtoan);
        mReViewCall = (RelativeLayout) view.findViewById(R.id.view_calling);
        mTvCall = (TextView) view.findViewById(R.id.tv_calling);
        mRecyDSGoiMon = (RecyclerView) view.findViewById(R.id.danh_sach_goi_mon);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyDSGoiMon.setLayoutManager(layoutManager);

        //Gọi thanh toán
        mReViewCall.setVisibility(View.GONE);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectSocketIO.getInstance(getContext()).sendData(TABLE_CALL, CALL, true);
                mFAB.setEnabled(false);
                mReViewCall.setVisibility(View.VISIBLE);

                new CountDownTimer(5000, 1000) {
                    String textShowCalling = "Đang gọi";

                    public void onTick(long millisUntilFinished) {
                        textShowCalling += ".";
                        mTvCall.setText(textShowCalling);
                    }

                    public void onFinish() {
                        mTvCall.setText("");
                        mReViewCall.setVisibility(View.GONE);
                        mFAB.setEnabled(true);
                        viewSucc(mReViewCall, "Đã gọi thanh toán! Xin quí khách chờ trong vài giây");
                    }
                }.start();

            }
        });


        // Danh sách sản phẩm đã gọi món
        mGoiMonRecyclerViewAdapter = new GoiMonRecyclerViewAdapter(mGoiMonList);
        mRecyDSGoiMon.setAdapter(mGoiMonRecyclerViewAdapter);

        //Item Click
        mGoiMonRecyclerViewAdapter.setOnItemClickListener(new GoiMonRecyclerViewAdapter.onClickListener() {
            @Override
            public void onItemClick(int position, String idGoiMon) {
                viewDetailGoiMon(idGoiMon);
            }
        });

        getDataFromServer();

        return view;
    }


    private void viewDetailGoiMon(String idGoiMon) {

        //init Alert Dialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_goimon_detail_recy, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final TextView tvIDGoiMon = dialogView.findViewById(R.id.textView_id_goi_mon);
        final TextView tvTongGiaGM = dialogView.findViewById(R.id.textView_tong_gia_goi_mon);
        final TextView tvNgayGM = dialogView.findViewById(R.id.textView_ngay_mua);
        final TextView tvTrangThaiGM = dialogView.findViewById(R.id.textView_trang_thai);
        final RecyclerView recyclerViewMA = dialogView.findViewById(R.id.recycler_view_dialog);
        final Button btnClose = dialogView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAlertDialog.isShowing()) {
                    mAlertDialog.dismiss();
                }
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewMA.setLayoutManager(layoutManager);
        final MADonHangRecyclerViewAdapter maDonHangRecyclerViewAdapter = new MADonHangRecyclerViewAdapter(itemMonAns);
        recyclerViewMA.setAdapter(maDonHangRecyclerViewAdapter);

        builder.setView(dialogView);

        mAlertDialog = builder.create();

        //Lấy dữ liệu từ Server
        ConnectServer.getInstance(getActivity()).getApi().layChiTietGoiMon(idGoiMon, mSttBanAn).enqueue(new Callback<GoiMonDetail>() {
            @Override
            public void onResponse(Call<GoiMonDetail> call, Response<GoiMonDetail> response) {
                if (response.isSuccessful() && response.code() == 400) {

                    viewError(response.errorBody().toString());
                } else {//Yêu cầu đặt hàng thành công

                    itemMonAns.clear(); //Xóa các dữ liệu trong giỏ hàng 111

                    tvIDGoiMon.setText("ID gọi món: " + response.body().getId());
                    tvTongGiaGM.setText("Tổng cộng gọi món: " + Number.convertNumber(response.body().getTongTien()) + " VND");
                    tvNgayGM.setText("Thời gian gọi món: " + response.body().getNgayGoiMon());
                    if (response.body().getDaVanChuyen())
                        tvTrangThaiGM.setText("Trạng thái: Đang vận chuyển");
                    else tvTrangThaiGM.setText("Trạng thái: Đang xử lý");

                    for (ItemMonAn item : response.body().getMonAn()) {
                        Log.e(TAG, "onResponse: " + item.getTenMA());
                        if (item.getTenMA() != null) itemMonAns.add(item);

                    }
                    itemMonAns = response.body().getMonAn();
                    maDonHangRecyclerViewAdapter.notifyDataSetChanged();
                    mAlertDialog.show();

                }
            }

            @Override
            public void onFailure(Call<GoiMonDetail> call, Throwable t) {
                showErrDisconnect(mRecyDSGoiMon);
            }
        });
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

    private void getDataFromServer() {
        ConnectServer.getInstance(getActivity()).getApi().layDSGoiMon(mSttBanAn).enqueue(new Callback<List<GoiMon>>() {
            @Override
            public void onResponse(Call<List<GoiMon>> call, Response<List<GoiMon>> response) {
                if (response.isSuccessful() && response.code() == 400) {

                    viewError(response.errorBody().toString());
                } else if (null != response.body()) {//Yêu cầu đặt hàng thành công

                    mGoiMonList.clear(); //Xóa các dữ liệu trong giỏ hàng

//                    Log.e(TAG, "onResponse: " );
                    for (GoiMon mg : response.body()) {
                        Log.e(TAG, "onResponse: " + mg.getId());
                        if (mg.getId() != null) mGoiMonList.add(mg);
                    }
                    mGoiMonRecyclerViewAdapter.notifyDataSetChanged();
                    mTvTongCong.setText("" + Number.convertNumber(mGoiMonRecyclerViewAdapter.getTongCong()) + " VND");


                }
            }

            @Override
            public void onFailure(Call<List<GoiMon>> call, Throwable t) {
                showErrDisconnect(mRecyDSGoiMon);
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.cancel();
        }
    }

    @Override
    public void reloadDataServer() {
        getDataFromServer();
    }


    public void showErrDisconnect(View view) {
        // Create snackbar
        if (view.isShown()) {
            final Snackbar snackbar = Snackbar.make(view, "Mất kết nối đến máy chủ", Snackbar.LENGTH_INDEFINITE);

            // Set an action on it, and a handler
            snackbar.setAction("Thử lại", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGoiMonList.clear();
                    reloadDataServer();
                }
            });

            snackbar.show();
        }

    }
}
