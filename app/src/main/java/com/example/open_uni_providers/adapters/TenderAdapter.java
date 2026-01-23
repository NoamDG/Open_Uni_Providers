package com.example.open_uni_providers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.open_uni_providers.R;
import com.example.open_uni_providers.models.Tender;
import com.example.open_uni_providers.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class TenderAdapter extends RecyclerView.Adapter<TenderAdapter.ViewHolder> {

    public interface OnTenderClickListener {
        void onClick(Tender Tender);
        void onLongClick(Tender Tender);
        void onEditWinnerClick(Tender tender);
        void onApplyClick(Tender tender);

        String StatusLayout(Tender tender);
    }

    private final List<Tender> tenderList;
    private final OnTenderClickListener onTenderClickListener;

    public TenderAdapter(@NonNull final OnTenderClickListener onTenderClickListener) {
        tenderList = new ArrayList<>();
        this.onTenderClickListener = onTenderClickListener;
    }

    @NonNull
    @Override
    public TenderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tender, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tender tender = tenderList.get(position);
        if (tender == null) return;

        holder.setInfo(tender);

        holder.itemView.setOnClickListener(v -> onTenderClickListener.onClick(tender));

        holder.itemView.setOnLongClickListener(v -> {
            onTenderClickListener.onLongClick(tender);
            return true;
        });

        holder.BtnEditWinner.setOnClickListener(v -> onTenderClickListener.onEditWinnerClick(tender));
        holder.BtnApply.setOnClickListener(v -> onTenderClickListener.onApplyClick(tender));

        String status = onTenderClickListener.StatusLayout(tender);
        if (status.equals("Employee")) {
            holder.employeeLayout.setVisibility(View.VISIBLE);
            holder.providerLayout.setVisibility(View.GONE);
        }
        else if(status.equals("Provider")) {
            holder.employeeLayout.setVisibility(View.GONE);
            holder.providerLayout.setVisibility(View.VISIBLE);
        }
        else{
            holder.employeeLayout.setVisibility(View.GONE);
            holder.providerLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tenderList.size();
    }

    public void setTenderList(List<Tender> Tenders) {
        tenderList.clear();
        tenderList.addAll(Tenders);
        notifyDataSetChanged();
    }

    public void addTender(Tender Tender) {
        tenderList.add(Tender);
        notifyItemInserted(tenderList.size() - 1);
    }

    public void updateTender(Tender Tender) {
        int index = tenderList.indexOf(Tender);
        if (index == -1) return;
        tenderList.set(index, Tender);
        notifyItemChanged(index);
    }

    public void removeTender(Tender Tender) {
        int index = tenderList.indexOf(Tender);
        if (index == -1) return;
        tenderList.remove(index);
        notifyItemRemoved(index);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View employeeLayout, providerLayout;
        TextView tvSubject, tvPublish, tvExpire, tvWinner, tvStatus, tvCategory;
        public Button BtnEditWinner, BtnApply;
        DatabaseService databaseService;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            employeeLayout = itemView.findViewById(R.id.item_tender_employee_layout);
            providerLayout = itemView.findViewById(R.id.item_tender_provider_layout);
            tvSubject = itemView.findViewById(R.id.tv_item_tender_subject);
            tvPublish = itemView.findViewById(R.id.tv_item_publish_date);
            tvExpire = itemView.findViewById(R.id.tv_item_expire_date);
            tvWinner = itemView.findViewById(R.id.tv_item_winner);
            tvStatus = itemView.findViewById(R.id.tv_item_status);
            tvCategory = itemView.findViewById(R.id.tv_item_category);
            BtnEditWinner = itemView.findViewById(R.id.btn_edit_winner);
            BtnApply = itemView.findViewById(R.id.btn_apply);
            databaseService=DatabaseService.getInstance();
        }

        void setInfo(Tender tender) {
            this.tvSubject.setText(tender.getTenSubj());
            this.tvPublish.setText(tender.getPubDate());
            this.tvExpire.setText(tender.getExpDate());
            this.tvWinner.setText(tender.getTenWinner());
            this.tvStatus.setText(tender.getTenStat());
            this.tvCategory.setText(tender.getCategory());

            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
                sdf.setLenient(false);

                java.util.Date expiryDate = sdf.parse(tender.getExpDate());
                String today = sdf.format(new java.util.Date());
                java.util.Date gToday = sdf.parse(today);
                if (expiryDate != null && gToday.after(expiryDate)) {
                    if (!tender.getTenStat().equals("Ended")) {
                        tender.setTenStat("Ended");
                        databaseService.updateTenderStatusOnly(tender.getId(), "Ended");
                    }
                    tvStatus.setBackgroundColor(android.graphics.Color.parseColor("#808080"));
                    tvStatus.setText(tender.getTenStat());
                    BtnEditWinner.setVisibility(View.GONE);
                    BtnApply.setVisibility(View.GONE);
                }
            }
            catch (java.text.ParseException e) {
                tvStatus.setText(tender.getTenStat());
            }
            if(tender.getTenStat().equals("Inactive")){
                tvStatus.setText(tender.getTenStat());
                BtnApply.setVisibility(View.GONE);
                tvStatus.setBackgroundColor(android.graphics.Color.parseColor("#808080"));
                BtnEditWinner.setVisibility(View.VISIBLE);
            }
            if(tender.getTenStat().equals("Active")){
                tvStatus.setText(tender.getTenStat());
                tvStatus.setBackgroundColor(android.graphics.Color.parseColor("#2E7D32"));
                BtnApply.setVisibility(View.VISIBLE);
                BtnEditWinner.setVisibility(View.VISIBLE);
            }
        }
    }
}