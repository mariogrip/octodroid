package com.mariogrip.octodroid.iu;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mariogrip.octodroid.R;
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
public class cont_card extends Fragment {

    public cont_card(){}
    private static final String TAG = "CardListActivity";
    private ListView listView;
    private View rootView;
    public static ViewGroup test123;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.card_main, container, false);


        ArrayList<Card> cards = new ArrayList<Card>();
        cardtest2 card2 = new cardtest2(rootView.getContext(),"Controls", "Controls");
        cards.add(card2);
        cardtest card1 = new cardtest(rootView.getContext(), "Printhead Controls", "Printhead Controls");
        cards.add(card1);
        cardteststart cardcont = new cardteststart(rootView.getContext(),"Start/Stop", "Startstop");
        cards.add(cardcont);




        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(rootView.getContext(),cards);
        mCardArrayAdapter.setInnerViewTypeCount(3);

        CardListView listView = (CardListView) rootView.findViewById(R.id.card_main_card_list);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }
        return rootView;
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



    public class cardtest extends Card{

        protected String mTitleHeader;
        protected String mTitleMain;

        public cardtest(Context context,String titleHeader,String titleMain) {
            super(context, R.layout.card_printhead);
            this.mTitleHeader=titleHeader;
            this.mTitleMain=titleMain;
            init();
        }

        private void init(){
            CardHeader header = new CardHeader(rootView.getContext());
            header.setTitle(mTitleHeader);
            addCardHeader(header);
            setTitle(mTitleMain);
        }


        @Override
        public int getType() {
            return 0;
        }
        @Override
        public void setupInnerViewElements(final ViewGroup parent, View view) {

            Button ex = (Button) parent.findViewById(R.id.button_reac);
            ex.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("OctoDroid", "button ex Pressed");
                    EditText text1 = (EditText) parent.findViewById(R.id.editText_mm);
                    if (util.isNumeric(text1.getText().toString())){
                        if (Double.parseDouble(text1.getText().toString()) < 1000) {
                            util_send.Extrude("-"+text1.getText().toString());
                        }else{
                            Toast.makeText(parent.getContext(),"Please do not Exstrude more than 1000mm", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(parent.getContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Button re = (Button) parent.findViewById(R.id.button_extB);
            re.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("OctoDroid", "button re Pressed");
                    EditText text1 = (EditText) parent.findViewById(R.id.editText_mm);
                    if (util.isNumeric(text1.getText().toString())){
                     if (Double.parseDouble(text1.getText().toString()) < 1000) {
                         util_send.Extrude(text1.getText().toString());
                     }else{
                         Toast.makeText(parent.getContext(),"Please do not Exstrude more than 1000mm", Toast.LENGTH_SHORT).show();
                     }
                    }else{
                        Toast.makeText(parent.getContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    public class cardtest2 extends Card{

        protected String mTitleHeader;
        protected String mTitleMain;

        public cardtest2(Context context,String titleHeader,String titleMain) {
            super(context, R.layout.card_controls);
            this.mTitleHeader=titleHeader;
            this.mTitleMain=titleMain;
            init();
        }

        private void init(){
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
        public void setupInnerViewElements(final ViewGroup parent, View view) {

            final RadioGroup Group = (RadioGroup) parent.findViewById(R.id.radiogrrr);
            Button up = (Button) parent.findViewById(R.id.buttonUp);
            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("OctoDroid", "button up Pressed");
                    int selectedId = Group.getCheckedRadioButtonId();
                    RadioButton radiobuttons = (RadioButton) parent.findViewById(selectedId);
                    util_send.goY(radiobuttons.getText().toString());
                }
            });

            final Button button = (Button) parent.findViewById(R.id.button_down);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("OctoDroid", "button Down Pressed");
                    int selectedId = Group.getCheckedRadioButtonId();
                    RadioButton radiobuttons = (RadioButton) parent.findViewById(selectedId);
                    util_send.goY("-" + radiobuttons.getText().toString());
                }
            });


            Button zup = (Button) parent.findViewById(R.id.button_zup);
            zup.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("OctoDroid", "button zup Pressed");
                    int selectedId = Group.getCheckedRadioButtonId();
                    RadioButton radiobuttons = (RadioButton) parent.findViewById(selectedId);
                    util_send.goZ(radiobuttons.getText().toString());
                }
            });

            Button right = (Button) parent.findViewById(R.id.button_right);
            right.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("OctoDroid", "button right Pressed");
                    int selectedId = Group.getCheckedRadioButtonId();
                    RadioButton radiobuttons = (RadioButton) parent.findViewById(selectedId);
                    util_send.goX(radiobuttons.getText().toString());
                }
            });

            Button zdown = (Button) parent.findViewById(R.id.button_zdown);
            zdown.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("OctoDroid", "button zdowbn Pressed");
                    int selectedId = Group.getCheckedRadioButtonId();
                    RadioButton radiobuttons = (RadioButton) parent.findViewById(selectedId);
                    util_send.goZ("-" + radiobuttons.getText().toString());
                }
            });

            Button left = (Button) parent.findViewById(R.id.button_left);
            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("OctoDroid", "button left Pressed");
                    int selectedId = Group.getCheckedRadioButtonId();
                    RadioButton radiobuttons = (RadioButton) parent.findViewById(selectedId);
                    util_send.goX("-"  + radiobuttons.getText().toString());
                }
            });

            Button home = (Button) parent.findViewById(R.id.button_home);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("OctoDroid", "button home Pressed");
                    util_send.goHome();
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
