package com.example.class_work_lesson_2;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout b1_main;
    private TextView mInfTextView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
        mInfTextView = findViewById(R.id.textView);
        b1_main=findViewById(R.id.main);

        Button b_red = findViewById(R.id.red_button);

        Button b_orange = findViewById(R.id.orange_button);

        Button b_yellow = findViewById(R.id.yellow_button);


        b_red.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                b1_main.setBackgroundColor(Color.RED);
                mInfTextView.setText("Red");
            }
        });

        b_orange.setOnClickListener(view -> {

            mInfTextView.setText("Orange");
            b1_main.setBackgroundColor(Color.rgb(225,87,34));
        });

        b_yellow.setOnClickListener(view -> {

            mInfTextView.setText("Yellow");
            b1_main.setBackgroundColor(Color.YELLOW);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    @SuppressLint("SetTextI18n")
    public void onClick(View view) {

            mInfTextView.setText("Set color");
        b1_main.setBackgroundColor(Color.WHITE);

    }
}