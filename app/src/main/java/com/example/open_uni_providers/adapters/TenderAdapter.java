package com.example.open_uni_providers.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.open_uni_providers.R;
import com.example.open_uni_providers.models.Tender;
import com.example.open_uni_providers.models.User;
import com.example.open_uni_providers.screens.ViewContentActivity;
import com.example.open_uni_providers.services.DatabaseService;
import com.example.open_uni_providers.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class TenderAdapter extends RecyclerView.Adapter<TenderAdapter.ViewHolder> {

    public interface OnTenderClickListener {
        void onClick(Tender Tender);
        void onLongClick(Tender Tender);
        void onEditWinnerClick(Tender tender);
        void onEditStatusClick(Tender tender);
        void onApplyClick(Tender tender);
        void onViewContentEmpClick(Tender tender);
        void onViewContentProClick(Tender tender);

        boolean showProviderLayout(Tender tender);
        boolean showEmployeeLayout(Tender tender);
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


        holder.itemView.setOnClickListener(v -> {
            onTenderClickListener.onClick(tender);
        });

        holder.itemView.setOnLongClickListener(v -> {
            onTenderClickListener.onLongClick(tender);
            return true;
        });

        holder.BtnEditWinner.setOnClickListener(v -> {
            onTenderClickListener.onEditWinnerClick(tender);
        });
        holder.BtnEditStatus.setOnClickListener(v -> {
            onTenderClickListener.onEditStatusClick(tender);
        });
        holder.BtnApply.setOnClickListener(v -> {
            onTenderClickListener.onApplyClick(tender);
        });
        holder.BtnViewContentEmp.setOnClickListener(v -> {
            onTenderClickListener.onViewContentEmpClick(tender);
        });
        holder.BtnViewContentPro.setOnClickListener(v -> {
            onTenderClickListener.onViewContentProClick(tender);
        });

        if (onTenderClickListener.showEmployeeLayout(tender)) {
            holder.employeeLayout.setVisibility(View.VISIBLE);
        } else {
            holder.employeeLayout.setVisibility(View.GONE);
        }
        if (onTenderClickListener.showProviderLayout(tender)) {
            holder.providerLayout.setVisibility(View.VISIBLE);
        } else {
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
        TextView tvSubject, tvPublish, tvExpire, tvWinner, tvStatus;
        public Button BtnEditWinner, BtnViewContentEmp, BtnViewContentPro, BtnApply, BtnEditStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            employeeLayout = itemView.findViewById(R.id.item_tender_employee_layout);
            providerLayout = itemView.findViewById(R.id.item_tender_provider_layout);
            tvSubject = itemView.findViewById(R.id.tv_item_tender_subject);
            tvPublish = itemView.findViewById(R.id.tv_item_publish_date);
            tvExpire = itemView.findViewById(R.id.tv_item_expire_date);
            tvWinner = itemView.findViewById(R.id.tv_item_winner);
            tvStatus = itemView.findViewById(R.id.tv_item_status);
            BtnEditWinner = itemView.findViewById(R.id.btn_edit_winner);
            BtnViewContentEmp = itemView.findViewById(R.id.btn_view_content_emp);
            BtnViewContentPro = itemView.findViewById(R.id.btn_view_content_pro);
            BtnApply = itemView.findViewById(R.id.btn_apply);
            BtnEditStatus = itemView.findViewById(R.id.btn_edit_status);
        }

        void setInfo(Tender tender) {
            this.tvSubject.setText(tender.getTenSubj());
            this.tvPublish.setText(tender.getPubDate());
            this.tvExpire.setText(tender.getPubDate());
            this.tvWinner.setText(tender.getTenWinner());
            this.tvStatus.setText(tender.getTenStat());
        }
    }
}