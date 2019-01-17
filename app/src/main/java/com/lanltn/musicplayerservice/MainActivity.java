package com.lanltn.musicplayerservice;

import android.icu.text.TimeZoneFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lanltn.musicplayerservice.sample.PlayerComponentView;

public class MainActivity extends AppCompatActivity {
    PlayerComponentView playerComponentView;
    Button btnPlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerComponentView = findViewById(R.id.player_component_view);
        btnPlay = findViewById(R.id.button);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerComponentView.setVisibility(View.VISIBLE);
                playerComponentView.onShowPlayer();
                btnPlay.setVisibility(View.GONE);
            }
        });

    }
}
