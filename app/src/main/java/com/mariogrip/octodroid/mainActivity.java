package com.mariogrip.octodroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.mariogrip.octodroid.iu.con_card;
import com.mariogrip.octodroid.iu.cont_card;
import com.mariogrip.octodroid.iu.custom_card;
import com.mariogrip.octodroid.iu.file_card;
import com.mariogrip.octodroid.iu.main_card;
import com.mariogrip.octodroid.iu.main_card_BETA;
import com.mariogrip.octodroid.iu.temp_card;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mariogrip on 27.10.14.
 *
 * GNU Affero General Public License http://www.gnu.org/licenses/agpl.html
 *
 * MainActiviti The main class (init class) This is the class that is called when the app start
 */
public class mainActivity extends Activity {
    private boolean AsyncTaskRunning = false;
    protected int pos;
    private String[] nawTitle;
    private DrawerLayout nawlay;
    private ListView nawList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    public static String jsonData_job;
    public static String jsonData_connetion;
    public static String jsonData_printer;
    protected SharedPreferences prefs;
    private util get_class;
    private boolean senderr = false;
    protected static boolean running = false;
    protected static boolean printing;
    protected static boolean push = true;
    protected static boolean servicerunning = false;
    public static String ip;
    public boolean firstTimeRunner = false;
    public boolean lostConnection = false;
    public static String key;
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Timer timer2 = new Timer();
    private TimerTask timerTask2;
    private boolean betamode = false;
    private static final int RESULT_SETTINGS = 1;

