package mycompany.com.nlcn.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mycompany.com.nlcn.Constant;
import mycompany.com.nlcn.Model.ItemSanpham;
import mycompany.com.nlcn.R;
import mycompany.com.nlcn.utils.Number;

public class SPDonHangRecyclerViewAdapter extends RecyclerView.Adapter<SPDonHangRecyclerViewAdapter.Holder> {
    private onClickListener onClickListener;
    private List<ItemSanpham> mSanPhams;


    public SPDonHangRecyclerViewAdapter(List<ItemSanpham> mSanPhams) {
        this.mSanPhams = mSanPhams;
    }

    @NonNull
    @Override
    public SPDonHangRecyclerViewAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sanpham_dathang, parent, false);

        return new Holder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Log.e("Adapter", "onBindViewHolder: "+  mSanPhams.get(position).getImgurl() );
        Picasso.get().load(Constant.URL_SERVER + mSanPhams.get(position).getImgurl()).fit().centerCrop().into(holder.mImageView);
        holder.mNameAgri.setText(mSanPhams.get(position).getTensp());
        holder.tongTien.setText("Giá mua: " + Number.convertNumber(mSanPhams.get(position).getGiasp()) + " VND");
        holder.mSanLuong.setText("Sản lượng mua: " + Number.convertNumber(mSanPhams.get(position).getSanluong()) + " Gam");

    }

    @Override
    public int getItemCount() {
        return mSanPhams.size();
    }

    public void setOnClickListener(SPDonHangRecyclerViewAdapter.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface onClickListener {
        void onItemClick(int position, String idSanPham);
    }

    class Holder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView mNameAgri;
        TextView mSanLuong;
        TextView tongTien;

        public Holder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.imageView);
            mNameAgri = (TextView) view.findViewById(R.id.textView_tensp);
            mSanLuong = (TextView) view.findViewById(R.id.textView_gia_mua_sp);
            tongTien = (TextView) view.findViewById(R.id.textView_san_luong_mua);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClick(getAdapterPosition(), mSanPhams.get(getAdapterPosition()).getId());
                }
            });
        }
    }
}
