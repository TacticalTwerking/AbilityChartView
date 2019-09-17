# AbilityChartView
A custom view that display multiple attributes by a polygon shape

# Review
![image](https://github.com/TacticalTwerking/PolygonProgressView/blob/master/art/GifArt.gif)

# Simply Usage

1.Prepering your values

        String [] labels = new String[]{"label0","label1","label2"...};
        float [] values = new float[]{1,.3f,1...};
2.Initializing values

        PolygonProgressView.initial(14,values,labels);
3.Animate it!

        PolygonProgressView.animateProgress();



## Customizations your view
```xml
<tactical.twerk.abilitychartview.AbilityChartView
        android:id="@+id/ppv"
        app:acv_polygon_color = "@color/defaultPolygonColor"
        app:acv_circle_color = "@color/defaultCircleColor"
        app:acv_grid_color = "@color/defaultGridColor"
        app:acv_show_grid = "true"
        app:acv_grid_width = "1px"
        app:acv_circle_width = "2px"
        app:acv_label_txt_size = "12sp"
        app:acv_label_txt_color = "@color/colorPrimary"
        app:acv_center_image="@mipmap/ic_launcher"
        app:acv_minimal_value_percentage="40%p"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
```xml
