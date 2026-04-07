package com.example.open_uni_providers.adapters;
import com.example.open_uni_providers.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.open_uni_providers.models.User;
import com.example.open_uni_providers.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    public interface OnUserClickListener {
        void onClick(User user);
        void onLongClick(User user);
        void onUpdateUserClick(User user);
        void onDeleteUserClick(User user);
        void onMakeUserAdminClick(User user);
    }

    private final List<User> userList;
    private final OnUserClickListener onUserClickListener;

    public UserAdapter(@NonNull final OnUserClickListener onUserClickListener) {
        this.userList = new ArrayList<>();
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        if (user == null) return;

        holder.setInfo(user);

        holder.itemView.setOnClickListener(v -> onUserClickListener.onClick(user));
        holder.itemView.setOnLongClickListener(v -> {
            onUserClickListener.onLongClick(user);
            return true;
        });
        holder.BtnUpdateUserInfo.setOnClickListener(v -> onUserClickListener.onUpdateUserClick(user));
        holder.BtnDeleteUser.setOnClickListener(v -> onUserClickListener.onDeleteUserClick(user));
        holder.BtnMakeAdmin.setOnClickListener(v -> onUserClickListener.onMakeUserAdminClick(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setUserList(List<User> users) {
        this.userList.clear();
        this.userList.addAll(users);
        notifyDataSetChanged();
    }

    public void removeUser(User user) {
        int index = userList.indexOf(user);
        if (index != -1) {
            userList.remove(index);
            notifyItemRemoved(index);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFName, tvLName, tvEmail, tvPassword, tvID, tvAdminBadge, status, im64;
        ImageView profPic;
        Button BtnUpdateUserInfo, BtnDeleteUser, BtnMakeAdmin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tv_item_user_id);
            tvFName = itemView.findViewById(R.id.tv_item_user_fname);
            tvLName = itemView.findViewById(R.id.tv_item_user_lname);
            tvEmail = itemView.findViewById(R.id.tv_item_user_email);
            tvPassword = itemView.findViewById(R.id.tv_item_user_password);
            status = itemView.findViewById(R.id.tv_item_user_status);
            profPic = itemView.findViewById(R.id.iv_item_user_pfp);
            BtnUpdateUserInfo = itemView.findViewById(R.id.btn_update_user);
            BtnDeleteUser = itemView.findViewById(R.id.btn_delete_user);
            BtnMakeAdmin = itemView.findViewById(R.id.btn_make_admin);
            tvAdminBadge = itemView.findViewById(R.id.tv_admin_badge);
        }

        void setInfo(User user) {
            tvID.setText(user.getId());
            tvFName.setText(user.getFirstname());
            tvLName.setText(user.getLastname());
            tvEmail.setText(user.getEmail());

            profPic.setImageBitmap(ImageUtil.fromBase64(user.getIm64()));
            // Masking password for security in the list
            tvPassword.setText("********");
            if (user.isAdmin()){
                tvAdminBadge.setVisibility(View.VISIBLE);
                BtnMakeAdmin.setVisibility(View.GONE);

            }
            else{
                tvAdminBadge.setVisibility(View.GONE);
                if(user.isEmployee()){
                    BtnMakeAdmin.setVisibility(View.VISIBLE);
                }
                else{
                    BtnMakeAdmin.setVisibility(View.GONE);
                }

            }
            if(user.isEmployee()){
                status.setText("Employee");
            }
            else{
                status.setText("Provider");
            }
        }
    }
}