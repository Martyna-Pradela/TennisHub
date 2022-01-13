package com.example.tenis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Message extends Fragment implements AdapterView.OnItemClickListener {

    final String NAME = "first_name";
    final String LAST_NAME = "last_name";
    final String AGE = "birth";
    final String EMAIL = "email";
    final String IMAGE = "image";

    DatabaseReference reff;
    FirebaseFirestore db;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;
    SimpleAdapter sAdapter;
    String[] from;
    int[]to;
    String mailStr;

    ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_message, container, false);

        db = FirebaseFirestore.getInstance();

        data = new ArrayList<Map<String,Object>>();
        from = new String[]{NAME, LAST_NAME, AGE, EMAIL, IMAGE};
        to = new int[]{R.id.nameIV, R.id.lastNameIV, R.id.ageIV, R.id.mailIV, R.id.photo};

        listView = (ListView) rootView.findViewById(R.id.listView2);
        listView.setOnItemClickListener(this);

        showPlayers();

        return rootView;
    }

    public int age(String birth){
        String ageStr = birth.substring(0,4);
        int ageInt = Integer.parseInt(ageStr);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String strDate = formatter.format(date);
        int dateNow = Integer.parseInt(strDate);
        int ageNow = dateNow-ageInt;
        return ageNow;
    }

    public void showPlayers() {
        reff = FirebaseDatabase.getInstance().getReference().child("Email");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String mail4 = snapshot.getValue().toString();

                db.collection(mail4 + "-message").whereEqualTo("type", "M").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        data.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String firstName = document.get("first_name").toString();
                                String lastName = document.get("last_name").toString();
                                String mail = document.get("email").toString();
                                String birth = document.get("birth").toString();

                                m = new HashMap<String, Object>();
                                m.put(NAME, firstName);
                                int img = R.drawable.border_message;
                                m.put(LAST_NAME, lastName);
                                m.put(AGE, age(birth) + " lat");
                                m.put(EMAIL, mail);
                                m.put(IMAGE, img);
                                data.add(m);
                            }
                        }
                        sAdapter = new SimpleAdapter(getContext(), data, R.layout.row_users, from, to);
                        listView.setAdapter(sAdapter);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), Chat.class);
        startActivity(intent);
    }

}