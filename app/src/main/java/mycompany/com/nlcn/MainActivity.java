package mycompany.com.nlcn;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import mycompany.com.nlcn.Adapter.ViewPagerAdapter;
import mycompany.com.nlcn.Data.ConnectServer;
import mycompany.com.nlcn.Fragment.GioHangFrag;
import mycompany.com.nlcn.Fragment.HomeFrag;
import mycompany.com.nlcn.Fragment.UserFrag;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        ViewPager.OnPageChangeListener {

    private ViewPagerAdapter mViewPagerAdapter;
    private ViewPager mViewPager;
    private MenuItem mPrevMenuItem;
    private BottomNavigationView mBottomNavigation;
//    private Toolbar mToolbar;

    private HomeFrag mHomeFrag;
    private GioHangFrag mGioHangFrag;
    private UserFrag mUserFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
//        mToolbar = findViewById(R.id.toolbar);
//        mToolbar.setTitle("Trang chủ");

        mHomeFrag = new HomeFrag();
        mGioHangFrag = new GioHangFrag();
        mUserFrag = new UserFrag();

        mBottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mBottomNavigation.setOnNavigationItemSelectedListener(this);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),mHomeFrag, mGioHangFrag, mUserFrag );
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(1);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                mViewPager.setCurrentItem(0, true);
                return true;
            case R.id.navigation_dashboard:
                mViewPager.setCurrentItem(1, true);
                mGioHangFrag.layDuLieuGioHang(); //Load lại giỏ hàng
                return true;
            case R.id.navigation_notifications:
                mViewPager.setCurrentItem(2, true);
                mUserFrag.layThongTinCaNhan(); //Load lại thông tin cá nhân
                return true;
        }
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mPrevMenuItem != null) {
            mPrevMenuItem.setChecked(false);
        } else {
            mBottomNavigation.getMenu().getItem(0).setChecked(false);
        }
        Log.d("page", "onPageSelected: " + position);
        mBottomNavigation.getMenu().getItem(position).setChecked(true);
        mPrevMenuItem = mBottomNavigation.getMenu().getItem(position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle bundle = getIntent().getExtras();
        if (null!=bundle){
            int position = bundle.getInt("position", 0);
            mViewPager.setCurrentItem(position);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConnectServer.destroy();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        viewExitApp();
    }

    private void viewExitApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cảnh báo");
        builder.setMessage("Bạn có muốn thoát ứng dụng.");
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                System.exit(1);
            }
        }).setPositiveButton("Hủy", null);
        AlertDialog mAlertDialog = builder.create();
        mAlertDialog.show();
    }
}
