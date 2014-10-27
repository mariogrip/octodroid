package com.mariogrip.octoprint;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by mariogrip on 26.10.14.
 */
public class settings extends PreferenceActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    addPreferencesFromResource(R.xml.setting);
}

}
