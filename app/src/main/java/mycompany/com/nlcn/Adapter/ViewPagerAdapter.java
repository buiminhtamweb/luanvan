package mycompany.com.nlcn.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import mycompany.com.nlcn.Fragment.GioHangFrag;
import mycompany.com.nlcn.Fragment.HomeFrag;
import mycompany.com.nlcn.Fragment.UserFrag;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    private HomeFrag mHomeFrag;
    private GioHangFrag mGioHangFrag;
    private UserFrag mUserFrag;
    private String[] mTitle;

    public ViewPagerAdapter(FragmentManager fm, HomeFrag mHomeFrag, GioHangFrag mGioHangFrag, UserFrag mUserFrag) {
        super(fm);
        this.mHomeFrag = mHomeFrag;
        this.mGioHangFrag = mGioHangFrag;
        this.mUserFrag = mUserFrag;


        mTitle = new String[]{"Trang chủ", "Đặt hàng", "Tài khoản"};
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return mHomeFrag;
            case 1:
                return mGioHangFrag;
            case 2:
                return mUserFrag;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }
}
