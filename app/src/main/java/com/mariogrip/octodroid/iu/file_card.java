package com.mariogrip.octodroid.iu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mariogrip.octodroid.R;

/**
 * Created by mariogrip on 08.12.14.
 */
public class file_card extends Fragment {
    public file_card(){}
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.card_files, container, false);


        return rootView;
    }
}
