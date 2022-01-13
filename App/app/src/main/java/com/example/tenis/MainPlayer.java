package com.example.tenis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.text.MessageFormat;

public class MainPlayer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    private DrawerLayout drawer;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_player);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfilPlayer()).commit();
                break;
            case R.id.history:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HistoryPlayer()).commit();
                break;
            case R.id.favourite:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Favourite()).commit();
                break;
            case R.id.message:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Message()).commit();
                break;
            case R.id.logOutMenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LogOutPlayer()).commit();
                break;
            case R.id.findPlayer:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FindPlayer()).commit();
                break;
            case R.id.findCoach:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FindCoach()).commit();
                break;
            case R.id.findClub:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FindClub()).commit();
                break;
            case R.id.findCourt:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FindCourtRez()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}