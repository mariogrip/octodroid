package com.mariogrip.octodroid.iu;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mariogrip.octodroid.Activity;
import com.mariogrip.octodroid.R;
import com.mariogrip.octodroid.util;

/**
 * Created by mariogrip on 31.10.14.
 */
public class controls extends Fragment {

    public controls(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.controls, container, false);

        Button up = (Button) rootView.findViewById(R.id.buttonUp);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OctoDroid","button up Pressed");
                util.sendcmd(Activity.ip, "printer/printhead", "jog", "10", Activity.key);
            }
        });

       final Button button = (Button) rootView.findViewById(R.id.button_down);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("OctoDroid","button Down Pressed");
            }
        });


        Button right = (Button) rootView.findViewById(R.id.button_right);
        right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("OctoDroid","button right Pressed");
            }
        });

        Button left = (Button) rootView.findViewById(R.id.button_left);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OctoDroid","button left Pressed");
            }
        });

        Button home = (Button) rootView.findViewById(R.id.button_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OctoDroid","button home Pressed");
            }
        });

        return rootView;
    }

}
