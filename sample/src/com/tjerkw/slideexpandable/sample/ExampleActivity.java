package com.tjerkw.slideexpandable.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

/**
 *
 * @author tjerk
 * @date 6/13/12 7:33 AM
 */
public class ExampleActivity extends Activity {

	@Override
	public void onCreate(Bundle savedData) {

		super.onCreate(savedData);
		this.setContentView(R.layout.single_expandable_list);
		ListView list = (ListView)this.findViewById(R.id.list);

		String[] values = {
				"1",
				"2",
				"3",
				"4",
				"5"
		};
		ListAdapter adapter = new ArrayAdapter<String>(
			this,
			R.layout.expandable_list_item,
			R.id.text,
			values
		);
		list.setAdapter(
			new SlideExpandableListAdapter(
				adapter,
				R.id.expandable_toggle_button,
				R.id.expandable
			)
		);
	}

}
