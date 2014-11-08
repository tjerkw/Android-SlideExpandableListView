package com.tjerkw.slideexpandable.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;
import com.tjerkw.slideexpandable.library.SlideExpandableListener;

public class ExampleAnimListenerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_view);

        ListView lv_test = (ListView) findViewById(R.id.lv_test);

        //Use the wrapper expandable list adapter
        SlideExpandableListAdapter slideAdapter = new SlideExpandableListAdapter(
                buildDummyData(),
                R.id.expandable_toggle_button,
                R.id.expandable);

        slideAdapter.setExpandableSlideListener(new SlideExpandableListener() {

            @Override
            public void onStartExpandAnimation(View view, int position) {
                Log.d("slide", "start animation expand with item position: " + position);
            }

            @Override
            public void onEndExpandAnimation(View view, int position) {
                Log.d("slide", "end animation expand with item position:" + position);
            }

            @Override
            public void onStartCollapseAnimation(View view, int position) {
                Log.d("slide", "start animation collapse with item position: " + position);
            }

            @Override
            public void onEndCollapseAnimation(View view, int position) {
                Log.d("slide", "end animation collapse with item position: " + position);
            }
        });

        // fill the list with data
        lv_test.setAdapter(slideAdapter);
    }

    /**
     * Builds dummy data for the test.
     * In a real app this would be an adapter
     * for your data. For example a CursorAdapter
     */
    public ListAdapter buildDummyData() {
        final int SIZE = 20;
        String[] values = new String[SIZE];
        for(int i=0;i<SIZE;i++) {
            values[i] = "Item "+i;
        }
        return new ArrayAdapter<String>(
                this,
                R.layout.expandable_list_item,
                R.id.text,
                values
        );
    }
}
