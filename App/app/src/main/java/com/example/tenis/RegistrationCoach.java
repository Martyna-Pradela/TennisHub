package com.example.tenis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegistrationCoach extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText firstNameT, lastNameT, emailT, passwordT, phoneT;
    private TextView birthT, clubT, birthSmall, clubSmall;
    private Button registration;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_coach);

        firstNameT = findViewById(R.id.editNameC);
        lastNameT = findViewById(R.id.editLastNameC);
        emailT = findViewById(R.id.editEmailC);
        passwordT = findViewById(R.id.editPasswordC);
        phoneT = findViewById(R.id.editPhoneC);

        birthT = (TextView)findViewById(R.id.textView5);
        clubT = (TextView)findViewById(R.id.textView21);
        birthSmall = (TextView) findViewById(R.id.textView4);
        clubSmall = (TextView) findViewById(R.id.textView20);

        registration = (Button) findViewById(R.id.button3);

        registration.setOnClickListener(this);
        birthT.setOnClickListener(this);

        registerForContextMenu(clubT);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button3:
                addDataBase();
                String emailLog = emailT.getText().toString();
                String passwordLog = passwordT.getText().toString();

                if(TextUtils.isEmpty(emailLog) || TextUtils.isEmpty(passwordLog)){
                    Toast.makeText(getApplicationContext(), "Uzupełnij wszystkie pola", Toast.LENGTH_SHORT).show();
                }else if(passwordT.length()<8){
                    Toast.makeText(getApplicationContext(), "Hasło musi mieć minimum 8 znaków", Toast.LENGTH_SHORT).show();
                }else{
                    registerUser(emailLog, passwordLog);
                }
                break;
            case R.id.textView5:
                dateOfBirth();
                break;
        }
    }

    private void registerUser(String email, String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationCoach.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(), LogIn.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Błąd rejestracji", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addDataBase(){
        FirebaseFirestore bd = FirebaseFirestore.getInstance();
        String firstName, lastName, email, phone, birthday, club, type, password;
        firstName = firstNameT.getText().toString();
        lastName = lastNameT.getText().toString();
        email = emailT.getText().toString();
        phone = phoneT.getText().toString();
        birthday = birthT.getText().toString();
        club = clubT.getText().toString();
        password = passwordT.getText().toString();

        Map<String, String> coach = new HashMap<>();
        coach.put("first_name", firstName);
        coach.put("last_name", lastName);
        coach.put("email", email);
        coach.put("phone", phone);
        coach.put("birth", birthday);
        coach.put("club", club);

        if(password.length()<8){
            Toast.makeText(getApplicationContext(), "Hasło musi mieć minimum 8 znaków", Toast.LENGTH_SHORT).show();
        }else {
            bd.collection("trainers").document(email).set(coach).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Zarejestrowano", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void dateOfBirth(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(RegistrationCoach.this,
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
                    date = year + "-0" + month + "-0" + day;
                    birthT.setText(date);
                    birthSmall.setVisibility(View.VISIBLE);
                }
                else if(day<10 && month>=10)  {
                    date = year + "-" + month + "-0" + day;
                    birthT.setText(date);
                    birthSmall.setVisibility(View.VISIBLE);
                }
                else if(day>=10 && month<10){
                    date = year + "-0" + month + "-" + day;
                    birthT.setText(date);
                    birthSmall.setVisibility(View.VISIBLE);
                }
                else {
                    date = year + "-" + month + "-" + day;
                    birthT.setText(date);
                    birthSmall.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_coach, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.winners:
                clubSmall.setVisibility(View.VISIBLE);
                clubT.setText("Winners");
                break;
            case R.id.mp:
                clubSmall.setVisibility(View.VISIBLE);
                clubT.setText("MP");
                break;
            case R.id.reduco:
                clubSmall.setVisibility(View.VISIBLE);
                clubT.setText("Reduco");
                break;
        }
        return super.onContextItemSelected(item);
    }
}