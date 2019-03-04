package mycompany.com.nlcn.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mycompany.com.nlcn.Model.ItemDonhang;
import mycompany.com.nlcn.R;

public class DonHangRecyclerViewAdapter extends RecyclerView.Adapter<DonHangRecyclerViewAdapter.Holder> {
    private onClickListener onClickListener;
    private onScrollListener onScrollListener;
    private List<ItemDonhang> mItemDonhangs;


    public DonHangRecyclerViewAdapter(List<ItemDonhang> mItemDonhangs) {
        this.mItemDonhangs = mItemDonhangs;
    }

    @NonNull
    @Override
    public DonHangRecyclerViewAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donhang, parent, false);

        return new Holder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        holder.idDonHang.setText("Mã đơn hàng: " + mItemDonhangs.get(position).getId());
        holder.tongTien.setText("Tổng đơn hàng: " + mItemDonhangs.get(position).getTongTien() + " VND");
        holder.soLuongSPMua.setText("Số lượng sản phẩm mua: " + mItemDonhangs.get(position).getSoluongspmua() + "");
        holder.ngayMua.setText("Ngày mua: " + mItemDonhangs.get(position).getNgayDatHang());
        if(null != mItemDonhangs.get(position).getNgayDuyetDH()){
            holder.ngayDuyet.setVisibility(View.VISIBLE);
            holder.ngayDuyet.setText("Ngày duyệt đơn hàng: " +mItemDonhangs.get(position).getNgayDuyetDH() );
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onItemClick(position, mItemDonhangs.get(position).getId());
            }
        });


        onScrollListener.onScroll(position);
    }

    @Override
    public int getItemCount() {
        return mItemDonhangs.size();
    }

    public void setOnClickListener(DonHangRecyclerViewAdapter.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnScrollListener(DonHangRecyclerViewAdapter.onScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public interface onClickListener {
        void onItemClick(int position, String idDonHang);
    }

    public interface onScrollListener {
        void onScroll(int position);
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
}
