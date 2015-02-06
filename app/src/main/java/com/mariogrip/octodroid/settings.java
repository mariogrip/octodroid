package com.mariogrip.octodroid;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by mariogrip on 26.10.14.
 *
 * GNU Affero General Public License http://www.gnu.org/licenses/agpl.html
 *
 * This is the Settings file
 */
public class settings extends PreferenceActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
    }
}
