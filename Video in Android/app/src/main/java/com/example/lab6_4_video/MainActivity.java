package com.example.lab6_4_video;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        VideoView videoView = findViewById(R.id.videoView);
      String path = "android.resource://" + getPackageName() + "/" + R.raw.elef;
        Uri uri = Uri.parse(path);
        videoView.setVideoURI(uri);
        videoView.start();

       /// MediaController mediaController = new MediaController(this);
       /// mediaController.setAnchorView(videoView);
        ///videoView.setMediaController(mediaController);
        ///videoView.start();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}