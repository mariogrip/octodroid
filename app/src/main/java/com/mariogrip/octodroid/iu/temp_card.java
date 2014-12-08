package com.mariogrip.octodroid.iu;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;

import com.mariogrip.octodroid.R;
import com.mariogrip.octodroid.memory;
import com.mariogrip.octodroid.util;
import com.mariogrip.octodroid.util_send;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by mariogrip on 01.12.14.
 */
public class temp_card extends Fragment {

    public temp_card(){}
    private static final String TAG = "CardListActivity";
    private ListView listView;
    private View rootView;
    private Integer setExt = 0;
    private Integer setBed = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.card_main, container, false);


        ArrayList<Card> cards = new ArrayList<Card>();
        cardtest card = new cardtest(rootView.getContext(),"Heatbed", "HeatBed");
        cards.add(card);
        cardtest2 card2 = new cardtest2(rootView.getContext(),"Extruder", "Extruder");
        cards.add(card2);


        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(rootView.getContext(),cards);
        mCardArrayAdapter.setInnerViewTypeCount(3);

        CardListView listView = (CardListView) rootView.findViewById(R.id.card_main_card_list);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }
        return rootView;
    }


    public class cardtest extends Card{

        protected String mTitleHeader;
        protected String mTitleMain;

        public cardtest(Context context,String titleHeader,String titleMain) {
            super(context, R.layout.card_bead);
            this.mTitleHeader=titleHeader;
            this.mTitleMain=titleMain;
            init();
        }

        private void init(){
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
        public void setupInnerViewElements(ViewGroup parent, View view) {
            SeekBar seekBar = (SeekBar) parent.findViewById(R.id.seekBar_bed);
            final Button setbutton = (Button) parent.findViewById(R.id.button_setBed);
            Integer temp = Integer.parseInt(memory.bedTempTarget);
            if (temp > 100){
                temp = 100;
            }
            seekBar.setProgress(temp);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progress = 0;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                    progress = progresValue;
                    setBed = progress;
                    setbutton.setText("Set to " + setBed + "°C");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    setBed = progress;
                    setbutton.setText("Set to " + setBed + "°C");
                }
            });

            Button button2 = (Button) parent.findViewById(R.id.button_setBed);
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    util_send.setBedTemp(setBed.toString());
                }
            });


        }
    }

    public class cardtest2 extends Card{

        protected String mTitleHeader;
        protected String mTitleMain;

        public cardtest2(Context context,String titleHeader,String titleMain) {
            super(context, R.layout.card_heat);
            this.mTitleHeader=titleHeader;
            this.mTitleMain=titleMain;
            init();
        }

        private void init(){
           // CardHeader header = new CardHeader(rootView.getContext());
            //header.setTitle(mTitleHeader);
           // addCardHeader(header);
           // setTitle(mTitleMain);
        }


        @Override
        public int getType() {
            //Very important with different inner layouts
            return 1;
        }
        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
            SeekBar seekBar = (SeekBar) parent.findViewById(R.id.seekBar_ext);
            final Button setbutton = (Button) parent.findViewById(R.id.button_ext);
            Integer temp = Integer.parseInt(memory.ExtTempTarget) / 3;
            if (temp > 100){
                temp = 100;
            }
            seekBar.setProgress(temp);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progress = 0;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                    progress = progresValue;
                    setExt = progress * 3;
                    setbutton.setText("Set to " + setExt + "°C");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    setExt = progress * 3;
                    setbutton.setText("Set to " + setExt + "°C");
                }
            });
            setbutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    util_send.setExtTemp(setExt.toString());
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
