package com.edwin.android.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.edwin.android.jokesdisplayer.ShowJokeActivity;
import com.edwin.android.jokesdisplayer.ShowJokeFragment;
import com.edwin.builditbigger.R;

import static android.content.ContentValues.TAG;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener,
        ShowJokeAsyncTask.ShowJokeListener {

    private ProgressBar mProgressBar;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        Button showJokeButton = (Button) root.findViewById(R.id.button_show_joke);
        mProgressBar = (ProgressBar) root.findViewById(R.id.progress_bar_loading_indicator);
        showJokeButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "clicked tell joke");

        ShowJokeAsyncTask showJokeAsyncTask = new ShowJokeAsyncTask(getActivity(),  MainActivityFragment.this);
        showJokeAsyncTask.execute();
        mProgressBar.setVisibility(View.VISIBLE);

    }


    @Override
    public void onComplete(String jokeString) {
        mProgressBar.setVisibility(View.INVISIBLE);
        Intent activityToStart = new Intent(getActivity(), ShowJokeActivity.class);
        activityToStart.putExtra(ShowJokeFragment.EXTRA_JOKE, jokeString);
        getActivity().startActivity(activityToStart);
    }
}
