package mycompany.com.luanvan.Adapter;

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

import mycompany.com.luanvan.Constant;
import mycompany.com.luanvan.Model.ItemMonAn;
import mycompany.com.luanvan.R;
import mycompany.com.luanvan.utils.Number;

public class MADonHangRecyclerViewAdapter extends RecyclerView.Adapter<MADonHangRecyclerViewAdapter.Holder> {
    private onClickListener onClickListener;
    private List<ItemMonAn> mSanPhams;

    public MADonHangRecyclerViewAdapter(List<ItemMonAn> mSanPhams) {
        this.mSanPhams = mSanPhams;
    }

    @NonNull
    @Override
    public MADonHangRecyclerViewAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_monan_dathang, parent, false);

        return new Holder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Log.e("Adapter", "onBindViewHolder: "+  mSanPhams.get(position).getImgurl() );
        Picasso.get().load(Constant.URL_IMG + mSanPhams.get(position).getImgurl()).fit().centerCrop().into(holder.mImageView);
        holder.mNameAgri.setText(mSanPhams.get(position).getTenMA());
        holder.mGiaMua.setText("Giá mua: " + Number.convertNumber(mSanPhams.get(position).getGiaMA()) + " VND");
        holder.mSoLuong.setText("Số lượng: " + (mSanPhams.get(position).getSoLuongMua()) + "");
        holder.tongTien.setText("Thành tiền: " + Number.convertNumber((mSanPhams.get(position).getGiaMA() * mSanPhams.get(position).getSoLuongMua())) + " VND");

    }

    @Override
    public int getItemCount() {
        return mSanPhams.size();
    }

    public void setOnClickListener(MADonHangRecyclerViewAdapter.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface onClickListener {
        void onItemClick(int position, String idSanPham);
    }

    class Holder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView mNameAgri;
        TextView mSoLuong, mGiaMua;
        TextView tongTien;

        public Holder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.imageView);
            mNameAgri = (TextView) view.findViewById(R.id.textView_tensp);
            mSoLuong = (TextView) view.findViewById(R.id.textView_san_luong_mua);
            mGiaMua = (TextView) view.findViewById(R.id.textView_gia_mua_sp);
            tongTien = (TextView) view.findViewById(R.id.textView_tong_tien);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClick(getAdapterPosition(), mSanPhams.get(getAdapterPosition()).getIdMonAn());
                }
            });
        }
    }
}
