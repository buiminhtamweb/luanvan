package mycompany.com.luanvan.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mycompany.com.luanvan.Adapter.MonAnRecyclerViewAdapter;
import mycompany.com.luanvan.Constant;
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

public class TimKiemActivity extends AppCompatActivity implements MonAnRecyclerViewAdapter.onScrollListener,
        MonAnRecyclerViewAdapter.onClickListener {

    private RecyclerView mRecyclerView;
    private MonAnRecyclerViewAdapter mMonAnRecyclerViewAdapter;
    private List<MonAn> mSanPhams = new ArrayList<>();
    private int mPageCurrent;
    private int mNumPage;
    private AlertDialog mAlertDialog;

    private ImageView mImgSearch;
    private EditText mEdtKeyWord;
    private int sttBanAn;
    private String mKeyWord;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem);

        sttBanAn = SharedPreferencesHandler.getInt(this, Constant.SO_BAN);


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


        mEdtKeyWord = (EditText) findViewById(R.id.editText_search);
        mEdtKeyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mKeyWord = mEdtKeyWord.getText().toString();
                    timKiem(mKeyWord, 1);
                    return true;
                }
                return false;
            }
        });
        mImgSearch = (ImageView) findViewById(R.id.img_ic_search);
        mImgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mKeyWord = mEdtKeyWord.getText().toString();
                timKiem(mKeyWord, 1);
            }
        });

        mRecyclerView = findViewById(R.id.recyclerview);

        mMonAnRecyclerViewAdapter = new MonAnRecyclerViewAdapter(this, mSanPhams);
        mMonAnRecyclerViewAdapter.setOnScrollListener(this);
        mMonAnRecyclerViewAdapter.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mMonAnRecyclerViewAdapter);


    }




    private void timKiem(String keyWord,int page) {
        viewProgressDialog("Đang tìm kiếm...");
        mSanPhams.clear();
        ConnectServer.getInstance(this).getApi().timKiem(keyWord, page).enqueue(new Callback<DataMonAn>() {
            @Override
            public void onResponse(Call<DataMonAn> call, Response<DataMonAn> response) {
                hideProgressDialog();
                if (null != response && response.code() == 200) {
                    mPageCurrent = Integer.parseInt(response.body().getPage());
                    mNumPage = response.body().getNumpages();

                    for (MonAn sp : response.body().getItemMonAns()) {
                        if (sp.getId() != null) mSanPhams.add(sp);

                    }
                    mMonAnRecyclerViewAdapter.notifyDataSetChanged();
                    Log.e("SEARCH", "onResponse: Succ");
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
                Log.e("SEARCH", "onFailure: " + t.getMessage());
                viewErrorExitApp();
            }
        });

    }

    @Override
    public void onScroll(int position) {
//        Log.e("TAG", "onScroll: "+position +"_"+  mSanPhams.size() );
        if (mPageCurrent < mNumPage && (position + 3) == mSanPhams.size()) {
            timKiem(mKeyWord,mPageCurrent + 1);
        }
    }


    @Override
    public void onItemClick(int position, String idSanPham) {
        Intent intent = new Intent(this, ChiTietSPActivity.class);
        intent.putExtra("idSP", idSanPham);
        startActivity(intent);

    }

    @Override
    public void onItemAddClick(int position, String idSanPham) {
        themVaoGioHang(idSanPham);
    }

    private void themVaoGioHang(final String mIdSP) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
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
                ConnectServer.getInstance(TimKiemActivity.this)
                        .getApi()
                        .themMonAn(sttBanAn, mIdSP, sanLuongMua)
                        .enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                mAlertDialog.dismiss();
                                try {
                                    if (response.code() == 401) {
                                        Intent intent = new Intent(TimKiemActivity.this, BeginActivity.class);
                                        intent.putExtra("message", "Phiên làm việc hết hạn \n Vui lòng đăng nhập lại");
                                        startActivity(intent);
                                        finish();
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
                Intent intent = new Intent(TimKiemActivity.this, MainActivity.class);
                intent.putExtra("position", 1);
                startActivity(intent);
                finish();
            }
        });
        snackbar.show();
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

    private void viewProgressDialog(String message) {
        if (null == mProgressDialog) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
        }
    }

}
