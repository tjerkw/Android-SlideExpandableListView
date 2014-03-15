package com.tjerkw.slideexpandable.library;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.BitSet;

/**
 * Wraps a ListAdapter to give it expandable list view functionality.
 * The main thing it does is add a listener to the getToggleButton
 * which expands the getExpandableView for each list item.
 *
 * @author tjerk
 * @date 6/9/12 4:41 PM
 */
public abstract class AbstractSlideExpandableListAdapter extends WrapperListAdapterImpl {

	private static final int TAG_EXPANDABLE_VIEW = 381286553;
	private static final int TAG_EXPAND_TOGGLE_BUTTON = 668645171;

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
	 * Default Animation duration
	 * Set animation duration with @see setAnimationDuration
	 */
	private int animationDuration = 330;
	
	/**
	 * A list of positions of all list items that are expanded.
	 * Normally only one is expanded. But a mode to expand
	 * multiple will be added soon.
	 *
	 * If an item onj position x is open, its bit is set
	 */
	private BitSet openItems = new BitSet();
	/**
	 * We remember, for each collapsible view its height.
	 * So we don't need to recalculate.
	 * The height is calculated just before the view is drawn.
	 */
	private final SparseIntArray viewHeights = new SparseIntArray(10);

	/**
	 * If a view is trying to expand beyond the bounds of the list view,
	 * scroll the list to fit the expanded view.
	 */
	private boolean adjustToFit = true;

	/**
	* Will point to the ListView
	*/
	private ViewGroup parent;

	public AbstractSlideExpandableListAdapter(ListAdapter wrapped) {
		super(wrapped);
	}

	private OnItemExpandCollapseListener expandCollapseListener;

	/**
	 * Sets a listener which gets call on item expand or collapse
	 * 
	 * @param listener
	 *            the listener which will be called when an item is expanded or
	 *            collapsed
	 */
	public void setItemExpandCollapseListener(
			OnItemExpandCollapseListener listener) {
		expandCollapseListener = listener;
	}

	public void removeItemExpandCollapseListener() {
		expandCollapseListener = null;
	}

	/**
	 * If a view is trying to expand beyond the bounds of the list view,
	 * scroll the list to fit the expanded view.
	 *
	 * @param adjustToFit
	 *            true if the view should adjust to fit, false otherwise
	 */
	public void setAdjustToFit(boolean adjustToFit) {
		this.adjustToFit = adjustToFit;
	}

	/**
	 * Interface for callback to be invoked whenever an item is expanded or
	 * collapsed in the list view.
	 */
	public interface OnItemExpandCollapseListener {
		/**
		 * Called when an item is expanded.
		 * 
		 * @param itemView
		 *            the view of the list item
		 * @param position
		 *            the position in the list view
		 */
		public void onExpand(View itemView, int position);

		/**
		 * Called when an item is collapsed.
		 * 
		 * @param itemView
		 *            the view of the list item
		 * @param position
		 *            the position in the list view
		 */
		public void onCollapse(View itemView, int position);

	}

	private void notifyExpandCollapseListener(int type, View view, int position) {
		if (expandCollapseListener != null) {
			if (type == ExpandCollapseAnimation.EXPAND) {
				expandCollapseListener.onExpand(view, position);
			} else if (type == ExpandCollapseAnimation.COLLAPSE) {
				expandCollapseListener.onCollapse(view, position);
			}
		}

	}


	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		this.parent = viewGroup;
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

	private View findExpandToggleButton(View parent) {
		View view = (View) parent.getTag(TAG_EXPAND_TOGGLE_BUTTON);
		if (view == null) {
			view = getExpandToggleButton(parent);
			parent.setTag(TAG_EXPAND_TOGGLE_BUTTON, view);
		}
		return view;
	}

	private View findExpandableView(View parent) {
		View view = (View) parent.getTag(TAG_EXPANDABLE_VIEW);
		if (view == null) {
			view = getExpandableView(parent);
			parent.setTag(TAG_EXPANDABLE_VIEW, view);
		}
		return view;
	}

	/**
	 * Gets the duration of the collapse animation in ms.
	 * Default is 330ms. Override this method to change the default.
	 *
	 * @return the duration of the anim in ms
	 */
	public int getAnimationDuration() {
		return animationDuration;
	}
	/**
	 * Set's the Animation duration for the Expandable animation
	 * 
	 * @param duration The duration as an integer in MS (duration > 0)
	 * @exception IllegalArgumentException if parameter is less than zero
	 */
	public void setAnimationDuration(int duration) {
		if(duration < 0) {
			throw new IllegalArgumentException("Duration is less than zero");
		}
		
		animationDuration = duration;
	}
	/**
	 * Check's if any position is currently Expanded
	 * To collapse the open item @see collapseLastOpen
	 * 
	 * @return boolean True if there is currently an item expanded, otherwise false
	 */
	public boolean isAnyItemExpanded() {
		return (lastOpenPosition != -1) ? true : false;
	}

