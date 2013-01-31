# SlideExpandableListView for Android

![Screenshot](https://github.com/tjerkw/Android-SlideExpandableListView/raw/master/raw/example-screens.png)

Not happy with the Android ExpandableListView android offers? Want something like the Spotify app. This library allows you to have custom listview in wich each list item has an area that will slide-out once the users clicks on a certain button.

## Features

 * Provides a better ExpandableListView usable for normal ListView's
 * Animates by default
 * Easy to use

Repository at <https://github.com/tjerkw/Android-SlideExpandableListView/>.

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
              android:layout_width="fill_parent"
              android:layout_height="wrap_content"
              android:orientation="vertical">
	<RelativeLayout
			android:layout_width="fill_parent"
			android:layout_height="wrap_content"
			android:orientation="horizontal">

		<TextView
				android:layout_width="fill_parent"
				android:layout_height="fill_parent"
				android:id="@+id/text"
				android:text="Hello World"/>

		<!-- this is the button that will trigger sliding of the expandable view -->
		<Button
				android:id="@+id/expandable_toggle_button"
				android:text="More"
				android:layout_width="wrap_content"
				android:layout_height="wrap_content"
				android:layout_alignBottom="@+id/text"
				android:layout_alignParentRight="true"
				android:layout_alignTop="@id/text"/>

	</RelativeLayout>

	<!-- this is the expandable view that is initially hidden and will slide out when the more button is pressed -->
	<LinearLayout
			android:layout_width="fill_parent"
			android:layout_height="fill_parent"
			android:orientation="horizontal"
			android:id="@+id/expandable"
			android:background="#000000">

		<!-- put whatever you want in the expandable view -->
		<Button
				android:layout_width="fill_parent"
				android:layout_height="fill_parent"
				android:layout_weight="0.5"
				android:text="Action A" />

		<Button
				android:id="@+id/details"
				android:layout_width="fill_parent"
				android:layout_height="fill_parent"
				android:layout_weight="0.5"
				android:text="Action B"/>

	</LinearLayout>
</LinearLayout>
```

### Wrap your ListAdapter

In order to provide the functionality you simply wrap your list adapter in a SlideExpandableListAdapter.
The adapter gets the ids to the more button, and the expandable view as parameters. This allows the adapter
to find those views.

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

### Use the SlideExpandableListView or ActionSlideExpandableListView

In order to simplify the usage of this library, you can also use the mentioned ListViews directly in your
layout xml file. The view itself will make sure the ListAdapter is wrapped in a SlideExpandableListAdapter.

See the sample app for usage information.

### Including In Your Project

Currently you have to checkout the project, include it in your eclipse and ensure your project includes it.

I am working on putting it in maven Central so it can be easily added to any maven Android project.

A jar may also be an option, however i want to extend this library with android resources,
which you cannot include as a jar file.

## Pull Requests

If you have any contributions I am gladly to review them and use them if they make sense.

## Changelog

### v1.1.0

* Added ActionSlideExpandableListView for easier event listening, see the sample app
* Updated the sample app to also contain event handling logic (Solved issue #3)
* Solved the issue with random views being expanded, due to recycling of views was not properly handled
* Solved more issues #1 #2

### v1.0.0

* First release!

## Acknowledgments

* [TjerkWolterink] (http://about.me/tjerkw), about me (https://github.com/tjerkw), my linked in (http://www.linkedin.com/in/tjerkwolterink)
* [Udinic] (https://github.com/Udinic/SmallExamples/tree/master/ExpandAnimationExample), his blog (http://udinic.wordpress.com/2011/09/03/expanding-listview-items/) contains the initial idea

## License

Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

[![githalytics.com alpha](https://cruel-carlota.pagodabox.com/789c42a6ae45661a79e6e2695942ef65 "githalytics.com")](http://githalytics.com/tjerkw/Android-SlideExpandableListView)
