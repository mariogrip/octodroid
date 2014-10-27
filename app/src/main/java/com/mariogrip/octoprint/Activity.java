package com.mariogrip.octoprint;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class Activity extends ActionBarActivity {
    protected SharedPreferences prefs;
    private get get_class;
    protected String ip;
    private Timer timer = new Timer();
    private TimerTask timerTask;
    public static boolean server_status = false;
    private static final int RESULT_SETTINGS = 1;
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Just for testing, allow network access in the main thread
        //NEVER use this is productive code
        StrictMode.ThreadPolicy policy = new StrictMode.
        ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_tab);
        prefs = PreferenceManager.getDefaultSharedPreferences(Activity.this);
        ip = prefs.getString("ip", "localhost");
        get_class = new get();
        runner();

    }

    public void runner(){
        timerTask = new TimerTask() {
            @Override
            public void run() {

                Activity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        TextView text = (TextView)findViewById(R.id.textView11_time);
                        text.setText(get.getData("job","printTime").toString());
                    }
                });
                }

        };
        timer.schedule(timerTask, 0, 10000);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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
            Intent i = new Intent(this, settings.class);
            startActivityForResult(i, RESULT_SETTINGS);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:

                break;

        }

    }
}