	public void enableFor(final View parent, final int position) {
		final View button = findExpandToggleButton(parent);
		final View target = findExpandableView(parent);
		target.measure(parent.getWidth(), parent.getHeight());

		if(parent == lastOpen && position!=lastOpenPosition) {
			// lastOpen is recycled, so its reference is false
			lastOpen = null;
		}
		if(position == lastOpenPosition) {
			// re reference to the last view
			// so when can animate it when collapsed
			lastOpen = parent;
		}
		int height = viewHeights.get(position, -1);
		if(height == -1) {
			viewHeights.put(position, target.getMeasuredHeight());
			updateExpandable(target,position);
		} else {
			updateExpandable(target, position);
		}
		target.requestLayout();

		button.setSelected(openItems.get(position));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {

				Animation a = target.getAnimation();

				if (a != null && a.hasStarted() && !a.hasEnded()) {

					a.setAnimationListener(new Animation.AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {
						}

						@Override
						public void onAnimationEnd(Animation animation) {
							view.performClick();
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
						}
					});

				} else {

					target.setAnimation(null);

					int type = target.getVisibility() == View.VISIBLE
							? ExpandCollapseAnimation.COLLAPSE
							: ExpandCollapseAnimation.EXPAND;

					// remember the state
					if (type == ExpandCollapseAnimation.EXPAND) {
						openItems.set(position, true);
					} else {
						openItems.set(position, false);
					}
					// check if we need to collapse a different view
					if (type == ExpandCollapseAnimation.EXPAND) {
						if (lastOpenPosition != -1 && lastOpenPosition != position) {
							if (lastOpen != null) {
								toggleExpand(
										lastOpen,
										ExpandCollapseAnimation.COLLAPSE,
										lastOpenPosition);
							}
							openItems.set(lastOpenPosition, false);
						}
						lastOpen = parent;
						lastOpenPosition = position;
					} else if (lastOpenPosition == position) {
						lastOpenPosition = -1;
					}
					toggleExpand(parent, type, position);
				}
			}
		});
	}

	private void toggleExpand(View view, int type, int position) {
		View target = findExpandableView(view);
		View button = findExpandToggleButton(view);
		button.setSelected(type == ExpandCollapseAnimation.EXPAND);
		animateView(target, type);
		notifyExpandCollapseListener(type, target, position);
	}

	private void updateExpandable(View target, int position) {

		final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)target.getLayoutParams();
		if(openItems.get(position)) {
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

		if (adjustToFit && type == ExpandCollapseAnimation.EXPAND) {
			anim.setAnimationListener(new AdjustAnimationListener(target));
		}
		target.startAnimation(anim);
	}

	private class AdjustAnimationListener implements Animation.AnimationListener {

		private View target;

		AdjustAnimationListener(View target) {
			this.target = target;
		}

			@Override
		public void onAnimationStart(Animation animation) {
		}

			@Override
		public void onAnimationRepeat(Animation animation) {
		}

			@Override
			public void onAnimationEnd(Animation animation) {

					if (parent instanceof ListView) {
						ListView listView = (ListView) parent;
						int movement = target.getBottom();

						Rect r = new Rect();
						boolean visible = target.getGlobalVisibleRect(r);
						Rect r2 = new Rect();
						listView.getGlobalVisibleRect(r2);
						
						if (!visible) {
							listView.smoothScrollBy(movement, getAnimationDuration());
						} else {
							if (r2.bottom == r.bottom) {
								listView.smoothScrollBy(movement, getAnimationDuration());
							}
						}
					}
				}
	}


	/**
	 * Closes the current open item.
	 * If it is current visible it will be closed with an animation.
	 *
	 * @return true if an item was closed, false otherwise
	 */
	public boolean collapseLastOpen() {
		if(isAnyItemExpanded()) {
			// if visible animate it out
			if(lastOpen != null) {
				toggleExpand(lastOpen, ExpandCollapseAnimation.COLLAPSE, lastOpenPosition);
			}
			openItems.set(lastOpenPosition, false);
			lastOpenPosition = -1;
			return true;
		}
		return false;
	}

	public Parcelable onSaveInstanceState(Parcelable parcelable) {

		SavedState ss = new SavedState(parcelable);
		ss.lastOpenPosition = this.lastOpenPosition;
		ss.openItems = this.openItems;
		return ss;
	}

	public void onRestoreInstanceState(SavedState state) {

		if (state != null) {
			this.lastOpenPosition = state.lastOpenPosition;
			this.openItems = state.openItems;
		}
	}

	/**
	 * Utility methods to read and write a bitset from and to a Parcel
	 */
	private static BitSet readBitSet(Parcel src) {
		BitSet set = new BitSet();
		if (src == null) {
			return set;
		}
		int cardinality = src.readInt();


		for (int i = 0; i < cardinality; i++) {
			set.set(src.readInt());
		}

		return set;
	}

	private static void writeBitSet(Parcel dest, BitSet set) {
		int nextSetBit = -1;

		if (dest == null || set == null) {
			return; // at least don't crash
		}

		dest.writeInt(set.cardinality());

		while ((nextSetBit = set.nextSetBit(nextSetBit + 1)) != -1) {
			dest.writeInt(nextSetBit);
		}
	}

	/**
	 * The actual state class
	 */
	static class SavedState extends View.BaseSavedState {
		public BitSet openItems = null;
		public int lastOpenPosition = -1;

		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			lastOpenPosition = in.readInt();
			openItems = readBitSet(in);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(lastOpenPosition);
			writeBitSet(out, openItems);
		}

		//required field that makes Parcelables from a Parcel
		public static final Parcelable.Creator<SavedState> CREATOR =
		new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}
