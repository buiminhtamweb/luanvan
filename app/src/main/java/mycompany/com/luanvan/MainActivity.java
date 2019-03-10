package mycompany.com.luanvan;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import mycompany.com.luanvan.Adapter.ViewPagerAdapter;
import mycompany.com.luanvan.Data.ConnectServer;
import mycompany.com.luanvan.Fragment.GoiMonFrag;
import mycompany.com.luanvan.Fragment.HomeFrag;
import mycompany.com.luanvan.Fragment.HoaDonFrag;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        ViewPager.OnPageChangeListener {

    private ViewPagerAdapter mViewPagerAdapter;
    private ViewPager mViewPager;
    private MenuItem mPrevMenuItem;
    private BottomNavigationView mBottomNavigation;
//    private Toolbar mToolbar;

    private HomeFrag mHomeFrag;
    private GoiMonFrag mGoiMonFrag;
    private HoaDonFrag mHoaDonFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
//        mToolbar = findViewById(R.id.toolbar);
//        mToolbar.setTitle("Trang chủ");

        mHomeFrag = new HomeFrag();
        mGoiMonFrag = new GoiMonFrag();
        mHoaDonFrag = new HoaDonFrag();

        mBottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mBottomNavigation.setOnNavigationItemSelectedListener(this);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),mHomeFrag, mGoiMonFrag, mHoaDonFrag);
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
                mGoiMonFrag.layDuLieuGioHang(); //Load lại giỏ hàng
                return true;
            case R.id.navigation_notifications:
                mViewPager.setCurrentItem(2, true);

                //Load lại thông tin cá nhân
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
