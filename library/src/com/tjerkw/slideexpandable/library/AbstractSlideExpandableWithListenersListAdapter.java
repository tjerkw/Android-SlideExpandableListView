package com.tjerkw.slideexpandable.library;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public abstract class AbstractSlideExpandableWithListenersListAdapter extends SlideExpandableListAdapter {

	private ListView list;

	public AbstractSlideExpandableWithListenersListAdapter(ListAdapter wrapped, ListView list) {
		super(wrapped);
		this.list = list;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup viewGroup) {
		
		final View v = super.getView(position, convertView, viewGroup);
		for (int i : getExpandedClickableViewsIds()) {
			final int viewId = i;
			final View clickable = v.findViewById(viewId);
			clickable.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View button) {
					onListItemExpandedViewClick(list, position, viewId);
					if (shouldCollapseAfterClick()) {
						collapseExpandableView(v);
					}
				}
			});
		}
		return v;
	}

	/**
	 * This method returns an array of views ids (often buttons) who are children of the expandable view,
	 * and will trigger the @see onListItemExpandedViewClick
	 * @see onListItemExpandedViewClick
	 * @return an array of views ids that are children of the expandable view
	 */
	public abstract int[] getExpandedClickableViewsIds();
	
	/**
	 * A callback to be invoked when the registered children of the expandable view are clicked 
	 * @param list the listView holding the expandable list item
	 * @param position the position of the clicked item
	 * @param clickedViewId the id of the clicked item
	 */
	public abstract void onListItemExpandedViewClick(ListView list, int position, int clickedViewId);

	/**
	 * A predicate for telling whether or not to collapse the expandable view after a child is clicked
	 * @return true if the expandable view should collapse after a child is clicked
	 */
	public abstract boolean shouldCollapseAfterClick();
	
	

}
