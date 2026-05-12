package com.example.my_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
private TextView mInfTextView;
    private Button mElefantConterButton;
    private Button mConterButton;
    private int mCount=0;
    private int mCount_1=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        mInfTextView = findViewById(R.id.textView);
        mElefantConterButton=findViewById(R.id.counter_elefhants);
        mConterButton=findViewById(R.id.button_clickers);
        mElefantConterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mInfTextView.setText("I can see "+ ++mCount + " elefants");
            }


        });
        mConterButton=findViewById(R.id.button_clickers);
        mConterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mInfTextView.setText("Click number: "+ ++mCount_1);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onClick(View view) {
        mInfTextView.setText("Hello elefhants");
    }
}