package com.example.instagramclone.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;
import com.example.instagramclone.models.Post;
import com.parse.ParseFile;

public class DetailsActivity extends AppCompatActivity {

    public static final String TAG = "DetailsActivity";
    TextView tvUsername;
    TextView tvDescription;
    TextView tvTimestamp;
    ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvUsername = findViewById(R.id.tvUsername);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        tvDescription = findViewById(R.id.tvDescription);
        ivImage = findViewById(R.id.ivImage);

        Post post = getIntent().getParcelableExtra("post");
        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        ParseFile image = post.getImage();
        if(image != null) {
            Glide.with(getApplicationContext()).load(image.getUrl()).into(ivImage);
            Log.i(TAG, "image to post: " + image.getUrl());
        }
        tvTimestamp.setText(post.getRelativeTimeAgo(post.getCreatedAt()));

    }
}