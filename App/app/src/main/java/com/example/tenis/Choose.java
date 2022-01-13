package com.example.tenis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Choose extends AppCompatActivity implements View.OnClickListener {

    private Button player, coach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        player = findViewById(R.id.player);
        coach = findViewById(R.id.coach);

        player.setOnClickListener(this);
        coach.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.player:
                startActivity(new Intent(getApplicationContext(), RegistrationPlayer.class));
                finish();
                break;
            case R.id.coach:
                startActivity(new Intent(getApplicationContext(), RegistrationCoach.class));
                finish();
                break;
        }
    }
}