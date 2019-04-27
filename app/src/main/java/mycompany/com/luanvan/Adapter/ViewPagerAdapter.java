package mycompany.com.luanvan.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import mycompany.com.luanvan.Fragment.GoiMonFrag;
import mycompany.com.luanvan.Fragment.HomeFrag;
import mycompany.com.luanvan.Fragment.ThanhToanFrag;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    private HomeFrag mHomeFrag;
    private GoiMonFrag mGoiMonFrag;
    private ThanhToanFrag mThanhToanFrag;
    private String[] mTitle;

    public ViewPagerAdapter(FragmentManager fm, HomeFrag mHomeFrag, GoiMonFrag mGoiMonFrag, ThanhToanFrag mThanhToanFrag) {
        super(fm);
        this.mHomeFrag = mHomeFrag;
        this.mGoiMonFrag = mGoiMonFrag;
        this.mThanhToanFrag = mThanhToanFrag;


        mTitle = new String[]{"Trang chủ", "Đặt hàng", "Tài khoản"};
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return mHomeFrag;
            case 1:
                return mGoiMonFrag;
            case 2:
                return mThanhToanFrag;
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
