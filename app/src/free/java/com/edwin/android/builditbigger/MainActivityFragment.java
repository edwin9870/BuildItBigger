package com.edwin.android.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.edwin.android.jokesdisplayer.ShowJokeActivity;
import com.edwin.android.jokesdisplayer.ShowJokeFragment;
import com.edwin.builditbigger.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener, ShowJokeAsyncTask.ShowJokeListener {

    private static final String TAG = MainActivityFragment.class.getSimpleName();
    private InterstitialAd mInterstitialAd;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        Button showJokeButton = (Button) root.findViewById(R.id.button_show_joke);
        showJokeButton.setOnClickListener(this);

        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        mAdView.loadAd(getAdRequest());

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.banner_ad_unit_id));
        mInterstitialAd.loadAd(getAdRequest());

        return root;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "clicked tell joke");
        if (mInterstitialAd.isLoaded()) {
            Log.d(TAG, "Showing interstitial ad");
            mInterstitialAd.show();
        }



        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(getAdRequest());

                Log.d(TAG, "Showing joke activity");
                ShowJokeAsyncTask showJokeAsyncTask = new ShowJokeAsyncTask(getActivity(), MainActivityFragment.this);
                showJokeAsyncTask.execute();
            }
        });
    }


    @NonNull
    private AdRequest getAdRequest() {
        return new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
    }

    @Override
    public void onComplete(String jokeString) {
        Intent activityToStart = new Intent(getActivity(), ShowJokeActivity.class);
        activityToStart.putExtra(ShowJokeFragment.EXTRA_JOKE, jokeString);
        getActivity().startActivity(activityToStart);
    }
}
