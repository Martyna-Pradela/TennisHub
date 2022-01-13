package com.example.tenis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText email, password;
    private Button buttonLog;
    private TextView registration, forgotPassword;
    private FirebaseAuth auth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editEmailLog);
        password = findViewById(R.id.editPasswordLog);

        buttonLog = findViewById(R.id.buttonLog);

        registration = findViewById(R.id.textView3);
        forgotPassword = findViewById(R.id.forgotPassword);

        buttonLog.setOnClickListener(this);
        registration.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonLog:
                String emailLog = email.getText().toString();
                String passwordLog = password.getText().toString();
                if(emailLog.equals("") || passwordLog.equals("")){
                    Toast.makeText(getApplicationContext(), "Uzupełnij login lub hasło", Toast.LENGTH_SHORT).show();
                }else
                    FirebaseDatabase.getInstance().getReference().child("Email").setValue(emailLog);
                loginUser(emailLog, passwordLog);
                break;
            case R.id.textView3:
                startActivity(new Intent(getApplicationContext(),Choose.class));
                finish();
                break;
            case R.id.forgotPassword:
                showRecoverPasswordDialog();
                break;
        }
    }

    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Resetuj hasło");
        LinearLayout linearLayout = new LinearLayout(this);
        EditText email = new EditText(this);
        email.setHint("Email");
        email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        email.setMinEms(16);
        linearLayout.addView(email);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        //przycisk potwierdzenia
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String emailStr = email.getText().toString().trim();
                beginRecovery(emailStr);
            }
        });
        //przycisk anulowania
        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //pokaż dialog
        builder.create().show();
    }

    private void beginRecovery(String email) {
        pd.setMessage("Wysyłanie wiadomości...");
        pd.show();
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                pd.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Wysłano email", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Błąd...", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String email, String password) {
        pd.setMessage("Logowanie...");
        pd.show();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    pd.dismiss();
                    FirebaseUser user = auth.getCurrentUser();
                    startActivity(new Intent(getApplicationContext(), MainPlayer.class));
                    finish();
                }else{
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Zły login lub hasło", Toast.LENGTH_SHORT).show();
                }
            }
        });


//    OnSuccessListener(new OnSuccessListener<AuthResult>() {
//            @Override
//            public void onSuccess(AuthResult authResult) {
//                Toast.makeText(getApplicationContext(), "Zalogowano", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getApplicationContext(), MainPlayer.class));
//                finish();
//            }
//        });
    }
}