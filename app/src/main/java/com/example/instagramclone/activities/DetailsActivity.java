package com.example.instagramclone.activities;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;
import com.example.instagramclone.models.Post;
import com.parse.ParseFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        tvTimestamp.setText(getRelativeTimeAgo(post.getCreatedAt()));

    }

    private String getRelativeTimeAgo(Date createdAt) {
        String format = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(format, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(createdAt.toString()).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(),
                    DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] num = relativeDate.split(" ");
        if(num.length > 1) {
            relativeDate = num[0] + num[1].charAt(0) + " ago";
        } else {
            relativeDate = num[0];
        }

        return relativeDate;
    }
}