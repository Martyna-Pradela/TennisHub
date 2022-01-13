package com.example.tenis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Favourite extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    final String NAME = "first_name";
    final String LAST_NAME = "last_name";
    final String AGE = "birth";
    final String EMAIL = "email";
    final String IMAGE = "image";
    final String LEVEL = "level";

    DatabaseReference reff;
    FirebaseFirestore db;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;
    SimpleAdapter sAdapter;
    String[] from;
    int[]to;

    ListView listView;
    TextView favouritePlayers, favouriteCoachs;
    private RelativeLayout mesOut, mesMes, mesDelete, relativeLayout, relativeLayout2;
    String mailStr, nameStr, lastNameStr, ageStr, mail;
    private TextView name, lastName, age, email;
    ImageView photo, mesPhoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_favourite, container, false);

        db = FirebaseFirestore.getInstance();

        data = new ArrayList<Map<String,Object>>();
        from = new String[]{NAME, LAST_NAME, AGE, EMAIL, IMAGE, LEVEL};
        to = new int[]{R.id.nameIV, R.id.lastNameIV, R.id.ageIV, R.id.mailIV,R.id.photo, R.id.levelIV};

        listView = (ListView) rootView.findViewById(R.id.listView2);
        favouritePlayers = (TextView) rootView.findViewById(R.id.favPlayers);
        favouriteCoachs = (TextView) rootView.findViewById(R.id.favCoachs);
        mesOut = (RelativeLayout) rootView.findViewById(R.id.mesOut);
        mesMes = (RelativeLayout) rootView.findViewById(R.id.mesMes);
        mesDelete = (RelativeLayout) rootView.findViewById(R.id.mesAdd);
        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.rl2);
        relativeLayout2 = (RelativeLayout) rootView.findViewById(R.id.rl4);
        favouritePlayers.setOnClickListener(this);
        favouriteCoachs.setOnClickListener(this);
        mesOut.setOnClickListener(this);
        mesMes.setOnClickListener(this);
        mesDelete.setOnClickListener(this);

        name = (TextView) rootView.findViewById(R.id.mesName);
        lastName = (TextView) rootView.findViewById(R.id.mesLast);
        age = (TextView) rootView.findViewById(R.id.mesAge);
        email = (TextView) rootView.findViewById(R.id.mesEmail);
        mesPhoto = (ImageView) rootView.findViewById(R.id.mesPhoto);

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

    @SuppressLint("ResourceAsColor")
    public void showPlayers(){
        favouriteCoachs.setTextColor(R.color.black);
        favouritePlayers.setTextColor(R.color.players);
        reff = FirebaseDatabase.getInstance().getReference().child("Email");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String mail4 = snapshot.getValue().toString();
                mail = mail4;
                db.collection(mail4+"-favourite").whereEqualTo("type", "P").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        data.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String firstName = document.get("first_name").toString();
                                String lastName = document.get("last_name").toString();
                                String mail = document.get("email").toString();
                                String birth = document.get("birth").toString();
                                String level = document.get("level").toString();

                                m = new HashMap<String, Object>();
                                m.put(NAME, firstName);
                                int img = R.drawable.border_favourite;
                                m.put(LAST_NAME, lastName);
                                m.put(AGE, age(birth) + " lat");
                                m.put(EMAIL, mail);
                                m.put(IMAGE, img);
                                m.put(LEVEL, level);
                                data.add(m);
                            }
                        }
                        sAdapter = new SimpleAdapter(getContext(), data, R.layout.row_users_favourite, from, to);
                        listView.setAdapter(sAdapter);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
    @SuppressLint("ResourceAsColor")
    public void showCoachs(){
        favouriteCoachs.setTextColor(R.color.players);
        favouritePlayers.setTextColor(R.color.black);
        reff = FirebaseDatabase.getInstance().getReference().child("Email");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String mail4 = snapshot.getValue().toString();

                db.collection(mail4+"-favourite").whereEqualTo("type", "T").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        data.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String firstName = document.get("first_name").toString();
                                String lastName = document.get("last_name").toString();
                                String mail = document.get("email").toString();
                                String birth = document.get("birth").toString();
                                String club = document.get("club").toString();

                                m = new HashMap<String, Object>();
                                m.put(NAME, firstName);
                                int img = R.drawable.border_favourite_coach;
                                m.put(LAST_NAME, lastName);
                                m.put(AGE, age(birth) + " lat");
                                m.put(EMAIL, mail);
                                m.put(IMAGE, img);
                                m.put(LEVEL, club);
                                data.add(m);
                            }
                        }
                        sAdapter = new SimpleAdapter(getContext(), data, R.layout.row_users_favourite, from, to);
                        listView.setAdapter(sAdapter);
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
            case R.id.favPlayers:
                showPlayers();
                break;
            case R.id.favCoachs:
                showCoachs();
                break;
            case R.id.mesOut:
                relativeLayout.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
                relativeLayout2.setBackgroundColor(0x00000000);
                break;
            case R.id.mesMes:
                Intent intent = new Intent(getContext(), Chat.class);
                startActivity(intent);
                break;
            case R.id.mesAdd:
                deletePlayer(mail, mailStr);
                relativeLayout.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
                relativeLayout2.setBackgroundColor(0x00000000);
                break;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String click = parent.getItemAtPosition(position).toString();
        String[] str = click.split(",");
        String temp1 = str[2];
        String temp2 = str[3];
        String temp3 = str[4];
        String temp = str[5];
        nameStr = temp3.substring(12, temp3.length());
        ageStr = temp2.substring(7, temp2.length());
        lastNameStr = temp1.substring(11, temp1.length());
        mailStr = temp.substring(7, temp.length()-1);
        relativeLayout.setVisibility(View.VISIBLE);
        relativeLayout2.setBackgroundColor(0x80000000);
        listView.setVisibility(View.INVISIBLE);
        email.setText(mailStr);
        String k = email.getText().toString();
        name.setText(nameStr);
        lastName.setText(lastNameStr);
        age.setText(ageStr);
        FirebaseDatabase.getInstance().getReference().child("EmailMes").setValue(mailStr);
        //photo
        mesPhoto.setImageResource(R.drawable.account);
        Image.downloadImage(k, mesPhoto);
    }

    public void deletePlayer(String myEmail, String hisEmail){
        db.collection(myEmail+"-favourite").document(hisEmail)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Usunięto", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error", e);
                    }
                });
    }
}