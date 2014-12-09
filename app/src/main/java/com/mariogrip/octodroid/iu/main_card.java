package com.mariogrip.octodroid.iu;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mariogrip.octodroid.R;
import com.mariogrip.octodroid.util;
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
public class main_card extends Fragment {

    public main_card(){}
    private static final String TAG = "CardListActivity";
    private ListView listView;
    private View rootView;
    public static ViewGroup test123;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.card_main, container, false);


        ArrayList<Card> cards = new ArrayList<Card>();
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

        //cards.add(cardss);

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(rootView.getContext(),cards);
        mCardArrayAdapter.setInnerViewTypeCount(3);

        CardListView listView = (CardListView) rootView.findViewById(R.id.card_main_card_list);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }
        ViewToClickToExpand viewToClickToExpand =
                ViewToClickToExpand.builder()
                        .highlightView(false)
                        .setupCardElement(ViewToClickToExpand.CardElementUI.CARD);
        cardss.setViewToClickToExpand(viewToClickToExpand);

        return rootView;

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
        }


        @Override
        public int getType() {
            //Very important with different inner layouts
            return 0;
        }
        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

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
        public void setupInnerViewElements(ViewGroup parent, View view) {
            Button up = (Button) parent.findViewById(R.id.button_stop);
            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    util_send.stopprint();
                }
            });

            Button button = (Button) parent.findViewById(R.id.button_start);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    util_send.startprint();
                }
            });


            Button right = (Button) parent.findViewById(R.id.button_pause);
            right.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    util_send.pauseprint();
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
            spinnerArray.add("one");
            spinnerArray.add("two");
            LinearLayout layout = new LinearLayout(parent.getContext());
            Spinner spinner = (Spinner) parent.findViewById(R.id.spinner);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(parent.getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
            spinner.setAdapter(spinnerArrayAdapter);
        }
    }

    public class CustomExpandCard extends CardExpand {

        public CustomExpandCard(Context context) {
            super(context, R.layout.card_connect);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
            parent.setBackgroundColor(mContext.getResources().
                    getColor(R.color.card_background));
            if (view == null) return;


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
