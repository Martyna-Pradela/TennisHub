package com.example.tenis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static androidx.constraintlayout.widget.Constraints.TAG;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindPlayer extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    final String NAME = "first_name";
    final String LAST_NAME = "last_name";
    final String AGE = "birth";
    final String EMAIL = "email";
    final String IMAGE = "image";

    DocumentReference docRef;
    DatabaseReference reff;
    private Button levelButton;
    private TextView levelTextView, mail;
    private ListView listView;
    FirebaseFirestore db;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;
    SimpleAdapter sAdapter;
    String[] from;
    int[]to;
    String mailStr, nameStr, lastNameStr, ageStr;
    ImageView photo, mesPhoto;
    private RelativeLayout relativeLayout, relativeLayout2, out, message, favourite;
    private TextView name, lastName, age, email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_find_player, container, false);

        levelButton = (Button) rootView.findViewById(R.id.chooseLevelB);
        levelTextView = (TextView) rootView.findViewById(R.id.levelTV);
        listView = (ListView) rootView.findViewById(R.id.listView1);
        mail = (TextView) rootView.findViewById(R.id.mailIV);
        photo = (ImageView) rootView.findViewById(R.id.photo);

        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.rl2);
        relativeLayout2 = (RelativeLayout) rootView.findViewById(R.id.rl4);
        out = (RelativeLayout) rootView.findViewById(R.id.mesOut);
        message = (RelativeLayout) rootView.findViewById(R.id.mesMes);
        favourite = (RelativeLayout) rootView.findViewById(R.id.mesAdd);

        name = (TextView) rootView.findViewById(R.id.mesName);
        lastName = (TextView) rootView.findViewById(R.id.mesLast);
        age = (TextView) rootView.findViewById(R.id.mesAge);
        email = (TextView) rootView.findViewById(R.id.mesEmail);
        mesPhoto = (ImageView) rootView.findViewById(R.id.mesPhoto);

        data = new ArrayList<Map<String,Object>>();
        from = new String[]{NAME, LAST_NAME, AGE, EMAIL, IMAGE};
        to = new int[]{R.id.nameIV, R.id.lastNameIV, R.id.ageIV, R.id.mailIV,R.id.photo};

        db = FirebaseFirestore.getInstance();

        registerForContextMenu(levelButton);

        listView.setOnItemClickListener(this);
        out.setOnClickListener(this);
        favourite.setOnClickListener(this);
        message.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, 0, 0, "1.0");
        menu.add(0,1,0,"2.0");
        menu.add(0,2,0,"3.0");
        menu.add(0,3,0,"4.0");
        menu.add(0,4,0,"5.0");
        menu.add(0,5,0,"6.0");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                levelTextView.setText("1.0");
                showPlayers("1.0");
                break;
            case 1:
                levelTextView.setText("2.0");
                showPlayers("2.0");
                break;
            case 2:
                levelTextView.setText("3.0");
                showPlayers("3.0");
                break;
            case 3:
                levelTextView.setText("4.0");
                showPlayers("4.0");
                break;
            case 4:
                levelTextView.setText("5.0");
                showPlayers("5.0");
                break;
            case 5:
                levelTextView.setText("6.0");
                showPlayers("6.0");
                break;
        }
        return super.onContextItemSelected(item);
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

    public void showPlayers(String level){
        db.collection("players").whereEqualTo("level", level).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                data.clear();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String firstName = document.get("first_name").toString();
                        String lastName = document.get("last_name").toString();
                        String mail = document.get("email").toString();
                        String birth = document.get("birth").toString();
                        String phone = document.get("phone").toString();
                        String level = document.get("level").toString();

                        m = new HashMap<String, Object>();
                        m.put(NAME, firstName);
                        int img = R.drawable.border_log_out;
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String click = parent.getItemAtPosition(position).toString();
        String[] str = click.split(",");
        String temp1 = str[1];
        String temp2 = str[2];
        String temp3 = str[3];
        String temp = str[4];
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.mesOut:
                relativeLayout.setVisibility(View.INVISIBLE);
                relativeLayout2.setBackgroundColor(0x00000000);
                listView.setVisibility(View.VISIBLE);
                break;
            case R.id.mesAdd:
                    addFavourite();
                break;
            case R.id.mesMes:
                Intent intent = new Intent(getContext(), Chat.class);
                startActivity(intent);
                break;
        }
    }
    public void addFavourite() {
        db.collection("players").whereEqualTo("email", mailStr).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String firstName = document.get("first_name").toString();
                        String lastName = document.get("last_name").toString();
                        String mail = document.get("email").toString();
                        String birth = document.get("birth").toString();
                        String level = document.get("level").toString();

                        reff = FirebaseDatabase.getInstance().getReference().child("Email");
                        reff.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String mail4 = snapshot.getValue().toString();

                                Map<String, String> favourite = new HashMap<>();
                                favourite.put("first_name", firstName);
                                favourite.put("last_name", lastName);
                                favourite.put("email", mail);
                                favourite.put("birth", birth);
                                favourite.put("level", level);
                                favourite.put("type", "P");
                                    db.collection(mail4+"-favourite").document(mail).set(favourite).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Dodano "+firstName+" "+lastName+ " do ulubionych", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { }
                        });

                    }
                }
            }
        });
    }
}