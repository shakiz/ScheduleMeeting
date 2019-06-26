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

public class RecyclerViewAddedUserAdapter extends RecyclerView.Adapter<RecyclerViewAddedUserAdapter.ViewHolder>{
    private ArrayList<UserModel> userModelArrayList;
    private Context context;

    public RecyclerViewAddedUserAdapter(Context context,ArrayList<UserModel> userModelArrayList){
        this.context=context;
        this.userModelArrayList=userModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.recyclerview_horizontal_user_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.userIcon.setImageResource(userModelArrayList.get(position).getIcon());
        viewHolder.userName.setText(userModelArrayList.get(position).getName());
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userModelArrayList.remove(position);
                notifyDataSetChanged();
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView userIcon;
        TextView userName;
        ImageView deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userIcon=itemView.findViewById(R.id.userIconXML);
            userName=itemView.findViewById(R.id.userNameXML);
            deleteButton=itemView.findViewById(R.id.deleteXML);
        }
    }
}
