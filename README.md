# PolygonProgressView
A custom view that display multiple attributes by a polygon shape

# Review
![image](https://github.com/TacticalTwerking/PolygonProgressView/blob/master/art/device-2016-06-27-145918.png)

# Simply Usage

Your labels
    String [] labels = new String[]{"label0","label1","label2"...};
Percentage values
    float [] values = new float[]{1,.3f,1...};
Initial data
    PolygonProgressView.initial(14,values,labels);
Animate it!
    PolygonProgressView.animateProgress();
