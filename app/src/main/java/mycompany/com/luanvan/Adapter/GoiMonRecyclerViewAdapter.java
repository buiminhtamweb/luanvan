package mycompany.com.luanvan.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mycompany.com.luanvan.Model.GoiMon;
import mycompany.com.luanvan.R;
import mycompany.com.luanvan.utils.Number;

public class GoiMonRecyclerViewAdapter extends RecyclerView.Adapter<GoiMonRecyclerViewAdapter.Holder> {
    private onClickListener onClickListener;
    private List<GoiMon> mItemGoiMons;


    public GoiMonRecyclerViewAdapter(List<GoiMon> mItemGoiMons) {
        this.mItemGoiMons = mItemGoiMons;
    }

    @NonNull
    @Override
    public GoiMonRecyclerViewAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donhang, parent, false);

        return new Holder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        holder.idDonHang.setText("Mã gọi món: " + mItemGoiMons.get(position).getId());
        holder.tongTien.setText("Tổng gọi món: " + Number.convertNumber(mItemGoiMons.get(position).getTongTien()) + " VND");
        holder.soLuongSPMua.setText("Số lượng món ăn: " + mItemGoiMons.get(position).getSoluongMA() + "");
        holder.ngayMua.setText("Thời gian gọi món: " + mItemGoiMons.get(position).getNgayGoiMon());
        if (mItemGoiMons.get(position).isDaVanChuyen()) {
            holder.ngayDuyet.setVisibility(View.VISIBLE);
            holder.ngayDuyet.setText("Trạng thái: " + "Đang vận chuyển");
        } else {
            holder.ngayDuyet.setVisibility(View.VISIBLE);
            holder.ngayDuyet.setText("Trạng thái: " + "Đang xử lý");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onItemClick(position, mItemGoiMons.get(position).getId());
            }
        });



    }

    @Override
    public int getItemCount() {
        return mItemGoiMons.size();
    }

    public void setOnItemClickListener(GoiMonRecyclerViewAdapter.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface onClickListener {
        void onItemClick(int position, String idDonHang);
    }


    class Holder extends RecyclerView.ViewHolder {

        TextView idDonHang;
        TextView soLuongSPMua;
        TextView tongTien;
        TextView ngayMua, ngayDuyet;


        public Holder(View view) {
            super(view);

            idDonHang = (TextView) view.findViewById(R.id.textView_id_don_hang);
            soLuongSPMua = (TextView) view.findViewById(R.id.textView_so_luong_sp_mua);
            tongTien = (TextView) view.findViewById(R.id.textView_tong_gia_don_hang);
            ngayMua = (TextView) view.findViewById(R.id.textView_ngay_mua);
            ngayDuyet = (TextView) view.findViewById(R.id.textView_ngay_duyet);


        }
    }

    public int getTongCong() {
        int tongCong = 0;
        for (int i = 0; i < mItemGoiMons.size(); i++) {
            tongCong += mItemGoiMons.get(i).getTongTien();
        }
        return tongCong;
    }
}
