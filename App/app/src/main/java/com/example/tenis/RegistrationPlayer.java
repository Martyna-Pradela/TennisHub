package com.example.tenis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegistrationPlayer extends AppCompatActivity implements View.OnClickListener {

    private TextView birth, birthHeader, help, skilsSmall, skilsBig;
    private Button registrationButton, ntrpButton;
    private RelativeLayout ntrp;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    TextInputEditText firstNameText, lastNameText, emailText, passwordText, phoneText;
    private FirebaseAuth auth;
    ProgressDialog pd;
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_player);

        birth = (TextView) findViewById(R.id.textView5);
        birthHeader = (TextView) findViewById(R.id.textView4);
        help = (TextView) findViewById(R.id.textView6);
        skilsSmall = (TextView) findViewById(R.id.textView20);
        skilsBig = (TextView) findViewById(R.id.textView21);

        registrationButton = (Button) findViewById(R.id.button3);
        ntrpButton = (Button) findViewById(R.id.button4);

        ntrp = (RelativeLayout) findViewById(R.id.ntrp);

        firstNameText = findViewById(R.id.editName);
        lastNameText = findViewById(R.id.editLastName);
        emailText = findViewById(R.id.editEmail);
        passwordText = findViewById(R.id.editPassword);
        phoneText = findViewById(R.id.editPhone);

        registrationButton.setOnClickListener(this);
        ntrpButton.setOnClickListener(this);
        help.setOnClickListener(this);
        birth.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);

        registerForContextMenu(skilsBig);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button3:
                pd.setMessage("Rejestracja...");
                addDataBase();
                String emailLog = emailText.getText().toString();
                String passwordLog = passwordText.getText().toString();

                if(TextUtils.isEmpty(emailLog) || TextUtils.isEmpty(passwordLog)){
                    Toast.makeText(getApplicationContext(), "Uzupełnij wszystkie pola", Toast.LENGTH_SHORT).show();
                }else if(passwordText.length()<8){
                    Toast.makeText(getApplicationContext(), "Hasło musi mieć minimum 8 znaków", Toast.LENGTH_SHORT).show();
                }else{
                    pd.show();
                    registerUser(emailLog, passwordLog);
                }
                break;
            case R.id.button4:
                registrationButton.setVisibility(View.VISIBLE);
                ntrp.setVisibility(View.INVISIBLE);
                help.setVisibility(View.VISIBLE);
                break;
            case R.id.textView5:
                dateOfBirth();
                break;
            case R.id.textView6:
                registrationButton.setVisibility(View.INVISIBLE);
                ntrp.setVisibility(View.VISIBLE);
                help.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void registerUser(String email, String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationPlayer.this, new OnCompleteListener<AuthResult>() {
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String firstName, lastName, email, phone, birthday, level, type, password;
        firstName = firstNameText.getText().toString();
        lastName = lastNameText.getText().toString();
        email = emailText.getText().toString();
        phone = phoneText.getText().toString();
        birthday = birth.getText().toString();
        level = skilsBig.getText().toString();
        password = passwordText.getText().toString();
        mail = email;

        Map<String, String> player = new HashMap<>();
        player.put("first_name", firstName);
        player.put("last_name", lastName);
        player.put("email", email);
        player.put("phone", phone);
        player.put("birth", birthday);
        player.put("level", level);

        if(password.length()<8){
            Toast.makeText(getApplicationContext(), "Hasło musi mieć minimum 8 znaków", Toast.LENGTH_SHORT).show();
        }else {
            db.collection("players").document(email).set(player).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        DatePickerDialog dialog = new DatePickerDialog(RegistrationPlayer.this,
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
                    birth.setText(date);
                    birthHeader.setVisibility(View.VISIBLE);
                }
                else if(day<10 && month>=10)  {
                    date = year + "-" + month + "-0" + day;
                    birth.setText(date);
                    birthHeader.setVisibility(View.VISIBLE);
                }
                else if(day>=10 && month<10){
                    date = year + "-0" + month + "-" + day;
                    birth.setText(date);
                    birthHeader.setVisibility(View.VISIBLE);
                }
                else {
                    date = year + "-" + month + "-" + day;
                    birth.setText(date);
                    birthHeader.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.ntrp1:
                skilsSmall.setVisibility(View.VISIBLE);
                skilsBig.setText("1.0");
                break;
            case R.id.ntrp2:
                skilsSmall.setVisibility(View.VISIBLE);
                skilsBig.setText("2.0");
                break;
            case R.id.ntrp3:
                skilsSmall.setVisibility(View.VISIBLE);
                skilsBig.setText("3.0");
                break;
            case R.id.ntrp4:
                skilsSmall.setVisibility(View.VISIBLE);
                skilsBig.setText("4.0");
                break;
            case R.id.ntrp5:
                skilsSmall.setVisibility(View.VISIBLE);
                skilsBig.setText("5.0");
                break;
            case R.id.ntrp6:
                skilsSmall.setVisibility(View.VISIBLE);
                skilsBig.setText("6.0");
                break;
        }
        return super.onContextItemSelected(item);
    }
}