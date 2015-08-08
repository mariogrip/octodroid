package com.mariogrip.octodroid.iu;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mariogrip.octodroid.R;
import com.mariogrip.octodroid.memory;
import com.mariogrip.octodroid.util;
import com.mariogrip.octodroid.util_send;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by mariogrip on 08.02.15.
 */
public class custom_card extends Fragment {
    public custom_card(){}

    private static final String TAG = "CardListActivity";
    private ListView listView;
    private View rootView;
    private Integer setExt = 0;
    private Integer setBed = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.card_main, container, false);

        Button newb = (Button) rootView.findViewById(R.id.button_add_new_c);
        newb.setVisibility(View.VISIBLE);
        newb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                builder.setTitle("Add new Command");
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                final View PView = inflater.inflate(R.layout.costum_new, null);
                builder.setView(PView);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
                        EditText name = (EditText) PView.findViewById(R.id.editText_new_name);
                        EditText cmd = (EditText) PView.findViewById(R.id.editText_new_cmd);
                        util.SaveCommands(name.getText().toString(), cmd.getText().toString(), prefs);
                        new initFiles().execute();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

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
            plwait = ProgressDialog.show(rootView.getContext(), "Please wait....", "Getting Commands.....", true);
            plwait.setCancelable(true);
        }

        protected ArrayList doInBackground(Void... Voids) {
            ArrayList<Card> cards = new ArrayList<Card>();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
            try {
                for (String keey : util.getSavedCommandsIDs(prefs)) {
                    cardtest card = new cardtest(rootView.getContext(), util.getCommandInfo(keey,prefs));
                    cards.add(card);
                }
            } catch (Exception e) {
                String[] keey = {"Cannot find any Commands", "Error", "Error"};
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
            plwait.dismiss();
        }
    }

    public class cardtest extends Card {

        protected String mName;
        protected String mCMD;
        protected String mID;

        public cardtest(Context context, String[] Data) {
            super(context, R.layout.card_custom);
            this.mName = Data[0];
            this.mCMD = Data[1];
            this.mID = Data[2];
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
            if (mID.contains("Error")){
                Button button2 = (Button) parent.findViewById(R.id.button_send_c);
                button2.setVisibility(View.GONE);
                Button button3 = (Button) parent.findViewById(R.id.button_delete_c);
                button3.setVisibility(View.GONE);
                Button button4 = (Button) parent.findViewById(R.id.button_edit_c);
                button4.setVisibility(View.GONE);
            }else {
                final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
                Button button2 = (Button) parent.findViewById(R.id.button_send_c);
                button2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        util_send.SendGcodeCMD(util.arrayToJsonArray(util.oneLinetoArray(mCMD)));
                        Toast.makeText(parent.getContext(), "Running: " + mName, Toast.LENGTH_SHORT).show();
                    }
                });
                Button button3 = (Button) parent.findViewById(R.id.button_delete_c);
                button3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        util.RemoveCommands(mID, prefs);
                        Toast.makeText(parent.getContext(), "Deleted: " + mName, Toast.LENGTH_SHORT).show();
                        new initFiles().execute();
                    }
                });
                Button button4 = (Button) parent.findViewById(R.id.button_edit_c);
                button4.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                        builder.setTitle("Edit Command");
                        final LayoutInflater inflater = getActivity().getLayoutInflater();
                        final View PView = inflater.inflate(R.layout.costum_new, null);
                        builder.setView(PView);
                        EditText name = (EditText) PView.findViewById(R.id.editText_new_name);
                        EditText cmd = (EditText) PView.findViewById(R.id.editText_new_cmd);
                        name.setText(mName);
                        cmd.setText(mCMD);
                        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                util.RemoveCommands(mID, prefs);
                                EditText name = (EditText) PView.findViewById(R.id.editText_new_name);
                                EditText cmd = (EditText) PView.findViewById(R.id.editText_new_cmd);
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
                                util.SaveCommands(name.getText().toString(), cmd.getText().toString(), prefs);
                                new initFiles().execute();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
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
