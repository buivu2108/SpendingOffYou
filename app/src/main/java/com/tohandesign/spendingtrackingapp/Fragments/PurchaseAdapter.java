package com.tohandesign.spendingtrackingapp.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tohandesign.spendingtrackingapp.R;

import java.util.ArrayList;
import java.util.List;


public class PurchaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PointPackage> pointPackageList = new ArrayList<>();

    private BuyItemListener buyItemListener;

    public PurchaseAdapter(BuyItemListener buyItemListener) {
        this.buyItemListener = buyItemListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).fillData(pointPackageList.get(position));
    }

    @Override
    public int getItemCount() {
        return pointPackageList.size();
    }

    public void addAll(List<PointPackage> packageList) {
        pointPackageList.clear();
        pointPackageList.addAll(packageList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvPrice;
        private Button btnBuy;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnBuy = itemView.findViewById(R.id.btnBuy);
        }

        public void fillData(PointPackage pointPackage) {
            btnBuy.setOnClickListener(v -> buyItemListener.buyItem(pointPackage.getSku()));

            tvPrice.setText(pointPackage.getPrice());
            tvTitle.setText(pointPackage.getTitle());
        }
    }
}
