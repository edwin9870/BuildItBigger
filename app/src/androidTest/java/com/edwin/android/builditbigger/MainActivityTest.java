package com.edwin.android.builditbigger;

import android.support.annotation.NonNull;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Edwin Ramirez Ventura on 6/7/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);
    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        MainActivity activity = mActivityTestRule.getActivity();
        mIdlingResource = activity.getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void checkTellJokeButton() {
        onView(withId(R.id.button_show_joke)).perform(click());
        onView(withId(R.id.text_joke)).check(matches(MainActivityTest.isGreaterTextLengthTextView(0)));

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

    @NonNull
    public static Matcher<View> isGreaterTextLengthTextView(final int minLength) {

        return new BoundedMatcher<View, TextView>(TextView.class) {

            @Override
            public void describeTo(final Description description) {
                description.appendText("Expect TextView's text length greater than "+ minLength);
            }

            @Override
            public boolean matchesSafely(final TextView textView) {
                int textViewLength = textView.getText().length();
                return textViewLength > minLength;
            }
        };
    }

}
