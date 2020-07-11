package com.example.instagramclone.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.EndlessRecyclerViewScrollListener;
import com.example.instagramclone.R;
import com.example.instagramclone.activities.LoginActivity;
import com.example.instagramclone.adapters.ProfilePostsAdapter;
import com.example.instagramclone.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends PostFragment {

    public static final String TAG = "ProfileFragment";
    private ProfilePostsAdapter adapter;

    // on view created - set username and caption to gone, resize image
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        super.onViewCreated(view, savedInstanceState);

        swipeContainer.setEnabled(false);

        rvPosts = view.findViewById(R.id.rvPosts);
        btnLogout = view.findViewById(R.id.btnLogout);
        setButtonVisibility(view);
        // Steps to use the recycler view
        // 0. create layout for one row in the list
        // 1. create the adapter
        allPosts = new ArrayList<>();
        adapter = new ProfilePostsAdapter(getContext(), allPosts);
        // 2. create the data source
        // 3. set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        // 4. set the layout manager on the recycler view
        rvPosts.setLayoutManager(getLayoutManager());
        queryPosts(0); // get data, update data, and notify adapter there is new data
    }

    @Override
    protected void setButtonVisibility(View view) {
        btnLogout.setVisibility(View.VISIBLE);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logout button clicked
                ParseUser.logOut();
                goLogoutActivity();
            }
        });
    }

    private void goLogoutActivity() {
        Activity oldActivity = getActivity();
        Intent i = new Intent(getContext(), LoginActivity.class);
        startActivity(i);
        oldActivity.finish();
    }

    @Override
    protected GridLayoutManager getLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryPosts(page);
            }
        };
        rvPosts.addOnScrollListener(scrollListener);
        return gridLayoutManager;
    }

    @Override
    protected void queryPosts(final int page) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.setSkip(page*20);
        query.addDescendingOrder(Post.KEY_CREATEDAT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for(Post post: posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                Log.i(TAG, allPosts.toString());
                if(page == 0){
                    adapter.clear();
                }
                adapter.addAll(posts);
                //adapter.notifyDataSetChanged();
            }
        });
    }
}