    private ProgressDialog plwaitStart;
    private ProgressDialog plwaitRecon;
    private boolean plwateStartR = false;
    private boolean plwateReconR = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO Remove StrictMode and add AsyncTask!
        super.onCreate(savedInstanceState);
       StrictMode.ThreadPolicy policy = new StrictMode.
       ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity.this);
        getConnectionConfigurationFromPrefs();

        senderr = prefs.getBoolean("err", true);
        boolean first = false;
        if (!memory.skipWelcom){
        Intent i = new Intent(mainActivity.this, welcome.class);
        if(!util.doGeneralCheckApi()){
                first = true;
                startActivity(i);
        }
        if (!first) {
            if(!util.doGeneralCheckIp()) {
                    startActivity(i);
            }
        }
        }
        betamode = prefs.getBoolean("beta", false);

        setContentView(R.layout.nawdraw);
        mTitle = mDrawerTitle = getTitle();
        nawTitle = getResources().getStringArray(R.array.nawbars);
        nawlay = (DrawerLayout) findViewById(R.id.drawer_layout);
        nawList = (ListView) findViewById(R.id.left_drawer);
        nawList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.nawlist, nawTitle));
        nawList.setOnItemClickListener(new DrawerItemClickListener());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                nawlay,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        nawlay.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            selectItem(0);
        }
        push = prefs.getBoolean("push", true);
        running = false;
        plwaitStart = new ProgressDialog(this);
        plwaitStart.setTitle("Please wait....");
        plwaitStart.setMessage("Connection to "+memory.ip+".....");
        plwaitStart.setButton(DialogInterface.BUTTON_NEGATIVE,"Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                plwaitStart.dismiss();
            }
        });
        plwaitStart.setCancelable(false);
        plwaitStart.show();
        plwateStartR = true;
        util.logD("Done!");
    }
    public void startrunner(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        TextView textmaci = (TextView) findViewById(R.id.textView10_maci);
                     //   textmaci.setText("Offline");
                    }
                 });
               runner();
                util.logD("Done startrunner()");
            }
        };
        new Thread(runnable).start();
    }
    public void reConnM(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                plwaitRecon = new ProgressDialog(mainActivity.this);
                plwaitRecon.setTitle("Lost Connection....");
                plwaitRecon.setMessage("Reconnecting to "+memory.ip+".....");
                plwaitRecon.setButton(DialogInterface.BUTTON_NEGATIVE,"Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        plwaitRecon.dismiss();
                    }
                });
                plwaitRecon.setCancelable(false);
                plwaitRecon.show();
            }
        });
        util.logD("ReConnM");
        plwateReconR = true;
    }
    public void startservice(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                util.logD("Starting Service");
                if (push) {
                    if (!servicerunning) {
                        if(service.isIRunning) {
                            util.logD("Service was starded twice, stopping it!");
                        }else{
                            servicerunning = true;
                            Intent mServiceIntent = new Intent(mainActivity.this, service.class);
                            mainActivity.this.startService(mServiceIntent);
                        }
                    }
                }else{
                    if (servicerunning){
                        util.logD("starting runner");
                        servicerunning = false;
                        try {
                            Intent mServiceIntent = new Intent(mainActivity.this, service.class);
                            mainActivity.this.stopService(mServiceIntent);
                        }catch (Exception e){

                        }
                    }
                }
                util.logD("Done startservice()");
            }
        };
        new Thread(runnable).start();
    }



    public void runner(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int sync = 3000;
        try{
            sync = Integer.parseInt(prefs.getString("sync", "2"));
            if (sync < 999){
                sync = 1000;
            }
            if (sync > 7001){
                sync = 7000;
            }
        }catch (Exception e){
            sync = 3000;
        }
        try {



            if (running) {
                util.logD("Stopping runner, Might started twice");
                return;
            }
            if (!running) {
                util.logD("OneRunStarted");
                running = true;
            }
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    decodejobs();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                Fill();
                            }catch (Exception e){

                            }
                        }
                    });
                }

            };

            timer.schedule(timerTask, 0, sync);
        }catch (NullPointerException v){
            v.printStackTrace();
        }
    }

    private class updateTextview extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            if(!AsyncTaskRunning) {
                AsyncTaskRunning = true;
                decodejobs();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(AsyncTaskRunning) {
                Fill();
                AsyncTaskRunning = false;
            }
        }
    }

    private void decodejobs(){

        util_decode.decodeJob();
        if(memory.isServerUp()){
            if(plwateStartR){
                plwateStartR = false;
                plwaitStart.dismiss();
            }else{
                if(plwateReconR){
                    plwateReconR = false;
                   plwaitRecon.dismiss();
                }
            }
            util_decode.decodeConnections();
            util_decode.decodePrinter();
        }else{
            util.logD("IN ISSERVERUP" + plwateStartR + plwateReconR);
            if(!plwateReconR && !plwateStartR){
                util.logD("IN IF RECONNM");
                reConnM();
            }
        }
            util.logD("DecodeJobs Ping true");
    }

    private void Fill(){
        switch (pos) {
            case 0:
                try{
                    util.logD("Running runner");
                    TextView oflline = (TextView) findViewById(R.id.textView_offline);
                    if (memory.isServerUp()) {
                        oflline.setText("");
                        try {
                            if (util_get.isConnected()) {
                                final Spinner spinner = (Spinner) findViewById(R.id.spinner);
                                spinner.setEnabled(false);
                                final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
                                spinner2.setEnabled(false);
                                final Button right = (Button) findViewById(R.id.buttonConDis);
                                right.setText("Disconnect");
                                right.setEnabled(true);
                            } else {
                                final Spinner spinner = (Spinner) findViewById(R.id.spinner);
                                spinner.setEnabled(true);
                                final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
                                spinner2.setEnabled(true);
                                final Button right = (Button) findViewById(R.id.buttonConDis);
                                right.setText("Connect");
                                right.setEnabled(true);
                            }
                        }catch (Exception e){

                        }
                        ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
                        TextView texttime = (TextView) findViewById(R.id.textView11_time);
                        TextView textpri = (TextView) findViewById(R.id.textView16_printed);
                        TextView textest = (TextView) findViewById(R.id.textView13_est);
                        TextView textfile = (TextView) findViewById(R.id.textView11_file);
                        TextView textmaci = (TextView) findViewById(R.id.textView10_maci);
                        TextView texttarT = (TextView) findViewById(R.id.textView18_tar_t);
                        TextView textcurT = (TextView) findViewById(R.id.textView18_cur_T);
                        TextView textBcur = (TextView) findViewById(R.id.textView18_Bcur_T);
                        TextView textBtar = (TextView) findViewById(R.id.textView18_Btar_T);
                        TextView textprinttime = (TextView) findViewById(R.id.textView17_print_time);
                        // TextView texthei = (TextView) findViewById(R.id.textView15_hei);
                        //TextView textfila = (TextView) findViewById(R.id.textView12_fila);
                        //TextView texttimel = (TextView) findViewById(R.id.textView14_timel);

                        textfile.setText(" " + memory.job.file.name);
                        textpri.setText(" " + util.toMBGB(memory.job.progress.filepos).toString() + "/" + util.toMBGB(memory.job.file.size).toString());
                        texttime.setText(" " + util.toHumanRead(memory.job.progress.PrintTimeLeft));
                        textest.setText(" " + util.toHumanRead(memory.job.estimatedPrintTime));
                        textmaci.setText(" " + memory.connection.current.state);
                        texttarT.setText(" " + memory.temp.target.Ext[0] + "°C");
                        textcurT.setText(" " + memory.temp.current.Ext[0] + "°C");
                        textBcur.setText(" " + memory.temp.current.Bed[0] + "°C");
                        textBtar.setText(" " + memory.temp.target.Bed[0] + "°C");
                        textprinttime.setText(" " + util.toHumanRead(memory.job.progress.printTime));
                        progress.setProgress(util.getProgress());
                        // textfila.setText(" " + memory.Filament);
                        // texttimel.setText(" " + memory.Timelapse);
                        // texthei.setText(" " + memory.Height);
                    } else {
                        TextView textmaci = (TextView) findViewById(R.id.textView10_maci);
                        textmaci.setText("Cannot connect to\n" + ip);
                        oflline.setText("Offline");
                    }
                }catch (Exception v){v.printStackTrace();}
                break;
            case 2:
                TextView oflline = (TextView) findViewById(R.id.textView_offline);
                if (memory.isServerUp()) {

                    try {
                        oflline.setText("");
                        ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
                        TextView texttime = (TextView) findViewById(R.id.textView11_time);
                        texttime.setText(" " + util.toHumanRead(memory.job.progress.PrintTimeLeft));
                        progress.setProgress(util.getProgress());
                    }catch(Exception v){
                    }
                }else{
                    oflline.setText("Offline");
                }
                break;
            case 1:
                TextView oflline2 = (TextView) findViewById(R.id.textView_offline);
                if (memory.isServerUp()) {
                    try {
                        oflline2.setText("");
                        ProgressBar progresss = (ProgressBar) findViewById(R.id.progressBar);
                        TextView texttimes = (TextView) findViewById(R.id.textView11_time);
                        texttimes.setText(" " + util.toHumanRead(memory.job.progress.getPrintTimeLeft()));
                        progresss.setProgress(util.getProgress());
                        TextView textbed = (TextView) findViewById(R.id.textView_CurentTemp_bed_Tempcard);
                        TextView textext = (TextView) findViewById(R.id.textView_CurentTemp_ext_TempCard);
                        textbed.setText(memory.temp.current.getBed()[0] + "°C");
                        textext.setText(memory.temp.current.getExt()[0] + "°C");

                    } catch (Exception v) {
                    }
                }else{
                    oflline2.setText("Offline");
                }
                break;
            default:
                break;

        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = nawlay.isDrawerOpen(nawList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, settings.class);
            startActivityForResult(i, RESULT_SETTINGS);
            return true;
        }
        if (id == R.id.setup) {
            Intent i = new Intent(this, welcome.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_bug) {
            Intent i1 = new Intent(this, sendbug.class);
            startActivity(i1);
            return true;
        }
        if (id == R.id.action_git){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mariogrip/octodroid"));
            startActivity(browserIntent);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity.this);
                getConnectionConfigurationFromPrefs();
                push = prefs.getBoolean("push", true);
                if (push) {
                    if (!servicerunning) {
                        servicerunning = true;
                        Intent mServiceIntent = new Intent(this, service.class);
                        this.startService(mServiceIntent);
                    }
                }else{
                    if (servicerunning){
                        servicerunning = false;
                        Intent mServiceIntent = new Intent(this, service.class);
                        this.stopService(mServiceIntent);
                    }
                }
                break;

        }

    }
    public void onPause(){
        super.onPause();
        if (running) {
            running = false;
            try{
                timerTask.cancel();
            }catch (Exception e){

            }
        }
    }
    public void onResume(){
        super.onResume();
        getConnectionConfigurationFromPrefs();
        senderr = prefs.getBoolean("err", true);
        push = prefs.getBoolean("push", true);
        betamode = prefs.getBoolean("beta", false);
        servicerunning = false;
        startservice();
        startrunner();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("I cannot seem to find any valid IP address");
        builder.setPositiveButton("Setup", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(mainActivity.this, welcome.class);
                startActivity(i);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("Error");
        builder2.setMessage("I cannot seem to find any valid API Key");
        builder2.setPositiveButton("Setup", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(mainActivity.this, welcome.class);
                startActivity(i);
                dialog.dismiss();
            }
        });
        builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        AlertDialog dialog2 = builder2.create();
        boolean first = false;
        if(!util.doGeneralCheckApi()){
                first = true;
                dialog2.show();
        }
        if (!first) {
            if(!util.doGeneralCheckIp()) {
                    dialog.show();
            }
        }
    }
    public void onStop(){
        super.onStop();
        if (running) {
            running = false;
            try{
                timerTask.cancel();
            }catch (Exception e){

            }

        }
    }
    public void onStart(){
        super.onStart();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        pos = position;
        Fragment fragment = new main_card();
        if (betamode) {
            fragment = new main_card_BETA();
            switch (position) {
                case 0:
                    fragment = new main_card_BETA();
                    break;
                case 6:
                    fragment = new main_card_BETA();
                    pos = 0;
                    position = 0;
                    Intent i = new Intent(this, settings.class);
                    startActivityForResult(i, RESULT_SETTINGS);
                    break;
                case 1:
                    fragment = new temp_card();
                    break;
                case 2:
                    fragment = new cont_card();
                    break;
                case 3:
                    fragment = new file_card();
                    break;
                case 4:
                    fragment = new con_card();
                    break;
                case 5:
                    fragment = new custom_card();
                    break;
                default:
                    break;
            }
        }else{
            fragment = new main_card();
            switch (position) {
                case 0:
                    fragment = new main_card();
                    break;
                case 6:
                    fragment = new main_card();
                    pos = 0;
                    position = 0;
                    Intent i = new Intent(this, settings.class);
                    startActivityForResult(i, RESULT_SETTINGS);
                    break;
                case 1:
                    fragment = new temp_card();
                    break;
                case 2:
                    fragment = new cont_card();
                    break;
                case 3:
                    fragment = new file_card();
                    break;
                case 4:
                    fragment = new con_card();
                    break;
                case 5:
                    fragment = new custom_card();
                    break;
                default:
                    break;
            }
        }
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


        // update selected item and title, then close the drawer
        nawList.setItemChecked(position, true);
        setTitle(nawTitle[position]);
        nawlay.closeDrawer(nawList);


    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void getConnectionConfigurationFromPrefs() {
        key = prefs.getString("api", "none");
        ip = prefs.getString("ip", "none");
        memory.user.setApi(key);
        memory.user.setIp(ip);
        memory.user.setUseBasicAuth(prefs.getBoolean("enable_basic_auth", false));
        memory.user.setUserName(prefs.getString("auth_username", ""));
        memory.user.setPassword(prefs.getString("auth_password",""));

    }

    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";
        public PlanetFragment(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView;
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String naws = getResources().getStringArray(R.array.nawbars)[i];
            rootView = inflater.inflate(R.layout.status_tab, container, false);
            getActivity().setTitle(naws);

            return rootView;
        }
    }

}
