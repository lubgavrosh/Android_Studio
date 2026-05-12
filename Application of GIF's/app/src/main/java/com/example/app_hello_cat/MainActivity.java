package com.example.app_hello_cat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView mInfTextView;
    private EditText mEditText;

    private ImageButton mImageButton_1;
    private ImageButton mImageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        mInfTextView = findViewById(R.id.textview);
        mEditText=findViewById(R.id.editText);
        mImageButton_1=findViewById(R.id.imageButton_1);
        mImageButton=findViewById(R.id.imageButton);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void onClick(View view) {
        String text = mEditText.getText().toString().trim();

        if (text.isEmpty()) {
            mImageButton.setVisibility(View.VISIBLE);
            mImageButton_1.setVisibility(View.INVISIBLE);
            mInfTextView.setText("Hello!");

        } else {
            mInfTextView.setText(text);
            mImageButton.setVisibility(View.INVISIBLE);
            mImageButton_1.setVisibility(View.VISIBLE);
            mEditText.setText("");
        }

    }
}