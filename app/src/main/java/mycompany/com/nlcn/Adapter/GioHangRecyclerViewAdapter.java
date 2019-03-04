package mycompany.com.nlcn.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mycompany.com.nlcn.Constant;
import mycompany.com.nlcn.Model.SPGioHang;
import mycompany.com.nlcn.R;
import mycompany.com.nlcn.utils.Number;

public class GioHangRecyclerViewAdapter extends RecyclerView.Adapter<GioHangRecyclerViewAdapter.Holder> {
    private Context mContext;
    private onClickListener onClickListener;
    private List<SPGioHang> mGioHang;

    public GioHangRecyclerViewAdapter(Context mContext, List<SPGioHang> mGioHang) {
        this.mContext = mContext;
        this.mGioHang = mGioHang;
    }

    @NonNull
    @Override
    public GioHangRecyclerViewAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sanpham_giohang, parent, false);

        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        Picasso.get().load(Constant.URL_SERVER + mGioHang.get(position).getImgurl()).fit().centerCrop().into(holder.imageView);
        holder.nameAgri.setText(mGioHang.get(position).getTensp());
        holder.price.setText("Giá: " + Number.convertNumber(mGioHang.get(position).getGiasp()) + " VND");
        holder.sanLuong.setText("Tồn kho: " + Number.convertNumber(mGioHang.get(position).getSanluong()) + " Gam");
        holder.sanLuongMua.setText("" + mGioHang.get(position).getSanLuongMua());
    }

    @Override
    public int getItemCount() {
        return mGioHang.size();
    }

    public void setOnClickListener(GioHangRecyclerViewAdapter.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public int getTongTien() {
        int tongtien = 0;
        for (int i = 0; i < mGioHang.size(); i++) {
            tongtien += (mGioHang.get(i).getGiasp() * mGioHang.get(i).getSanLuongMua() / 1000);
        }
        return tongtien;
    }

    public interface onClickListener {
        void onItemClick(int position, String idSanPham);

        void onItemDeleteClick(int position, String idSanPham);

        void onEditTextClick(int positon, String idSanPham, String tenSanPham, int sanLuongMua);
    }

    class Holder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameAgri;
        TextView sanLuong;
        TextView price;
        EditText sanLuongMua;
        ImageButton btnDelete;

        public Holder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            nameAgri = (TextView) view.findViewById(R.id.textView_tensp);
            price = (TextView) view.findViewById(R.id.textView_giasp);
            sanLuong = (TextView) view.findViewById(R.id.textView_sanluong);
            sanLuongMua = (EditText) view.findViewById(R.id.edt_sanluongmua);
            btnDelete = (ImageButton) view.findViewById(R.id.imgView_delete);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Log.e("DELETE", "onClick: ");
                    onClickListener.onItemDeleteClick(position, mGioHang.get(position).getIdSpMua());
                }
            });


            sanLuongMua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    onClickListener.onEditTextClick(position,
                            mGioHang.get(position).getIdSpMua(),
                            mGioHang.get(position).getTensp(),
                            mGioHang.get(position).getSanLuongMua());
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClick(getAdapterPosition(), mGioHang.get(getAdapterPosition()).getIdSpMua());
                }
            });
        }
    }
}
