package com.example.tenis;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    ImageView imageView;
    TextView nameTV, lastNameTV, message, time;
    EditText editText;
    ImageButton send;

    DatabaseReference reff;
    DocumentReference docRef;
    FirebaseFirestore db;
    DatabaseReference dref;
    ValueEventListener seenListener;

    List<Model> chatList;
    AdapterChat adapterChat;

    String myMail, hisMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.chatRecycler);
        imageView = findViewById(R.id.toolbarMesImage);
        nameTV = findViewById(R.id.toolbarMesName);
        lastNameTV = findViewById(R.id.toolbarMesLast);
        editText = findViewById(R.id.chatEditText);
        send = findViewById(R.id.chatButton);

        message = findViewById(R.id.leftMessage);
        time = findViewById(R.id.leftTime);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        db = FirebaseFirestore.getInstance();

        completeThePanel();

        send.setOnClickListener(this);

        reff = FirebaseDatabase.getInstance().getReference().child("Email");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String mail4 = snapshot.getValue().toString();
                myMail = mail4;
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        readMessages();
    }

    private void completeThePanel() {
        reff = FirebaseDatabase.getInstance().getReference().child("EmailMes");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String mail4 = snapshot.getValue().toString();
                hisMail = mail4;
                docRef = db.collection("players").document(mail4);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String firstName = document.get("first_name").toString();
                                String lastName = document.get("last_name").toString();
                                nameTV.setText(firstName);
                                lastNameTV.setText(lastName);
                                imageView.setImageResource(R.drawable.account);
                                Image.downloadImage(mail4, imageView);
                            }
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.chatButton:
                String message = editText.getText().toString().trim();
                if(TextUtils.isEmpty(message)){
                    Toast.makeText(getApplicationContext(), "Napisz wiadomość", Toast.LENGTH_SHORT).show();
                }else{
                    sendMessage(message);
                }
                break;
        }
    }

    private void readMessages(){
        chatList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                chatList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    Model chat = ds.getValue(Model.class);
                    if(chat.getReceiver().equals(hisMail) && chat.getSender().equals(myMail) ||
                            chat.getReceiver().equals(myMail) && chat.getSender().equals(hisMail)){
                        chatList.add(chat);
                    }
                    adapterChat = new AdapterChat(Chat.this, chatList);
                    adapterChat.notifyDataSetChanged();

                    recyclerView.setAdapter(adapterChat);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    private void sendMessage(String message) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Date nowDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String dateN = sdf.format(nowDate);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myMail);
        hashMap.put("receiver", hisMail);
        hashMap.put("message", message);
        hashMap.put("time", dateN);
        databaseReference.child("Chats").push().setValue(hashMap);

        editText.setText("");

        addData();
        addMyData();
    }
    public void addData(){
    db.collection("players").whereEqualTo("email", hisMail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String firstName = document.get("first_name").toString();
                    String lastName = document.get("last_name").toString();
                    String mail = document.get("email").toString();
                    String birth = document.get("birth").toString();

                    Map<String, String> message = new HashMap<>();
                    message.put("first_name", firstName);
                    message.put("last_name", lastName);
                    message.put("email", hisMail);
                    message.put("birth", birth);
                    message.put("type", "M");
                    db.collection(myMail + "-message").document(mail).set(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                            }
                        }
                    });
                }
            }
        }
    });
}
    public void addMyData(){
        db.collection("players").whereEqualTo("email", myMail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String firstName = document.get("first_name").toString();
                        String lastName = document.get("last_name").toString();
                        String mail = document.get("email").toString();
                        String birth = document.get("birth").toString();

                        Map<String, String> message = new HashMap<>();
                        message.put("first_name", firstName);
                        message.put("last_name", lastName);
                        message.put("email", mail);
                        message.put("birth", birth);
                        message.put("type", "M");
                        db.collection(hisMail + "-message").document(mail).set(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}