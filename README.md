# FitCropAndroidVision
A simple demo of Android Vision (https://github.com/googlesamples/android-vision) scan barcode with camera preview fit view size without stretch issue

<h3>Screenshot</h3>
<img src="http://i.imgur.com/j3xbfEc.png" alt="screenshot" width="600px" height="auto" />

<h3>Layout Example</h3>
```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    android:weightSum="2">

    <vn.vadaihiep.fitcropandroidvision.camera.CameraSourcePreview
        android:id="@+id/preview"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1" />

    <TextView
        android:id="@+id/txtResult"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:gravity="center"
        android:text="Result here"
        android:textSize="16sp" />
</LinearLayout>
```
