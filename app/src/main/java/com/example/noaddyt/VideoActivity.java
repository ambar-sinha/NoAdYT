package com.example.noaddyt;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoActivity extends AppCompatActivity {
    YouTubePlayerView youtubePlayerView;
    RelativeLayout main;
    YouTubePlayer youTubePlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video);
        main = findViewById(R.id.main);
        String videoId = getYoutubeVideoId(getIntent().getExtras().getString("Url"));
        youtubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youtubePlayerView);

        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer player) {
                youTubePlayer = player;
                // Load default video
                player.loadVideo(videoId, 0);
            }
        });


        ImageButton btnFullscreen = findViewById(R.id.btnFullscreen);

        btnFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int orientation = getResources().getConfiguration().orientation;

                if (orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
                    // Switch to landscape
                    youtubePlayerView.matchParent();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    // Switch back to portrait
                    youtubePlayerView.wrapContent();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });


    }
    public String getYoutubeVideoId(String url) {
        String pattern =
                "(?<=v=|v/|embed/|youtu\\.be/|shorts/)[^&\\n?#]+";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);

        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}