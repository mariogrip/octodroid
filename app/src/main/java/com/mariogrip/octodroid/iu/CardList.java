package com.mariogrip.octodroid.iu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.mariogrip.octodroid.R;

public class CardList extends Activity {

    private static final String TAG = "CardListActivity";
    private cardAdapter cardArrayAdapter;
    private cardAdapter cardArrayAdapter2;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        listView = (ListView) findViewById(R.id.card_listView);

        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));

        cardArrayAdapter = new cardAdapter(getApplicationContext(), R.layout.list_item_card);

        for (int i = 0; i < 4; i++) {
            card card = new card("Card " + (i + 1) + " Line 1", "Card " + (i + 1) + " Line 2");
            cardArrayAdapter.add(card);
        }
        listView.setAdapter(cardArrayAdapter);
    }
}
