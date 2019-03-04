package mycompany.com.nlcn.Activity;

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
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.IOException;

import mycompany.com.nlcn.Constant;
import mycompany.com.nlcn.Data.ConnectServer;
import mycompany.com.nlcn.MainActivity;
import mycompany.com.nlcn.Model.ResLogin;
import mycompany.com.nlcn.R;
import mycompany.com.nlcn.utils.SharedPreferencesHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText mEdtUserName;
    private EditText mEdtPassword;
    private Context mContext;
    private CheckBox mCBRememberMe;
    private AlertDialog mAlertDialog;
    private Snackbar mSnackbar;
    private ProgressDialog mProgressDialog;

    private String mToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEdtUserName = (EditText) findViewById(R.id.user_search);
        mEdtPassword = (EditText) findViewById(R.id.pswd);
        mCBRememberMe = (CheckBox) findViewById(R.id.cb_remember);
        mContext = this.getApplicationContext();

        mToken = SharedPreferencesHandler.getString(mContext, Constant.TOKEN);

        mEdtUserName.setText(SharedPreferencesHandler.getString(mContext, Constant.USER_NAME));
        mEdtPassword.setText(SharedPreferencesHandler.getString(mContext, Constant.PASSWORD));


    }

    public void login(final View v) {
        viewProgressDialog("Đang đăng nhập ... ");
        if (checkNullDangNhap()) {

            Call<ResLogin> call = ConnectServer.getInstance(mContext).getApi().signInAcc(mToken,
                    mEdtUserName.getText().toString(),
                    mEdtPassword.getText().toString());

            call.enqueue(new Callback<ResLogin>() {
                @Override
                public void onResponse(Call<ResLogin> call, Response<ResLogin> response) {
                    hideProgressDialog();

                    if (response.code() == 400) {
                        try {
                            viewError(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (response.code() == 200 && response.body().getSERVERRESPONSE() == 1) {

                        if (mCBRememberMe.isChecked()) {

                            SharedPreferencesHandler.writeString(mContext, Constant.USER_NAME, mEdtUserName.getText().toString());
//                                SharedPreferencesHandler.writeString(mContext, Constant.PASSWORD, mEdtPassword.getText().toString());
                            SharedPreferencesHandler.writeBoolean(mContext, "remember_me", true);

                        }


                        Log.e("LOGIN", "onResponse: TOKEN: " + response.body().getTOKEN());


                        viewSucc(mCBRememberMe, "Đã đăng nhập thành công");
                        SharedPreferencesHandler.writeString(mContext, Constant.USER_NAME, mEdtUserName.getText().toString());
                        SharedPreferencesHandler.writeString(mContext, Constant.TOKEN, "Bearer " + response.body().getTOKEN());
                        Intent i = new Intent(mContext, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else if (response.code() == 200 && response.body().getSERVERRESPONSE() == 0) {

                        viewError(response.body().getSERVERMESSAGE());

                        v.setEnabled(true);
                    }


                }

                @Override
                public void onFailure(Call<ResLogin> call, Throwable t) {
                    viewErrorExitApp();
                }
            });
        }
    }

    public void register(View v) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private boolean checkNullDangNhap() {
        if (mEdtUserName.getText().toString().equals("")) {
            mEdtUserName.setError("Chưa nhập tên đăng nhập");
            return false;
        } else if (mEdtPassword.getText().toString().equals("")) {
            mEdtPassword.setError("Chưa nhập mật khẩu");
            return false;
        } else return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            String messsage = bundle.getString("message", null);
            if (messsage != null) {
                viewError(messsage);
            }
        } else {
            boolean remember_me = SharedPreferencesHandler.getBoolean(mContext, "remember_me");
            String token = SharedPreferencesHandler.getString(mContext, Constant.TOKEN);

            Log.e("LOGIN", "onStart: " + remember_me + token);

            if (remember_me && !token.equals("")) {
                startActivity(new Intent(this, MainActivity.class));
            }
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
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
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    private void viewSucc(View view, String message) {
        mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        mSnackbar.show();
    }

    private void viewProgressDialog(String message) {
        if (null == mProgressDialog) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
        }
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
