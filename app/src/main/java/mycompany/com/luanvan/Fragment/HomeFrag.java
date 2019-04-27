package mycompany.com.luanvan.Fragment;

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

import mycompany.com.luanvan.Activity.BeginActivity;
import mycompany.com.luanvan.Activity.ChiTietSPActivity;
import mycompany.com.luanvan.Activity.TimKiemActivity;
import mycompany.com.luanvan.Adapter.MonAnRecyclerViewAdapter;
import mycompany.com.luanvan.Constant;
import mycompany.com.luanvan.Data.API;
import mycompany.com.luanvan.Data.ConnectServer;
import mycompany.com.luanvan.MainActivity;
import mycompany.com.luanvan.Model.DataMonAn;
import mycompany.com.luanvan.Model.Message;
import mycompany.com.luanvan.Model.MonAn;
import mycompany.com.luanvan.R;
import mycompany.com.luanvan.utils.SharedPreferencesHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFrag extends Fragment implements MonAnRecyclerViewAdapter.onScrollListener, MonAnRecyclerViewAdapter.onClickListener {
    private RecyclerView mRecyclerView;
    private MonAnRecyclerViewAdapter mMonAnRecyclerViewAdapter;
    private List<MonAn> mSanPhams = new ArrayList<>();
    private int mPageCurrent;
    private int mNumPage;
    private API mApi;
    private AlertDialog mAlertDialog;
    private int sttBanAn;
    private TextView mTvSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_home, container, false);

        initView(v);

        sttBanAn = SharedPreferencesHandler.getInt(getActivity(), Constant.SO_BAN);
        mApi = ConnectServer.getInstance(getContext()).getApi();

//        layDSSanPham(1);

        return v;
    }

    private void initView(View v) {
        mTvSearch = v.findViewById(R.id.tv_seach);
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TimKiemActivity.class));
            }
        });

        mRecyclerView = v.findViewById(R.id.recyclerview);

        mMonAnRecyclerViewAdapter = new MonAnRecyclerViewAdapter(getContext(), mSanPhams);
        mMonAnRecyclerViewAdapter.setOnScrollListener(this);
        mMonAnRecyclerViewAdapter.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mMonAnRecyclerViewAdapter);
    }

    private void layDSSanPham(int page) {

        Call<DataMonAn> call = mApi.getDSMonAn(page);

        call.enqueue(new Callback<DataMonAn>() {
            @Override
            public void onResponse(Call<DataMonAn> call, Response<DataMonAn> response) {
                if (null != response.body() && response.code() == 200) {

                    mPageCurrent = Integer.parseInt(response.body().getPage());
                    mNumPage = response.body().getNumpages();

                    for (MonAn sp : response.body().getItemMonAns()) {
                        if (sp.getId() != null) mSanPhams.add(sp);

                    }
                    mMonAnRecyclerViewAdapter.notifyDataSetChanged();
                    Log.e("HOME_FRAG", "onResponse: layDSSanPham Succ");
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
            public void onFailure(Call<DataMonAn> call, Throwable t) {
                Log.e("HOME_FRAG", "onFailure: " + t.getMessage());
                viewError("Lỗi kết nối đến máy chủ!");
            }
        });

    }

    @Override
    public void onScroll(int position) {
//        Log.e("TAG", "onScroll: "+position +"_"+  mSanPhams.size() );
        if (mPageCurrent < mNumPage && (position + 3) == mSanPhams.size()) {
            layDSSanPham(mPageCurrent + 1);
        }
    }


    @Override
    public void onItemClick(int position, String idSanPham) {
        Intent intent = new Intent(getActivity(), ChiTietSPActivity.class);
        intent.putExtra("idSP", idSanPham);
        startActivity(intent);

    }

    @Override
    public void onItemAddClick(int position, String idSanPham) {
        themVaoGioHang(idSanPham);
    }

    private void themVaoGioHang(final String mIdSP) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_them_so_luong_monan, null);
        Button btnXacNhan = dialogView.findViewById(R.id.btn_xacnhan);
        Button btnHuy = dialogView.findViewById(R.id.btn_huy);
        final EditText edtSanLuong = dialogView.findViewById(R.id.edt_sanluongmua);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAlertDialog.isShowing()) mAlertDialog.dismiss();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int sanLuongMua = Integer.parseInt(edtSanLuong.getText().toString());
                ConnectServer.getInstance(getActivity())
                        .getApi()
                        .themMonAn(sttBanAn, mIdSP, sanLuongMua)
                        .enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                mAlertDialog.dismiss();
                                try {
                                    if (response.code() == 401) {
                                        Intent intent = new Intent(getActivity(), BeginActivity.class);
                                        intent.putExtra("message", "Phiên làm việc hết hạn \n Vui lòng đăng nhập lại");
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                    if (response.code() == 200 && null != response.body()) {
                                        viewSucc(mRecyclerView, response.body().getMessage());
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
                                viewError("Lỗi kết nối đến máy chủ!");
                            }
                        });
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Thêm vào giỏ hàng");
        mAlertDialog = dialogBuilder.create();
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
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        snackbar.setAction("Đi đến giỏ hàng", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("position", 1);
                startActivity(intent);
                getActivity().finish();
            }
        });
        snackbar.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mSanPhams.clear();
        layDSSanPham(1);

    }
}
