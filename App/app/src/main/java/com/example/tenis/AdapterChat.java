package com.example.tenis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder> {

    private static final int MSG_LEFT = 0;
    private static final int MSG_RIGHT = 1;
    Context context;
    List<Model> chatList;
    FirebaseUser fUser;
    DatabaseReference reff;
    String mail;

    public AdapterChat(Context context, List<Model> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(viewType==MSG_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_right, parent, false);
            return new MyHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.chat_left, parent, false);
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterChat.MyHolder holder, int position) {
        String message = chatList.get(position).getMessage();
        String time = chatList.get(position).getTime();

        holder.message.setText(message);
        holder.time.setText(time);


    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        reff = FirebaseDatabase.getInstance().getReference().child("Email");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String mail4 = snapshot.getValue().toString();
                mail = mail4;
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
        if(chatList.get(position).getSender().equals(mail)){
            return MSG_LEFT;
        }else{
            return MSG_RIGHT;
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView message, time;

        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.leftMessage);
            time = itemView.findViewById(R.id.leftTime);
        }
    }
}
