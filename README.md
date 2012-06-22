# SlideExpandableListView for Android

![Screenshot](https://github.com/tjerkw/Android-SlideExpandableListView/raw/master/raw/example-screens.png)

Not happy with the Android ExpandableListView android offers? Want something like the Spotify app. This library allows you to have custom listview in wich each list item has an area that will slide-out once the users clicks on a certain button.

## Features

 * Provides a better ExpandableListView usable for normal ListView's
 * Animates by default
 * Easy to use

Repository at <https://github.com/tjerkw/SlideExpandableListView>.

## Usage

### Layout

Use a normal list view in your layout.
You may also use a ListActivity or ListFragment

``` xml
<ListView
    android:id="@+id/list"
    android:layout_height="fill_parent"
    android:layout_width="fill_parent" />
```

The list item view should have a toggle button (Button view), and a target view that will be expanded.
By default the expandable view will be hidden. An when a user clicks the toggle button the
expandalbe view will slide out and be visible.

For example here below we have R.id.more Button view.
And a R.id.expandable LinearLayout which will be expanded.
Note that the expandable view does not have to be a LinearLayout,
it can be any subclass of View.

``` xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              xmlns:slide="http://schemas.android.com/apk/res/com.tjerkw.slideexpandable.library"
              android:layout_width="fill_parent"
              android:layout_height="wrap_content"
              android:orientation="vertical">
	<RelativeLayout
			android:layout_width="fill_parent"
			android:layout_height="wrap_content"
			android:orientation="horizontal"
			android:id="@+id/item">

		<TextView
				android:layout_width="fill_parent"
				android:layout_height="fill_parent"
				android:id="@+id/text"
				android:text="Hello World"/>

		<Button
				android:id="@+id/expandable_toggle_button"
				android:text="More"
				android:layout_width="wrap_content"
				android:layout_height="wrap_content"
				android:layout_alignBottom="@+id/text"
				android:layout_alignParentRight="true"
				android:layout_alignTop="@id/text"/>

	</RelativeLayout>

	<LinearLayout
			android:layout_width="fill_parent"
			android:layout_height="75dip"
			android:orientation="horizontal"
			android:id="@+id/expandable"
			android:background="#000000">

		<Button
				android:id="@+id/shoot"
				android:layout_width="fill_parent"
				android:layout_height="fill_parent" android:layout_weight="0.5"
				android:text="Action A"
				android:textSize="12dip"/>

		<Button
				android:id="@+id/details"
				android:layout_width="fill_parent"
				android:layout_height="fill_parent"
				android:layout_weight="0.5"
				android:text="Action B"
				android:textSize="12dip"/>

	</LinearLayout>
</LinearLayout>
```

## Wrap your ListAdapter

In order to provide the functionality you simply wrap your list adapter in a SlideExpandableListAdapter:

``` java
		ListView list = ... your list view
		ListAdapter adapter = ... your list adapter
		// now simply wrap the adapter
		// and indicate the ids of your toggle button
		// and expandable view
		list.setAdapter(
			new SlideExpandableListAdapter(
				adapter,
				R.id.expandable_toggle_button,
				R.id.expandable
			)
		);
```

## Pull Requests

If you have any contributions I am gladly to review them and use them if they make sense.

## Changelog

### v1.0

* First release!

## Acknowledgments

* [TjerkWolterink] (http://about.me/tjerkw, https://github.com/tjerkw)


## License

Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
