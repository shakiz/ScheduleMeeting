package com.example.schedulemeeting.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.schedulemeeting.Model.UserModel;
import com.example.schedulemeeting.R;
import java.util.ArrayList;

public class RecyclerViewUserAdapter extends RecyclerView.Adapter<RecyclerViewUserAdapter.ViewHolder>{
    private ArrayList<UserModel> userModelArrayList;
    private Context context;

    public RecyclerViewUserAdapter(Context context,ArrayList<UserModel> userModelArrayList){
        this.context=context;
        this.userModelArrayList=userModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.recylerview_vertical_user_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.userIcon.setImageResource(userModelArrayList.get(position).getIcon());
        viewHolder.userName.setText(userModelArrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return userModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView userIcon;
        TextView userName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userIcon=itemView.findViewById(R.id.listIcon);
            userName=itemView.findViewById(R.id.userNameXML);
        }
    }

    public void filterList(ArrayList<UserModel> filteredList) {
        userModelArrayList = filteredList;
        notifyDataSetChanged();
    }
}
