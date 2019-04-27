package mycompany.com.luanvan.Adapter;

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

import mycompany.com.luanvan.Constant;
import mycompany.com.luanvan.Model.SPGioHang;
import mycompany.com.luanvan.R;
import mycompany.com.luanvan.utils.Number;

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
                .inflate(R.layout.item_monan_goimon, parent, false);

        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        Picasso.get().load(Constant.URL_IMG + mGioHang.get(position).getImgurl()).fit().centerCrop().into(holder.imageView);
        holder.nameAgri.setText(mGioHang.get(position).getTenMA());
        holder.price.setText("Giá: " + Number.convertNumber(mGioHang.get(position).getGiaMA()) + " VND");
//        holder.sanLuong.setText("Tồn kho: " + Number.convertNumber(mGioHang.get(position).getSanluong()) + " Gam");
        holder.soLuongMua.setText("" + mGioHang.get(position).getSoLuongMua());
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
            tongtien += (mGioHang.get(i).getGiaMA() * mGioHang.get(i).getSoLuongMua());
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
//        TextView sanLuong;
        TextView price;
        EditText soLuongMua;
        ImageButton btnDelete;

        public Holder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            nameAgri = (TextView) view.findViewById(R.id.textView_tensp);
            price = (TextView) view.findViewById(R.id.textView_giasp);
//            sanLuong = (TextView) view.findViewById(R.id.textView_sanluong);
            soLuongMua = (EditText) view.findViewById(R.id.edt_sanluongmua);
            btnDelete = (ImageButton) view.findViewById(R.id.imgView_delete);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Log.e("DELETE", "onClick: ");
                    onClickListener.onItemDeleteClick(position, mGioHang.get(position).getIdMonAn());
                }
            });


            soLuongMua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    onClickListener.onEditTextClick(position,
                            mGioHang.get(position).getIdMonAn(),
                            mGioHang.get(position).getTenMA(),
                            mGioHang.get(position).getSoLuongMua());
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClick(getAdapterPosition(), mGioHang.get(getAdapterPosition()).getIdMonAn());
                }
            });
        }
    }
}
