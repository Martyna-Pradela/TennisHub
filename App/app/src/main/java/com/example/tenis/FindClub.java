package com.example.tenis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class FindClub extends Fragment implements View.OnClickListener {

    RelativeLayout winners, mp, reduco;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_find_club, container, false);

        winners = rootView.findViewById(R.id.rlwinners);
        mp = rootView.findViewById(R.id.rlmp);
        reduco = rootView.findViewById(R.id.rlreduco);

        winners.setOnClickListener(this);
        mp.setOnClickListener(this);
        reduco.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rlwinners:
                Intent intent = new Intent(getContext(), Winners.class);
                startActivity(intent);
                break;
                case R.id.rlmp:
                    Intent intent2 = new Intent(getContext(), Mp.class);
                    startActivity(intent2);
                break;
            case R.id.rlreduco:
                Intent intent3 = new Intent(getContext(), Reduco.class);
                startActivity(intent3);
                break;

        }
    }
}