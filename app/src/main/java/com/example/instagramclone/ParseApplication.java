package com.example.instagramclone;

import android.app.Application;

import com.example.instagramclone.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Register Parse models
        ParseObject.registerSubclass(Post.class);

        // set applicationId and server based on values in Heroku settings
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.APP_ID))
                .clientKey(null)
                .server(getString(R.string.SERVER_URL)).build());
    }
}
