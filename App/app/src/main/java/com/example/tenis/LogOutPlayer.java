package com.example.tenis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class LogOutPlayer extends Fragment{


    ProgressDialog pd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_log_out_player, container, false);

        pd = new ProgressDialog(getContext());

        showRecoverPasswordDialog();

        return rootView;
    }

    private void showRecoverPasswordDialog() {
        pd.setMessage("Wylogowywanie...");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chcesz się wylogować?");
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        //przycisk potwierdzenia
        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LogIn.class));
                //Toast.makeText(getContext(), "Wylogowano", Toast.LENGTH_SHORT).show();
            }
        });
        //przycisk anulowania
        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //pokaż dialog
        builder.create().show();

    }
}