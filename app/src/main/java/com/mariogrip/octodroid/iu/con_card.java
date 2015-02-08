package com.mariogrip.octodroid.iu;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;
import it.gmariotti.cardslib.library.view.CardListView;


/**
 * Created by mariogrip on 28.11.14.
 */
public class con_card extends Fragment {

    public con_card(){}
    private static final String TAG = "CardListActivity";
    private ListView listView;
    private View rootView;
    public static ViewGroup test123;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.card_main, container, false);

        try {
            ProgressBar progresss = (ProgressBar) rootView.findViewById(R.id.progressBar);
            TextView texttimes = (TextView) rootView.findViewById(R.id.textView11_time);
            texttimes.setText(" " + util.toHumanRead(memory.job.progress.getPrintTimeLeft()));
            progresss.setProgress(util.getProgress());

        }catch (Exception e){

        }

        ArrayList<Card> cards = new ArrayList<Card>();

        cardtest2 card2 = new cardtest2(rootView.getContext(),"Connections", "Connections");


        cards.add(card2);

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(rootView.getContext(),cards);
        mCardArrayAdapter.setInnerViewTypeCount(3);

        CardListView listView = (CardListView) rootView.findViewById(R.id.card_main_card_list);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }
        return rootView;

            }



    public class cardtest2 extends Card{

        protected String mTitleHeader;
        protected String mTitleMain;
        protected ViewGroup parrent1;

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
            parrent1 = parent;
            if (view == null) return;

            Log.d("OctoDroid", "doing the thing");
            if (util_get.isConnected()) {
                new createDisconnect().execute();
            } else {
                new createConnect().execute();
            }


        }
    }

    public class createDisconnect extends AsyncTask<Void, Void, Boolean>{
        private ProgressDialog plwait;
        private String CurrentB;
        private String CurrentS;
        protected void onPreExecute(){
            plwait = ProgressDialog.show(rootView.getContext(), "Please wait....", "Getting status.....", true);
            plwait.setCancelable(true);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String[] returnThis = {""};
            try{
            util_decode.decodeConnections();
            CurrentS = util_get.getCurrentSerialPort().replace("\\/", "/");
            CurrentB = util_get.getCurrentBaudrates();
            }catch (Exception e){
                   return false;
            }
            return  true;
        }

        @Override
        protected void onPostExecute(Boolean strings) {
            plwait.dismiss();
            if (!strings){
                final Button right = (Button) rootView.findViewById(R.id.buttonConDis);
                right.setText("Connect");
                right.setEnabled(false);
                return;
            }
            ArrayList<String> spinnerArray = new ArrayList<String>();
            spinnerArray.add(CurrentS);

            ArrayList<String> spinnerArray2 = new ArrayList<String>();
            spinnerArray2.add(CurrentB);

            LinearLayout layout = new LinearLayout(rootView.getContext());
            final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setEnabled(false);

            final Spinner spinner2 = (Spinner) rootView.findViewById(R.id.spinner2);
            ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray2);
            spinner2.setAdapter(spinnerArrayAdapter2);
            spinner2.setEnabled(false);

            final Button right = (Button) rootView.findViewById(R.id.buttonConDis);
            right.setText("Disconnect");
            right.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final ProgressDialog ringProgressDialog = ProgressDialog.show(rootView.getContext(), "Please wait ...", "Disconnecting for printer ...", true);
                    ringProgressDialog.setCancelable(true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                util_send.Disconnect();
                                int toomany = 0;
                                while (util_get.isConnected()){
                                    if(toomany > 10) {break;}
                                    Thread.sleep(1000);
                                    toomany++;
                                }
                                new createConnect().execute();
                            } catch (Exception e){

                            }
                            ringProgressDialog.dismiss();

                        }

                    }).start();

                }
            });

        }
    }
    public class createConnect extends AsyncTask<Void, Void, Boolean>{
        private String[] CurrentB;
        private String[] CurrentS;
        @Override
        protected Boolean doInBackground(Void... voids) {
            if (!util.isPingServerTrue()){
                return false;
            }
            try {
                util_decode.decodeConnections();
                CurrentS = util.jsonArraytoStringArray(util_get.getSerialPort());
                CurrentB = util.jsonArraytoStringArray(util_get.getBaudrates());
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
            return  true;
        }

        @Override
        protected void onPostExecute(Boolean strings) {
            try {
                final Button right = (Button) rootView.findViewById(R.id.buttonConDis);
                if (!strings) {
                    util.logD("HYO");
                    right.setText("Connect");
                    right.setEnabled(false);
                    return;
                }else{
                    right.setEnabled(true);
                }
            }catch (Exception e){

            }
            ArrayList<String> spinnerArray = new ArrayList<String>();
            for (String i : CurrentS){
                spinnerArray.add(i.replace("\\/", "/"));
            }
            ArrayList<String> spinnerArray2 = new ArrayList<String>();
            for (String i2 : CurrentB){
                spinnerArray2.add(i2);
            }
            LinearLayout layout = new LinearLayout(rootView.getContext());
            final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setEnabled(true);

            final Spinner spinner2 = (Spinner) rootView.findViewById(R.id.spinner2);
            ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray2);
            spinner2.setAdapter(spinnerArrayAdapter2);
            spinner2.setEnabled(true);

            final Button right = (Button) rootView.findViewById(R.id.buttonConDis);
            right.setText("Connect");
            right.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final ProgressDialog ringProgressDialog = ProgressDialog.show(rootView.getContext(), "Please wait ...", "Connecting for printer ...", true);
                    ringProgressDialog.setCancelable(true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                util_send.Connect("true", "true", spinner.getSelectedItem().toString(), spinner2.getSelectedItem().toString());
                                int toomany = 0;
                                while (!util_get.isConnected()){
                                    if(toomany > 10) {util.logD("tomanny");break;}
                                        Thread.sleep(2000);
                                        toomany++;
                                }
                                new createDisconnect().execute();
                            } catch (Exception e){

                            }
                            ringProgressDialog.dismiss();
                        }

                    }).start();

                }
            });


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

            Log.d("OctoDroid", "doing the thing");
            if (util_get.isConnected()) {
                    new createDisconnect().execute();
            } else {
                    new createConnect().execute();
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
