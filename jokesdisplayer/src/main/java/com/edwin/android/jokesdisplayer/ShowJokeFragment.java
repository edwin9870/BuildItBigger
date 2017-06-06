package com.edwin.android.jokesdisplayer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowJokeFragment extends Fragment {


    public static final String EXTRA_JOKE = "EXTRA_JOKE";


    @BindView(R2.id.text_joke)
    TextView jokeTextView;
    private Unbinder mUnbinder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_show_joke, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        String joke = getActivity().getIntent().getStringExtra(EXTRA_JOKE);
        jokeTextView.setText(joke);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
