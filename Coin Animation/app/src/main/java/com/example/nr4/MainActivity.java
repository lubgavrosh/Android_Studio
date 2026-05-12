package com.example.nr4;

import android.os.Bundle;
import android.view.View;
import android.view.animation.*;
import android.widget.Button;
import android.widget.ImageView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ImageView obraz;

    Button blink, fade, move, slide, rotate, zoom, stop;

    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        obraz = findViewById(R.id.obraz);

        blink = findViewById(R.id.blink);
        fade = findViewById(R.id.fade);
        move = findViewById(R.id.move);
        slide = findViewById(R.id.slide);
        rotate = findViewById(R.id.rotate);
        zoom = findViewById(R.id.zoom);
        stop = findViewById(R.id.stop);


        blink.setOnClickListener(v -> {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.blink);
            obraz.startAnimation(anim);
        });


        fade.setOnClickListener(v -> {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade);
            obraz.startAnimation(anim);
        });


        move.setOnClickListener(v -> {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.move);
            obraz.startAnimation(anim);
        });

        slide.setOnClickListener(v -> {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide);
            obraz.startAnimation(anim);
        });

        rotate.setOnClickListener(v -> {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.rotate);
            obraz.startAnimation(anim);
        });

        zoom.setOnClickListener(v -> {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.zoom);
            obraz.startAnimation(anim);
        });

        stop.setOnClickListener(v -> {
            obraz.clearAnimation();
        });
    }
}