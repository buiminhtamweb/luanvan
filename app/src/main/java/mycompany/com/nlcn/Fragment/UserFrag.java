package mycompany.com.nlcn.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import mycompany.com.nlcn.Activity.CaiDatTaiKhoanActivity;
import mycompany.com.nlcn.Activity.DonHangDangXyLyActivity;
import mycompany.com.nlcn.Activity.LichSuDatHangActivity;
import mycompany.com.nlcn.Activity.LoginActivity;
import mycompany.com.nlcn.Constant;
import mycompany.com.nlcn.Data.ConnectServer;
import mycompany.com.nlcn.Model.UserAcc;
import mycompany.com.nlcn.R;
import mycompany.com.nlcn.utils.SharedPreferencesHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFrag extends Fragment {
    private String mUsername = "";

    private String mToken;

    private CircleImageView mCircleImageView;
    private TextView mTvHoTen, mTvSDT, mTvDiaChi;
    private TextView mTvDonHangDangXuLy, mTvLichSuDatHang, mTvCaiDatTaiKhoan, mTvDangXuat, mTvPhanHoi, mTvThongTin;
    private AlertDialog mAlertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_user, container, false);

        mToken = SharedPreferencesHandler.getString(getActivity(), Constant.TOKEN);
        mUsername = SharedPreferencesHandler.getString(getActivity(), Constant.USER_NAME);

        mCircleImageView = (CircleImageView) view.findViewById(R.id.circleImageView);
        mTvHoTen = (TextView) view.findViewById(R.id.tv_ho_ten);
        mTvSDT = (TextView) view.findViewById(R.id.tv_sdt);
        mTvDiaChi = (TextView) view.findViewById(R.id.tv_dia_chi);

        mTvDonHangDangXuLy = (TextView) view.findViewById(R.id.tv_don_hang_dag_xu_ly);
        mTvLichSuDatHang = (TextView) view.findViewById(R.id.tv_ls_dathang);
        mTvCaiDatTaiKhoan = (TextView) view.findViewById(R.id.tv_caidat);
        mTvDangXuat = (TextView) view.findViewById(R.id.tv_dangxuat);
        mTvThongTin = (TextView) view.findViewById(R.id.tv_about);

        mTvPhanHoi = (TextView) view.findViewById(R.id.tv_phanhoi);

        setClickForView();
        layThongTinCaNhan();

        return view;
    }

    private void setClickForView() {
        mTvDonHangDangXuLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DonHangDangXyLyActivity.class);
                startActivity(intent);
            }
        });
        mTvLichSuDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LichSuDatHangActivity.class);
                startActivity(intent);
            }
        });

        mTvCaiDatTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CaiDatTaiKhoanActivity.class);
                startActivity(intent);
            }
        });

        mTvDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangXuat();
            }
        });

        mTvPhanHoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phanHoi();
            }
        });

        mTvThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thongTin();
            }
        });
    }

    private void phanHoi() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "tamb1401088@student.ctu.edu.vn"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Phản hồi ứng dụng Niên luận chuyên ngành");
            intent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(intent);
        } catch (Exception e) {
            viewError("Máy của bạn chưa cài đặt ứng dụng gửi Email !");
            e.printStackTrace();
        }
    }

    public void layThongTinCaNhan() {
        ConnectServer.getInstance(getActivity()).getApi().layThongTinNguoiDung(mToken, mUsername).enqueue(new Callback<UserAcc>() {
            @Override
            public void onResponse(Call<UserAcc> call, @NonNull Response<UserAcc> response) {

                if (response.code() == 401) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.putExtra("message", "Phiên làm việc hết hạn \nVui lòng đăng nhập lại");
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).finish();
                }

                if (response.isSuccessful() && response.code() == 200) {
                    UserAcc userAcc = response.body();
                    assert userAcc != null;
                    Picasso.get().load(Constant.URL_SERVER + userAcc.getAvatar())
                            .error(R.drawable.logoapp)
                            .centerCrop()
                            .fit()
                            .into(mCircleImageView);
                    mTvHoTen.setText(userAcc.getName());
                    mTvSDT.setText(userAcc.getSdt());
                    mTvDiaChi.setText(userAcc.getDiachi());
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
            public void onFailure(Call<UserAcc> call, Throwable t) {
                viewErrorExitApp();
            }
        });
    }

    private void dangXuat() {

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn đăng xuất tài khoản ra khỏi chương trình")
                .setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferencesHandler.writeString(getContext(), Constant.TOKEN, "");
                        Snackbar.make(mCircleImageView, "Đã đăng xuất", Snackbar.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                })
                .setPositiveButton("Hủy", null);

        mAlertDialog = alertBuilder.create();
        mAlertDialog.show();

    }

    private void thongTin() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();

        //Tham chieu layout
        View dialogView = inflater.inflate(R.layout.dialog_info_app, null);
        dialogBuilder.setView(dialogView);
        Button btnHuy = (Button) dialogView.findViewById(R.id.btn_close);

        //Show Dialog
        mAlertDialog = dialogBuilder.create();
        mAlertDialog.show();
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog.dismiss();
            }
        });


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

    @Override
    public void onStop() {
        super.onStop();
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.cancel();
        }
    }
}
