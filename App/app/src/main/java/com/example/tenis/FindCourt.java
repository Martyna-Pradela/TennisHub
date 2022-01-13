package com.example.tenis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class FindCourt extends Fragment implements View.OnClickListener {



    private TextView dateTV, timeTV;
    private Button dateIV, timeIV;
    private ListView listView;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private Button clock6, clock7, clock8, clock9, clock10, clock11, clock12, clock13, clock14, clock15, clock16, clock17, clock18, clock19, clock20, clock21, show;
    private RelativeLayout relativeLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_find_court, container, false);
        
        dateTV = rootView.findViewById(R.id.date);
        timeTV = rootView.findViewById(R.id.time);
        
        dateIV = (Button) rootView.findViewById(R.id.dateIV);
        timeIV = (Button) rootView.findViewById(R.id.timeIV);

        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.rel);
        
        dateIV.setOnClickListener(this);
        timeIV.setOnClickListener(this);
        
        listView = rootView.findViewById(R.id.listViewCourt);

        clock6 = (Button) rootView.findViewById(R.id.b1);
        clock7 = (Button) rootView.findViewById(R.id.b2);
        clock8 = (Button) rootView.findViewById(R.id.b3);
        clock9 = (Button) rootView.findViewById(R.id.b4);
        clock10 = (Button) rootView.findViewById(R.id.b5);
        clock11 = (Button) rootView.findViewById(R.id.b6);
        clock12 = (Button) rootView.findViewById(R.id.b7);
        clock13 = (Button) rootView.findViewById(R.id.b8);
        clock14 = (Button) rootView.findViewById(R.id.b9);
        clock15 = (Button) rootView.findViewById(R.id.b10);
        clock16 = (Button) rootView.findViewById(R.id.b11);
        clock17 = (Button) rootView.findViewById(R.id.b12);
        clock18 = (Button) rootView.findViewById(R.id.b13);
        clock19 = (Button) rootView.findViewById(R.id.b14);
        clock20 = (Button) rootView.findViewById(R.id.b15);
        clock21 = (Button) rootView.findViewById(R.id.b16);
        show = (Button) rootView.findViewById(R.id.show);

        clock6.setOnClickListener(this);
        clock7.setOnClickListener(this);
        clock8.setOnClickListener(this);
        clock9.setOnClickListener(this);
        clock10.setOnClickListener(this);
        clock11.setOnClickListener(this);
        clock12.setOnClickListener(this);
        clock13.setOnClickListener(this);
        clock14.setOnClickListener(this);
        clock15.setOnClickListener(this);
        clock16.setOnClickListener(this);
        clock17.setOnClickListener(this);
        clock18.setOnClickListener(this);
        clock19.setOnClickListener(this);
        clock20.setOnClickListener(this);
        clock21.setOnClickListener(this);
        show.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dateIV:
                showCalendar();
                break;
            case R.id.timeIV:
                relativeLayout.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
                break;
            case R.id.b1:
                showTime(clock6);
                break;
            case R.id.b2:
                showTime(clock7);
                break;
            case R.id.b3:
                showTime(clock8);
                break;
            case R.id.b4:
                showTime(clock9);
                break;
            case R.id.b5:
                showTime(clock10);
                break;
            case R.id.b6:
                showTime(clock11);
                break;
            case R.id.b7:
                showTime(clock12);
                break;
            case R.id.b8:
                showTime(clock13);
                break;
            case R.id.b9:
                showTime(clock14);
                break;
            case R.id.b10:
                showTime(clock15);
                break;
            case R.id.b11:
                showTime(clock16);
                break;
            case R.id.b12:
                showTime(clock17);
                break;
            case R.id.b13:
                showTime(clock18);
                break;
            case R.id.b14:
                showTime(clock19);
                break;
            case R.id.b15:
                showTime(clock20);
                break;
            case R.id.b16:
                showTime(clock21);
                break;
            case R.id.show:
                break;
        }
    }

    private void showTime(Button button) {
        relativeLayout.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
        String time = button.getText().toString();
        timeTV.setText(time);
        timeTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
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