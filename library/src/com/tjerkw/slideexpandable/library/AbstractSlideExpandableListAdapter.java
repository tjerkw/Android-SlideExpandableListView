package com.tjerkw.slideexpandable.library;

import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import java.util.HashSet;
import java.util.Set;

/**
 * Wraps a ListAdapter to give it expandable list view functionality.
 * The main thing it does is add a listener to the getToggleButton
 * which expands the getExpandableView for each list item.
 *
 * @author tjerk
 * @date 6/9/12 4:41 PM
 */
public abstract class AbstractSlideExpandableListAdapter extends WrapperListAdapterImpl {
	/**
	 * Reference to the last expanded list item.
	 * Since lists are recycled this might be null if
	 * though there is an expanded list item
	 */
	private View lastOpen = null;
	/**
	 * The position of the last expanded list item.
	 * If -1 there is no list item expanded.
	 * Otherwise it points to the position of the last expanded list item
	 */
	private int lastOpenPosition = -1;
	/**
	 * A list of positions of all list items that are expanded.
	 * Normally only one is expanded. But a mode to expand
	 * multiple will be added soon.
	 */
	private final Set<Integer> openItems = new HashSet<Integer>(3);
	/**
	 * We remember, for each collapsable view its height.
	 * So we dont need to recalculate.
	 * The height is calculated just before the view is drawn.
	 */
	private final SparseIntArray viewHeights = new SparseIntArray(10);

	public AbstractSlideExpandableListAdapter(ListAdapter wrapped) {
		super(wrapped);
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		view = wrapped.getView(position, view, viewGroup);
		enableFor(view, position);
		return view;
	}

	/**
	 * This method is used to get the Button view that should
	 * expand or collapse the Expandable View.
	 * <br/>
	 * Normally it will be implemented as:
	 * <pre>
	 * return parent.findViewById(R.id.expand_toggle_button)
	 * </pre>
	 *
	 * A listener will be attached to the button which will
	 * either expand or collapse the expandable view
	 *
	 * @see #getExpandableView(View)
	 * @param parent the list view item
	 * @ensure return!=null
	 * @return a child of parent which is a button
	 */
	public abstract View getExpandToggleButton(View parent);

	/**
	 * This method is used to get the view that will be hidden
	 * initially and expands or collapse when the ExpandToggleButton
	 * is pressed @see getExpandToggleButton
	 * <br/>
	 * Normally it will be implemented as:
	 * <pre>
	 * return parent.findViewById(R.id.expandable)
	 * </pre>
	 *
	 * @see #getExpandToggleButton(View)
	 * @param parent the list view item
	 * @ensure return!=null
	 * @return a child of parent which is a view (or often ViewGroup)
	 *  that can be collapsed and expanded
	 */
	public abstract View getExpandableView(View parent);

	/**
	 * Gets the duration of the collapse animation in ms.
	 * Default is 330ms. Override this method to change the default.
	 *
	 * @return the duration of the anim in ms
	 */
	protected int getAnimationDuration() {
		return 330;
	}

	public void enableFor(View parent, int position) {
		View more = getExpandToggleButton(parent);
		View itemToolbar = getExpandableView(parent);
		itemToolbar.measure(parent.getWidth(), parent.getHeight());

		enableFor(more, itemToolbar, position);
	}


	private void enableFor(final View button, final View target, final int position) {
		if(target == lastOpen && position!=lastOpenPosition) {
			// lastOpen is recycled, so its reference is false
			lastOpen = null;
		}
		if(position == lastOpenPosition) {
			// re reference to the last view
			// so when can animate it when collapsed
			lastOpen = target;
		}
		int height = viewHeights.get(position, -1);
		if(height == -1) {
			viewHeights.put(position, target.getMeasuredHeight());
			updateExpandable(target,position);
		} else {
			updateExpandable(target, position);
		}

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				view.setAnimation(null);
				// check wether we need to expand or collapse
				int type =
					target.getVisibility() == View.VISIBLE
					? ExpandCollapseAnimation.COLLAPSE
					: ExpandCollapseAnimation.EXPAND;

				// remember the state
				if(type == ExpandCollapseAnimation.EXPAND) {
					openItems.add(position);
				} else {
					openItems.remove(position);
				}
				// check if we need to collapse a different view
				if(type == ExpandCollapseAnimation.EXPAND) {
					if(lastOpenPosition != -1 && lastOpenPosition != position) {
						if(lastOpen!=null) {
							animateView(lastOpen, ExpandCollapseAnimation.COLLAPSE);
						}
						openItems.remove(lastOpenPosition);
					}
					lastOpen = target;
					lastOpenPosition = position;
				} else if(lastOpenPosition == position) {
					lastOpenPosition = -1;
				}

				animateView(target, type);
			}
		});
	}

	private void updateExpandable(View target, int position) {

		final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)target.getLayoutParams();
		if(openItems.contains(position)) {
			target.setVisibility(View.VISIBLE);
			params.bottomMargin = 0;
		} else {
			target.setVisibility(View.GONE);
			params.bottomMargin = 0-viewHeights.get(position);
		}
	}

	/**
	 * Performs either COLLAPSE or EXPAND animation on the target view
	 * @param target the view to animate
	 * @param type the animation type, either ExpandCollapseAnimation.COLLAPSE
	 *			 or ExpandCollapseAnimation.EXPAND
	 */
	private void animateView(final View target, final int type) {
		Animation anim = new ExpandCollapseAnimation(
				target,
				type
		);
		anim.setDuration(getAnimationDuration());
		target.startAnimation(anim);
	}


	/**
	 * Closes the current open item.
	 * If it is current visible it will be closed with an animation.
	 *
	 * @return true if an item was closed, false otherwise
	 */
	public boolean collapseLastOpen() {
		if(lastOpenPosition != -1) {
			// if visible animate it out
			if(lastOpen != null) {
				animateView(lastOpen, ExpandCollapseAnimation.COLLAPSE);
			}
			openItems.remove(lastOpenPosition);
			lastOpenPosition = -1;
			return true;
		}
		return false;
	}
}