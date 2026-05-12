package com.example.app_chat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TextView fishText= findViewById(R.id.text_fish);
        TextView catText= findViewById(R.id.text_cat);
        TextView dogText= findViewById(R.id.text_dog);
        TextView carText= findViewById(R.id.text_car);

        ImageView fishimage=findViewById(R.id.image_fish);
        ImageView carimage=findViewById(R.id.image_car);
        ImageView catimage=findViewById(R.id.image_cat);
        ImageView dogimage=findViewById(R.id.image_dog);

fishimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                String[] phrases = {"A good mood is like a ballon", "Doing noting is hard",
                        "Don't worry if plan a fails",
                        "Don't drink while driving",
                        "Every rule has an exception"};

                shuffleArray(phrases);

                fishText.setText(phrases[0]);
                carText.setText(phrases[1]);
                dogText.setText(phrases[2]);
                catText.setText(phrases[3]);
            }
        });
        carimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                String[] phrases = {"A good mood is like a ballon", "Doing noting is hard",
                        "Don't worry if plan a fails",
                        "Don't drink while driving",
                        "Every rule has an exception"};

                shuffleArray(phrases);

                fishText.setText(phrases[0]);
                carText.setText(phrases[1]);
                dogText.setText(phrases[2]);
                catText.setText(phrases[3]);
            }
        });
        catimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                String[] phrases = {"A good mood is like a ballon", "Doing noting is hard",
                        "Don't worry if plan a fails",
                        "Don't drink while driving",
                        "Every rule has an exception"};

                shuffleArray(phrases);

                fishText.setText(phrases[0]);
                carText.setText(phrases[1]);
                dogText.setText(phrases[2]);
                catText.setText(phrases[3]);
            }
        });
       dogimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                String[] phrases = {"A good mood is like a ballon", "Doing noting is hard",
                        "Don't worry if plan a fails",
                        "Don't drink while driving",
                        "Every rule has an exception"};

                shuffleArray(phrases);

                fishText.setText(phrases[0]);
                carText.setText(phrases[1]);
                dogText.setText(phrases[2]);
                catText.setText(phrases[3]);
            }
        });
    }


void shuffleArray(String[] arr)
{
        Random rnd = new Random();
        for (int i = arr.length -1;i>0;i--){
            int index = rnd.nextInt(i+1);
            String a=arr[index];
            arr[index]=arr[i];
            arr[i]=a;
        }
}


    public void onClick(View view) {

    }
}