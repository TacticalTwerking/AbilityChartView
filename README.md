# PolygonProgressView
A custom view that display multiple attributes by a polygon shape

# Review
![image](https://github.com/TacticalTwerking/PolygonProgressView/blob/master/art/screenrecorder.20190916130848_5.gif)

# Simply Usage

1.Prepering your values

        String [] labels = new String[]{"label0","label1","label2"...};
        float [] values = new float[]{1,.3f,1...};
2.Initializing values

        PolygonProgressView.initial(14,values,labels);
3.Animate it!

        PolygonProgressView.animateProgress();
