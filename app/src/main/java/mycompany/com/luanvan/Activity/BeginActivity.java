package mycompany.com.luanvan.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.util.List;

import mycompany.com.luanvan.Constant;
import mycompany.com.luanvan.Data.ConnectServer;
import mycompany.com.luanvan.MainActivity;
import mycompany.com.luanvan.Model.SPGioHang;
import mycompany.com.luanvan.R;
import mycompany.com.luanvan.utils.SharedPreferencesHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BeginActivity extends AppCompatActivity {

    private EditText mEdtSoBan;
    private Context mContext;
    private AlertDialog mAlertDialog;
    private Snackbar mSnackbar;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);
        mEdtSoBan = (EditText) findViewById(R.id.edt_soban);
        mContext = this.getApplicationContext();

//        mToken = SharedPreferencesHandler.getString(mContext, Constant.TOKEN);
//        mEdtSoBan.setText(SharedPreferencesHandler.getInt(mContext, Constant.SO_BAN));


    }

    private boolean checkNull() {

        if (mEdtSoBan.getText().toString().equals("")) {
            mEdtSoBan.setError("Chưa nhập số bàn");
            return false;
        } else if (Integer.parseInt(mEdtSoBan.getText().toString()) <= 0) {
            mEdtSoBan.setError("Số bàn phải lớn hơn 0");
            return false;
        } else return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        int soban = SharedPreferencesHandler.getInt(mContext, Constant.SO_BAN);
        Log.e("LOGIN", "onStart: " + soban);
        if (soban != 0) {
//            startActivity(new Intent(this, MainActivity.class));
            checkDataFromServer(soban);
        }

    }


    @Override
    protected void onStop() {
        super.onStop();

        if (null != mAlertDialog && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }

    }

    private void viewError(String message) {
        Log.e("BEGIN", "viewError: ");
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
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    private void viewSucc(View view, String message) {
        mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        mSnackbar.show();
    }


    public void begin(View view) {
        if(checkNull()){
            checkDataFromServer(Integer.parseInt(mEdtSoBan.getText().toString()));
        }
    }


    private void checkDataFromServer(final int sttBA) {
        ConnectServer.getInstance(this).getApi().layThongTinBanAn(sttBA).enqueue(new Callback<List<SPGioHang>>() {
            @Override
            public void onResponse(Call<List<SPGioHang>> call, Response<List<SPGioHang>> response) {
                if (response.code() == 404) {
//                    Log.e("LOGIN", "onResponse: " );
                    try {
                        viewError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if ((response.code() == 400 || response.code() == 200)) {//Có thông tin đơn hàng
                    SharedPreferencesHandler.writeInt(mContext, Constant.SO_BAN, sttBA);

                    startActivity(new Intent(BeginActivity.this, MainActivity.class));
                }
            }

            @Override
            public void onFailure(Call<List<SPGioHang>> call, Throwable throwable) {
                showErrDisconnect(mEdtSoBan);
            }
        });
    }

    public void showErrDisconnect(View view) {
        // Create snackbar
        if (view.isShown()) {
            final Snackbar snackbar = Snackbar.make(view, "Mất kết nối đến máy chủ", Snackbar.LENGTH_INDEFINITE);
            // Set an action on it, and a handler
            snackbar.setAction("Thử lại", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkDataFromServer(Integer.parseInt(mEdtSoBan.getText().toString()));
                }
            });

            snackbar.show();
        }

    }
}
