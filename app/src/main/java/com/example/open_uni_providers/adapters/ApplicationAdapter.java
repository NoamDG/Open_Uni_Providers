package com.example.open_uni_providers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.open_uni_providers.R;
import com.example.open_uni_providers.models.Application;

import java.util.ArrayList;
import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {

    private final List<Application> applicationList;
    private final OnApplicationClickListener onApplicationClickListener;

    public ApplicationAdapter(@NonNull final OnApplicationClickListener onApplicationClickListener) {
        applicationList = new ArrayList<>();
        this.onApplicationClickListener = onApplicationClickListener;
    }

    @NonNull
    @Override
    public ApplicationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_application, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Application application = applicationList.get(position);
        if (application == null) return;

        holder.setInfo(application);


        holder.itemView.setOnClickListener(v -> {
            onApplicationClickListener.onClick(application);
        });

        holder.itemView.setOnLongClickListener(v -> {
            onApplicationClickListener.onLongClick(application);
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    public void setApplicationList(List<Application> applicationList) {
        this.applicationList.clear();
        this.applicationList.addAll(applicationList);
        notifyDataSetChanged();
    }

    public void addApplication(Application application) {
        applicationList.add(application);
        notifyItemInserted(applicationList.size() - 1);
    }

    public void updateApplication(Application application) {
        int index = applicationList.indexOf(application);
        if (index == -1) return;
        applicationList.set(index, application);
        notifyItemChanged(index);
    }

    public void removeApplication(Application application) {
        int index = applicationList.indexOf(application);
        if (index == -1) return;
        applicationList.remove(index);
        notifyItemRemoved(index);
    }

    public interface OnApplicationClickListener {
        void onClick(Application application);

        void onLongClick(Application application);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFName, tvLName, subject, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.tv_item_app_sub);
            tvFName = itemView.findViewById(R.id.tv_item_app_fname);
            tvLName = itemView.findViewById(R.id.tv_item_app_lname);
            status = itemView.findViewById(R.id.tv_item_app_status);
        }

        void setInfo(Application application) {
            this.status.setText(application.getStatus());
            this.subject.setText(application.getSubject());
            this.tvFName.setText(application.getfName());
            this.tvLName.setText(application.getlName());
        }
    }
}