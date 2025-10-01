package com.example.assignment3;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnPlay, btnStop;
    TextView txtStatus;
    ProgressBar progressBar;

    static ProgressBar prog;
    static TextView timeTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = findViewById(R.id.btnPlay);
        btnStop = findViewById(R.id.btnStop);
        txtStatus = findViewById(R.id.txtStatus);
        progressBar = findViewById(R.id.progressBar);

        prog = progressBar;
        timeTxt = findViewById(R.id.txtTime);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, MyService.class));
                txtStatus.setText("Playing");
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, MyService.class));
                txtStatus.setText("Stoped");
                progressBar.setProgress(0);
                timeTxt.setText("0 s");
            }
        });
    }
}
