package com.mariogrip.octodroid.iu;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mariogrip.octodroid.R;
import com.mariogrip.octodroid.memory;
import com.mariogrip.octodroid.util;
import com.mariogrip.octodroid.util_decode;
import com.mariogrip.octodroid.util_get;
import com.mariogrip.octodroid.util_send;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by mariogrip on 08.12.14.
 */
public class file_card extends Fragment {
    public file_card() {}
    private static final String TAG = "CardListActivity";
    private ListView listView;
    private View rootView;
    private Integer setExt = 0;
    private Integer setBed = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.card_main, container, false);
        try {
            ProgressBar progresss = (ProgressBar) rootView.findViewById(R.id.progressBar);
            TextView texttimes = (TextView) rootView.findViewById(R.id.textView11_time);
            texttimes.setText(" " + util.toHumanRead(memory.job.progress.getPrintTimeLeft()));
            progresss.setProgress(util.getProgress());
            new initFiles().execute();
        }catch (Exception e) {

        }

        ImageButton up = (ImageButton) rootView.findViewById(R.id.button_stop);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                util.logD("CLICK STOP!!!");
                final ProgressDialog ringProgressDialog = ProgressDialog.show(rootView.getContext(), "Please wait ...", "Stopping print ...", true);
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

        ImageButton button = (ImageButton) rootView.findViewById(R.id.button_start);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(rootView.getContext(), "Starting printing...", Toast.LENGTH_LONG).show();
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


        ImageButton right = (ImageButton) rootView.findViewById(R.id.button_pause);
        right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ProgressDialog ringProgressDialog = ProgressDialog.show(rootView.getContext(), "Please wait ...", "Setting print on pause ...", true);
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

        return rootView;
    }

    private class initFiles extends AsyncTask<Void, Void, ArrayList> {

        private ProgressDialog plwait;
        protected void onPreExecute(){
              plwait = ProgressDialog.show(rootView.getContext(), "Please wait....", "Getting files.....", true);
              plwait.setCancelable(true);
        }

        protected ArrayList doInBackground(Void... Voids) {
            util_decode.decodeFiles();
            ArrayList<Card> cards = new ArrayList<Card>();
            try {
                for (String[] keey : util_get.getFiles()) {
                    cardtest card = new cardtest(rootView.getContext(), keey);
                    cards.add(card);
                }
            } catch (Exception e) {
                String[] keey = {"Cannot find any files", "Error"};
                cardtest card = new cardtest(rootView.getContext(), keey);
                cards.add(card);
            }

            return cards;
        }


        protected void onPostExecute(ArrayList cards){
            util.logD("YES DOING onPost");
            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(rootView.getContext(), cards);
            mCardArrayAdapter.setInnerViewTypeCount(3);

            CardListView listView = (CardListView) rootView.findViewById(R.id.card_main_card_list);
            if (listView != null) {
                listView.setAdapter(mCardArrayAdapter);

            }
            try{
                plwait.dismiss();
            }catch (Exception e){

            }

        }
    }

    public class cardtest extends Card {

        protected String mName;
        protected String mOrigin;

        public cardtest(Context context, String[] Data) {
            super(context, R.layout.card_files);
            this.mName = Data[0];
            this.mOrigin = Data[1];
            init();
        }

        private void init() {
            //  CardHeader header = new CardHeader(rootView.getContext());
            //header.setTitle(mTitleHeader);
            // addCardHeader(header);
            // setTitle(mTitleMain);
        }


        @Override
        public int getType() {
            return 0;
        }

        @Override
        public void setupInnerViewElements(final ViewGroup parent, View view) {
            TextView files = (TextView) parent.findViewById(R.id.file_text_list);
            files.setText(mName);
            ImageButton button2 = (ImageButton) parent.findViewById(R.id.button_load);
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    util_send.loadFile(mName, mOrigin);
                    Toast.makeText(parent.getContext(), "Loaded: " + mName, Toast.LENGTH_SHORT).show();
                }
            });
            ImageButton button3 = (ImageButton) parent.findViewById(R.id.button_print);
            button3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    util_send.printFile(mName, mOrigin);
                    Toast.makeText(parent.getContext(), "Starting to print: " + mName, Toast.LENGTH_SHORT).show();
                }
            });
            ImageButton button4 = (ImageButton) parent.findViewById(R.id.button_delete);
            button4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    util_send.deleteFileInList(mName, mOrigin);
                    Toast.makeText(parent.getContext(), "Deleted: " + mName, Toast.LENGTH_SHORT).show();
                }
            });



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
