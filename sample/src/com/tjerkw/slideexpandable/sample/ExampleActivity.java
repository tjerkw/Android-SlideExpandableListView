package com.tjerkw.slideexpandable.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

/**
 * This example shows a expandable listview
 * with a more button per list item which expands the expandable area.
 *
 * In the expandable area there are two buttons A and B which can be click.
 *
 * The events for these buttons are handled here in this Activity.
 *
 * @author tjerk
 * @date 6/13/12 7:33 AM
 */
public class ExampleActivity extends Activity {

	@Override
	public void onCreate(Bundle savedData) {

		super.onCreate(savedData);
		// set the content view for this activity, check the content view xml file
		// to see how it refers to the ActionSlideExpandableListView view.
		this.setContentView(R.layout.single_expandable_list);
		// get a reference to the listview, needed in order
		// to call setItemActionListener on it
		ActionSlideExpandableListView list = (ActionSlideExpandableListView)this.findViewById(R.id.list);

		// fill the list with data
		list.setAdapter(buildDummyData());

		// listen for events in the two buttons for every list item.
		// the 'position' var will tell which list item is clicked
		list.setItemActionListener(new ActionSlideExpandableListView.OnActionClickListener() {

			@Override
			public void onClick(View listView, View buttonview, int position) {

				/**
				 * Normally you would put a switch
				 * statement here, and depending on
				 * view.getId() you would perform a
				 * different action.
				 */
				String actionName = "";
				if(buttonview.getId()==R.id.buttonA) {
					actionName = "buttonA";
				} else {
					actionName = "ButtonB";
				}
				/**
				 * For testing sake we just show a toast
				 */
				Toast.makeText(
					ExampleActivity.this,
					"Clicked Action: "+actionName+" in list item "+position,
					Toast.LENGTH_SHORT
				).show();
			}

		// note that we also add 1 or more ids to the setItemActionListener
		// this is needed in order for the listview to discover the buttons
		}, R.id.buttonA, R.id.buttonB);
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
