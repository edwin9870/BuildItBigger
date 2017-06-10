package com.edwin.android.builditbigger;

import android.support.annotation.NonNull;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.edwin.builditbigger.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AnyOf.anyOf;

/**
 * Created by Edwin Ramirez Ventura on 6/7/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    public static final String TAG = MainActivityTest.class.getSimpleName();
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkTellJokeButton() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        new ShowJokeAsyncTask(mActivityTestRule.getActivity(), new ShowJokeAsyncTask.ShowJokeListener() {


            @Override
            public void onComplete(String jokeString) {
                Log.d(TAG, "Joke: "+ jokeString);
                assertThat(jokeString != null, is(true));
                assertThat(jokeString.length() > 0, is(true));


                signal.countDown();
            }
        }).execute();

        signal.await(10, TimeUnit.SECONDS);// wait for callback

    }



}
