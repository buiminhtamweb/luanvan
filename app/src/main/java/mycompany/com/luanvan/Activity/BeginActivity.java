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

import mycompany.com.luanvan.Constant;
import mycompany.com.luanvan.MainActivity;
import mycompany.com.luanvan.R;
import mycompany.com.luanvan.utils.SharedPreferencesHandler;

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
            startActivity(new Intent(this, MainActivity.class));
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

    public void begin(View view) {

        if(checkNull()){
            SharedPreferencesHandler.writeInt(mContext, Constant.SO_BAN, Integer.parseInt(mEdtSoBan.getText().toString()));
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
