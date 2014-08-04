CircularView
============
[![Build Status](https://travis-ci.org/sababado/CircularView.svg?branch=master)](https://travis-ci.org/sababado/CircularView)

[Trello Project Board](https://trello.com/b/RV5JNOjD/circularview-android-library)

A custom view for Android. It consists of a larger center circle that it surrounded by other circles. Each of the surrounding circles (or `CircularViewObject`'s) can be represented by data or simply as a visual.

![Quick example screenshot](http://s27.postimg.org/a9nzujq8z/Circular_View_example.png)

![Usage in Date Night](http://s27.postimg.org/wbvf0be5v/Screenshot_2014_05_25_17_31_57.png)
![Screenshot of the sample app](http://s27.postimg.org/4eb72vecz/Screenshot_2014_05_25_17_31_42.png)
##Usage

The library can be referenced from maven central.

Gradle
```Groovy
compile 'com.sababado.circularview:library:1.0.+'
```

The `CircularView` can be defined in a XML layout or in code.

##Quick Setup
###Adding the view to a layout
```XML
<com.sababado.circularview.CircularView
    android:id="@+id/circular_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:centerBackgroundColor="#33b5e5"
    app:centerDrawable="@drawable/center_bg"/>
```

Using the custom attributes requires the following in the layout file. [Example](sample/src/main/res/layout/activity_main.xml)
```
xmlns:app="http://schemas.android.com/apk/res-auto"
```

###Adding `Marker`s
A `Marker` is an object that visual "floats" around the view. Each marker is can represent data or it can simply be for visual effect. Markers must be customized through a `CircularViewAdapter`.
```JAVA
public class MySimpleCircularViewAdapter extends SimpleCircularViewAdapter {
    @Override
    public int getCount() {
        // This count will tell the circular view how many markers to use.
        return 20;
    }

    @Override
    public void setupMarker(final int position, final Marker marker) {
        // Setup and customize markers here. This is called every time a marker is to be displayed.
        // 0 >= position > getCount()
        // The marker is intended to be reused. It will never be null.
        marker.setSrc(R.drawable.ic_launcher);
        marker.setFitToCircle(true);
        marker.setRadius(10 + 2 * position);
    }
}
```

Once the `CircularViewAdapter` implementation is ready it can be set on a `CircularView` object.
```JAVA
mAdapter = new MySimpleCircularViewAdapter();
circularView = (CircularView) findViewById(R.id.circular_view);
circularView.setAdapter(mAdapter);
```

###Receiving click listeners
Click events can be received from the `CircularView`.

To receive click events set a `CircularView.OnClickListener` into `circularView.setOnCircularViewObjectClickListener(l)`. For example:
```JAVA
circularView.setOnCircularViewObjectClickListener(new CircularView.OnClickListener() {
	 public void onClick(final CircularView view) {
        Toast.makeText(MainActivity.this, "Clicked center", Toast.LENGTH_SHORT).show();
    }

    public void onMarkerClick(CircularView view, Marker marker, int position) {
        Toast.makeText(MainActivity.this, "Clicked " + marker.getId(), Toast.LENGTH_SHORT).show();
    }
});
```

###Animation
There are a few simple animations built into the library at the moment.
####Animate Highlighted Degree
The `CircularView` has `animateHighlightedDegree(start, end, duration)`. The method takes a start and end position in degrees, and a long value for the duration of the animation.
The highlighted degree refers to which degree is "highlighted" or "focused". When a degree is focused it can trigger a secondary animation automatically for a `Marker`.

A listener can be set to receive a callback when this animation ends, and on what object it stopped on.
```JAVA
circularView.setOnHighlightAnimationEndListener(new CircularView.OnHighlightAnimationEndListener() {
    @Override
    public void onHighlightAnimationEnd(CircularView view, Marker marker, int position) {
        Toast.makeText(MainActivity.this, "Spin ends on " + marker.getId(), Toast.LENGTH_SHORT).show();
    }
});
```

####Marker Animation Options
`Marker`s have a simple animation associated with them; up and down. It can repeat or it can happen once.
The `CircularView` can trigger the bounce animation when `animateHighlightedDegree(start, end, duration)` is called. The bounce animation can be turned off by calling the same method with an additional flag.
For example:
```JAVA
animateHighlightedDegree(start, end, duration, shouldAnimateMarkers)
```

In addition there is control over if a marker should bounce while it is highlighted and while the highlighted degree value is constant (aka not animating).
```JAVA
// Allow markers to continuously animate on their own when the highlight animation isn't running.
circularView.setAnimateMarkerOnStillHighlight(true);
// Combine the above line with the following so that the marker at it's position will animate at the start.
circularView.setHighlightedDegree(circularView.BOTTOM);
```

The latter line is necessary in case the bounce animation should also run initially. The highlighted degree is set to `CircularView.HIGHLIGHT_NONE` by default.

##Proguard
If using proguard add the following to your proguard script to make sure animations run

```
# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}
# keep setters in CircularViewObjects so that animations can still work.
-keepclassmembers public class * extends com.sababado.circularview.CircularViewObject {
   void set*(***);
   *** get*();
}
```


##Developer Hints
* Every property that can be customized on a `CircularViewObject` can also be customized on a `Marker` object. A `Marker` object extends from a `CircularViewObject`. The former is used as a smaller object that floats around the center object. The center object is a `CircularViewObject`.
* By default, markers are drawn in the order that they're created; meaning if markers overlap then the first marker will be partially covered by the next marker. An option can be set to draw the highlighted marker on top of the markers next to it with `circularView.setDrawHighlightedMarkerOnTop(true);`. The flag is false by default.
* Any CircularViewObject can be hidden and shown independently of other objects using `setVisibility(int)`
* In a layout editor use the attribute `editMode_markerCount` and `editMode_markerRadius` to see the size and layout of markers. Not supplying a radius will show the default radius.

##License
```
Copyright 2014 Robert Szabo

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
