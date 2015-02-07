package com.mariogrip.octodroid.iu;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.mariogrip.octodroid.R;
import com.mariogrip.octodroid.mainActivity;
import com.mariogrip.octodroid.mainActivity_BETA;
import com.mariogrip.octodroid.memory;
import com.mariogrip.octodroid.util;
import com.mariogrip.octodroid.util_get;
import com.mariogrip.octodroid.util_send;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;
import it.gmariotti.cardslib.library.view.CardListView;


/**
 * Created by mariogrip on 28.11.14.
 */
public class main_card_BETA extends Fragment {

    public main_card_BETA(){}
    private static final String TAG = "CardListActivity";
    private ListView listView;
    private View rootView;
    private Timer timer1 = new Timer();
    private TimerTask timerTask1;
    public static ViewGroup test123;
    private Dictionary<Integer, Integer> listViewItemHeights = new Hashtable<Integer, Integer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.card_main_beta, container, false);
        try {
            ProgressBar progresss = (ProgressBar) rootView.findViewById(R.id.progressBar);
            TextView texttimes = (TextView) rootView.findViewById(R.id.textView11_time);
            texttimes.setText(" " + util.toHumanRead(memory.job.progress.getPrintTimeLeft()));
            progresss.setProgress(util.getProgress());

        }catch (Exception e){

        }
        ArrayList<Card> cards = new ArrayList<Card>();
        card_progressbar cardprpg = new card_progressbar(rootView.getContext());
        cards.add(cardprpg);
        cardtest card = new cardtest(rootView.getContext(),"State", "State");
        cards.add(card);
        cardteststart cardcont = new cardteststart(rootView.getContext(),"Start/Stop", "Startstop");
        cards.add(cardcont);

        cardtest2 card2 = new cardtest2(rootView.getContext(),"Connections", "Connections");

        Card cardss = new Card(getActivity());
        CardHeader header = new CardHeader(getActivity());
        header.setTitle("Connections");
        header.setButtonExpandVisible(true);
        cardss.addCardHeader(header);
        CardExpand expand = new CustomExpandCard(getActivity());
        expand.setTitle("Connections");

        cardss.addCardExpand(expand);

        cards.add(cardss);

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(rootView.getContext(),cards);
        mCardArrayAdapter.setInnerViewTypeCount(3);

        final CardListView listView = (CardListView) rootView.findViewById(R.id.card_main_card_list);
        final RelativeLayout alphabackgroudn = (RelativeLayout) rootView.findViewById(R.id.beta_alpha_backgroud);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                Log.d("OctoTest", "Doing this");
                try {
                    if (listView != null) {
                        View c = listView.getChildAt(0); //this is the first visible row

                            int scrollY = -c.getTop();
                            listViewItemHeights.put(listView.getFirstVisiblePosition(), c.getHeight());
                            for (int i11 = 0; i < listView.getFirstVisiblePosition(); ++i) {
                                if (listViewItemHeights.get(i11) != null) // (this is a sanity check)
                                    scrollY += listViewItemHeights.get(i11); //add all heights of the views that are gone
                            }

                            listView.setAlpha(1);
                           alphabackgroudn.setAlpha((float) (scrollY + 1200) / 1000);

                           Log.d("OctoTest", "Math=" + (float) (scrollY + 1200) / 1000 + " POS=" + scrollY);

                    }
                }catch (Exception e){
                    Log.d("OctoTest", "No This should not happened!");
                }
            }
        });
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }
        ViewToClickToExpand viewToClickToExpand =
                ViewToClickToExpand.builder()
                        .highlightView(false)
                        .setupCardElement(ViewToClickToExpand.CardElementUI.CARD);
        cardss.setViewToClickToExpand(viewToClickToExpand);
      //  VideoView vv = (VideoView) rootView.findViewById(R.id.videoView);
        ImageView vv = (ImageView) rootView.findViewById(R.id.ImageView);

        if (vv != null) {
            timerTask1 = new TimerTask() {
                @Override
                public void run() {
                    new DownloadImageTask((ImageView) rootView.findViewById(R.id.ImageView)).execute("http://" + mainActivity.ip + "/webcam/?action=snapshot");
                }
            };
            timer1.schedule(timerTask1, 0, 10000);


            //vv.setVideoURI(Uri.parse("http://" + mainActivity.ip +"/webcam/?action=snapshot"));
            //vv.requestFocus();
            //vv.start();
        }else{
            Log.d("BETA", "Cannot find the stream video");
            Toast.makeText(rootView.getContext(), "Cannot find the stream video. if you know that the camera works at the web and octodroid is displaying information about you printer, please report this", Toast.LENGTH_LONG);
        }
        return rootView;

            }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    public class cardtest extends Card{

        protected String mTitleHeader;
        protected String mTitleMain;

        public cardtest(Context context,String titleHeader,String titleMain) {
            super(context, R.layout.card_status);
            this.mTitleHeader=titleHeader;
            this.mTitleMain=titleMain;
            init();
        }

        private void init(){

            //Create a CardHeader
            CardHeader header = new CardHeader(rootView.getContext());
            header.setTitle(mTitleHeader);
            addCardHeader(header);
            setTitle(mTitleMain);
            setCardElevation(10);

        }


        @Override
        public int getType() {
            //Very important with different inner layouts
            return 0;
        }
        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
            try {
                TextView textpri = (TextView) parent.findViewById(R.id.textView16_printed);
                TextView textest = (TextView) parent.findViewById(R.id.textView13_est);
                TextView textfile = (TextView) parent.findViewById(R.id.textView11_file);
                TextView textmaci = (TextView) parent.findViewById(R.id.textView10_maci);
                TextView texttarT = (TextView) parent.findViewById(R.id.textView18_tar_t);
                TextView textcurT = (TextView) parent.findViewById(R.id.textView18_cur_T);
                TextView textBcur = (TextView) parent.findViewById(R.id.textView18_Bcur_T);
                TextView textBtar = (TextView) parent.findViewById(R.id.textView18_Btar_T);
                TextView textprinttime = (TextView) parent.findViewById(R.id.textView17_print_time);
                // TextView texthei = (TextView) findViewById(R.id.textView15_hei);
                //TextView textfila = (TextView) findViewById(R.id.textView12_fila);
                //TextView texttimel = (TextView) findViewById(R.id.textView14_timel);

                textfile.setText(" " + memory.job.file.getName());
                textpri.setText(" " + util.toMBGB(memory.job.progress.getFilepos()).toString() + "/" + util.toMBGB(memory.job.file.getSize()).toString());
                textest.setText(" " + util.toHumanRead(memory.job.getEstimatedPrintTime()));
                textmaci.setText(" " + memory.connection.current.getState());
                texttarT.setText(" " + memory.temp.target.getExt()[0] + "°C");
                textcurT.setText(" " + memory.temp.current.getExt()[0] + "°C");
                textBcur.setText(" " + memory.temp.current.getBed()[0] + "°C");
                textBtar.setText(" " + memory.temp.target.getBed()[0] + "°C");
                textprinttime.setText(" " + util.toHumanRead(memory.job.progress.getPrintTime()));
                // progress.setProgress(util.getProgress());
                // textfila.setText(" " + memory.Filament);
                // texttimel.setText(" " + memory.Timelapse);
                // texthei.setText(" " + memory.Height);
            }catch (Exception e){

            }
        }
    }

    public class card_progressbar extends Card{

        public card_progressbar(Context context) {
            super(context, R.layout.card_progressbar);
            init();
        }

        private void init(){

        }


        @Override
        public int getType() {
            //Very important with different inner layouts
            return 0;
        }
        @Override
        public void setupInnerViewElements(final ViewGroup parent, View view) {

        }
    }

    public class cardteststart extends Card{

        protected String mTitleHeader;
        protected String mTitleMain;

        public cardteststart(Context context,String titleHeader,String titleMain) {
            super(context, R.layout.card_startstop);
            this.mTitleHeader=titleHeader;
            this.mTitleMain=titleMain;
            init();
        }

        private void init(){

            //Create a CardHeader
            CardHeader header = new CardHeader(rootView.getContext());
            header.setTitle(mTitleHeader);
            addCardHeader(header);
            setTitle(mTitleMain);
        }


        @Override
        public int getType() {
            //Very important with different inner layouts
            return 0;
        }
        @Override
        public void setupInnerViewElements(final ViewGroup parent, View view) {
            ImageButton up = (ImageButton) parent.findViewById(R.id.button_stop);
            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog ringProgressDialog = ProgressDialog.show(parent.getContext(), "Please wait ...", "Stopping print ...", true);
                    ringProgressDialog.setCancelable(true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                util_send.stopprint();
                            } catch (Exception e) {

                            }
                            ringProgressDialog.dismiss();
                        }
                    }).start();
                }
            });

            ImageButton button = (ImageButton) parent.findViewById(R.id.button_start);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(parent.getContext(),"Starting printing...", Toast.LENGTH_LONG).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                            util_send.startprint();
                        } catch (Exception e){

                        }
                        }
                    }).start();

                }
            });


            ImageButton right = (ImageButton) parent.findViewById(R.id.button_pause);
            right.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final ProgressDialog ringProgressDialog = ProgressDialog.show(parent.getContext(), "Please wait ...", "Setting print on pause ...", true);
                    ringProgressDialog.setCancelable(true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                util_send.pauseprint();
                            } catch (Exception e){

                            }
                            ringProgressDialog.dismiss();
                        }

                    }).start();

                }
            });

        }
    }

    public class cardtest2 extends Card{

        protected String mTitleHeader;
        protected String mTitleMain;

        public cardtest2(Context context,String titleHeader,String titleMain) {
            super(context, R.layout.card_connect);
            this.mTitleHeader=titleHeader;
            this.mTitleMain=titleMain;
            init();
        }

        private void init(){

            //Create a CardHeader
            CardHeader header = new CardHeader(rootView.getContext());
            header.setTitle(mTitleHeader);
            addCardHeader(header);
            setTitle(mTitleMain);
        }


        @Override
        public int getType() {
            //Very important with different inner layouts
            return 1;
        }
        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
            ArrayList<String> spinnerArray = new ArrayList<String>();
            Log.d("OctoDroid", "doing the thing");
            util_get.decodeConnections();
            String[] dev = util.jsonArraytoStringArray(util_get.getSerialPort());
            for (String i : dev){
                spinnerArray.add(i);
            }
            ArrayList<String> spinnerArray2 = new ArrayList<String>();
            String[] dev2 = util.jsonArraytoStringArray(util_get.getBaudrates());
            for (String i2 : dev2){
                spinnerArray2.add(i2);
            }
            LinearLayout layout = new LinearLayout(parent.getContext());
            Spinner spinner = (Spinner) parent.findViewById(R.id.spinner);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(parent.getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
            spinner.setAdapter(spinnerArrayAdapter);

            Spinner spinner2 = (Spinner) parent.findViewById(R.id.spinner2);
            ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(parent.getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray2);
            spinner2.setAdapter(spinnerArrayAdapter2);
        }
    }

    public class CustomExpandCard extends CardExpand {

        public CustomExpandCard(Context context) {
            super(context, R.layout.card_connect);
        }

        @Override
        public void setupInnerViewElements(final ViewGroup parent, View view) {
            parent.setBackgroundColor(mContext.getResources().
                    getColor(R.color.card_background));

            if (view == null) return;
            ArrayList<String> spinnerArray = new ArrayList<String>();
            Log.d("OctoDroid", "doing the thing");
            if (util_get.isConnected()) {
                util_get.decodeConnections();
                spinnerArray.add(util_get.getCurrentSerialPort().replace("\\/", "/"));

                ArrayList<String> spinnerArray2 = new ArrayList<String>();
                spinnerArray2.add(util_get.getCurrentBaudrates());

                LinearLayout layout = new LinearLayout(parent.getContext());
                final Spinner spinner = (Spinner) parent.findViewById(R.id.spinner);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(parent.getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                spinner.setAdapter(spinnerArrayAdapter);
                spinner.setEnabled(false);

                final Spinner spinner2 = (Spinner) parent.findViewById(R.id.spinner2);
                ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(parent.getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray2);
                spinner2.setAdapter(spinnerArrayAdapter2);
                spinner2.setEnabled(false);

                final Button right = (Button) parent.findViewById(R.id.buttonConDis);
                right.setText("Disconnect");
                right.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        final ProgressDialog ringProgressDialog = ProgressDialog.show(parent.getContext(), "Please wait ...", "Disconnecting for printer ...", true);
                        ringProgressDialog.setCancelable(true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    util_send.Disconnect();
                                    Thread.sleep(5000);
                                } catch (Exception e){

                                }
                                ringProgressDialog.dismiss();

                            }

                        }).start();

                    }
                });

            }else{
                util_get.decodeConnections();
                String[] dev = util.jsonArraytoStringArray(util_get.getSerialPort());
                for (String i : dev){
                    spinnerArray.add(i.replace("\\/", "/"));
                }
                for (String i : dev){
                    Log.d("OctoDroid", i);
                }
                ArrayList<String> spinnerArray2 = new ArrayList<String>();
                String[] dev2 = util.jsonArraytoStringArray(util_get.getBaudrates());
                for (String i2 : dev2){
                    spinnerArray2.add(i2);
                }
                LinearLayout layout = new LinearLayout(parent.getContext());
                final Spinner spinner = (Spinner) parent.findViewById(R.id.spinner);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(parent.getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                spinner.setAdapter(spinnerArrayAdapter);
                spinner.setEnabled(true);

                final Spinner spinner2 = (Spinner) parent.findViewById(R.id.spinner2);
                ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(parent.getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray2);
                spinner2.setAdapter(spinnerArrayAdapter2);
                spinner2.setEnabled(true);

                final Button right = (Button) parent.findViewById(R.id.buttonConDis);
                right.setText("Connect");
                right.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        final ProgressDialog ringProgressDialog = ProgressDialog.show(parent.getContext(), "Please wait ...", "Connecting for printer ...", true);
                        ringProgressDialog.setCancelable(true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    util_send.Connect("true", "true", spinner.getSelectedItem().toString(), spinner2.getSelectedItem().toString());
                                    Thread.sleep(5000);
                                } catch (Exception e){

                                }
                                ringProgressDialog.dismiss();
                            }

                        }).start();

                    }
                });
            }

        }


    }


    public class MyCardArrayAdapter extends CardArrayAdapter{

        /**
         * Constructor
         *
         * @param context The current context.
         * @param cards   The cards to represent in the ListView.
         */
        public MyCardArrayAdapter(Context context, List<Card> cards) {
            super(context, cards);
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }
    }




}
