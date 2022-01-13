package com.example.tenis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FindCourtRez extends Fragment implements View.OnClickListener{

    final String CLOCK = "clock";
    final String TYPE = "type";
    final String IMAGE = "image";

    private TextView dateTV, timeTV;
    private Button dateIV, timeIV, show;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    RelativeLayout relativeLayout;
    FirebaseFirestore db;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;
    SimpleAdapter sAdapter;
    String[] from;
    int[]to;
    ListView listViewHT1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_find_court_rez, container, false);

        dateTV = rootView.findViewById(R.id.date);
        timeTV = rootView.findViewById(R.id.time);

        dateIV = (Button) rootView.findViewById(R.id.dateIV);
        timeIV = (Button) rootView.findViewById(R.id.timeIV);
        show = (Button) rootView.findViewById(R.id.show);

        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.rl2);

        listViewHT1 = (ListView) rootView.findViewById(R.id.listViewHT1);

        registerForContextMenu(timeIV);

        db = FirebaseFirestore.getInstance();

        dateIV.setOnClickListener(this);
        show.setOnClickListener(this);

        data = new ArrayList<Map<String,Object>>();
        from = new String[]{CLOCK, TYPE, IMAGE};
        to = new int[]{R.id.clock, R.id.type, R.id.image};

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dateIV:
                showCalendar();
                break;
            case R.id.show:
                relativeLayout.setVisibility(View.VISIBLE);
                String data = dateTV.getText().toString();
                showCourts(data);
                break;
        }
    }
    public void showCourts(String show){
        db.collection("Winners").whereEqualTo("show", show+"-HT1").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                data.clear();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String clock = document.get("clock").toString();
                        String type = document.get("type").toString();
                        String per = document.get("per").toString();

                        m = new HashMap<String, Object>();
                        if(per.equals("Z")){
                        int img = R.drawable.border_ht1;
                            m.put(CLOCK, clock);
                            m.put(TYPE, type);
                            m.put(IMAGE, img);
                            data.add(m);
                        }else if(per.equals("N")){
                            m.put(CLOCK, clock);
                            m.put(TYPE, type);
                            data.add(m);
                        }
                    }
                }
                sAdapter = new SimpleAdapter(getContext(), data, R.layout.row_courts_ht1, from, to);
                listViewHT1.setAdapter(sAdapter);
            }
        });
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, 0, 0, "Winners");
        menu.add(0,1,0,"MP");
        menu.add(0,2,0,"Reduco");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                timeTV.setText("Winners");
                break;
            case 1:
                timeTV.setText("MP");
                break;
            case 2:
                timeTV.setText("Reduco");
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void showCalendar(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                onDateSetListener, year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date;
                if(day<10 && month<10) {
                    date = "0" + day + "-0" + month + "-" + year;
                    dateTV.setText(date);
                    dateTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                }
                else if(day<10 && month>=10)  {
                    date = "0" + day + "-" + month + "-" +year;
                    dateTV.setText(date);
                    dateTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                }
                else if(day>=10 && month<10){
                    date = day + "-0" + month +"-" + year;
                    dateTV.setText(date);
                    dateTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                }
                else {
                    date = day + "-" + month + "-" + year;
                    dateTV.setText(date);
                    dateTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                }
            }
        };
    }
}