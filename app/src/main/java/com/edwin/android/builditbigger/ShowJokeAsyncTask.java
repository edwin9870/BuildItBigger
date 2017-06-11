package com.edwin.android.builditbigger;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.edwin.backend.jokeApi.JokeApi;
import com.edwin.builditbigger.R;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by Edwin Ramirez Ventura on 6/10/2017.
 */

public class ShowJokeAsyncTask extends AsyncTask<Void, Void, String> {

    public static final String TAG = ShowJokeAsyncTask.class.getSimpleName();
    private JokeApi apiService;
    private Context mContext;
    private ShowJokeListener mShowJokeListener;

    public ShowJokeAsyncTask(Context context, ShowJokeListener showJokeListener) {
        this.mContext = context;
        this.mShowJokeListener = showJokeListener;
    }


    @Override
    protected String doInBackground(Void... params) {


        JokeApi.Builder builder = new JokeApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null);
        builder.setRootUrl(mContext.getString(R.string.gce_url))
                .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                    @Override
                    public void initialize(AbstractGoogleClientRequest<?>
                                                   abstractGoogleClientRequest) throws
                            IOException {
                        abstractGoogleClientRequest.setDisableGZipContent(true);
                    }
                });
        apiService = builder.build();
        try {
            return apiService.getJoke().execute().getData();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String joke) {
        Log.d(TAG, "Joke: " + joke);

        if (joke == null) {

            Toast.makeText(mContext, "Error getting joke", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        mShowJokeListener.onComplete(joke);

    }

    interface ShowJokeListener {
        void onComplete(String jokeString);
    }

}
