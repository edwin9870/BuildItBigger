package com.edwin.android.builditbigger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.edwin.android.jokesdisplayer.ShowJokeActivity;
import com.edwin.android.jokesdisplayer.ShowJokeFragment;
import com.edwin.backend.jokeApi.JokeApi;
import com.edwin.builditbigger.R;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getIdlingResource();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {

        new AsyncTask<Void, Void, String>() {

            private JokeApi apiService;

            @Override
            protected String doInBackground(Void... params) {

                if(mIdlingResource != null) {
                    Log.d(TAG, "Setting idle state to false");
                    mIdlingResource.setIdleState(false);
                }

                JokeApi.Builder builder = new JokeApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null);
                builder.setRootUrl("http://10.0.2.2:8080/_ah/api/")
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

                if(joke == null) {

                    Toast.makeText(MainActivity.this, "Error getting joke", Toast.LENGTH_LONG).show();
                    return;
                }

                if(mIdlingResource != null) {
                    Log.d(TAG, "Setting idle state to true");
                    mIdlingResource.setIdleState(true);
                }

                Intent activityToStart = new Intent(MainActivity.this, ShowJokeActivity.class);
                activityToStart.putExtra(ShowJokeFragment.EXTRA_JOKE, joke);
                startActivity(activityToStart);
            }
        }.execute();
    }


    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
