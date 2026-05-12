package com.example.nr7;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    TextView textSummary;
    Button buttonOrder, buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textSummary = findViewById(R.id.textSummary);
        buttonOrder = findViewById(R.id.buttonOrder);
        buttonCancel = findViewById(R.id.buttonCancel);

        String name = getIntent().getStringExtra("name");
        String product = getIntent().getStringExtra("product");
        String quantity = getIntent().getStringExtra("quantity");

        final String summary = "Klient: " + name +
                "\nProdukt: " + product +
                "\nIlość: " + quantity;

        textSummary.setText(summary);

        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Zamówienie");
                intent.putExtra(Intent.EXTRA_TEXT, summary);

                startActivity(intent);
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}