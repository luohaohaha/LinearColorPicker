# LinearColorPicker
一个线性颜色拾取器

![image](https://github.com/luohaohaha/LinearColorPicker/blob/master/img/device-2016-07-07-112325.png)
![image](https://github.com/luohaohaha/LinearColorPicker/blob/master/img/device-2016-07-07-112439.png)

支持垂直和水平方向


thumbDrawable>>>>滑块图标

colorPanelWidth>>>>颜色条大小

colorOrientation>>>>布局方向

gradientArray>>>>渐变颜色数组 默认为 0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF0000

```
 <saltyfish.linearcolorpicker.LinearColorPicker
        android:id="@+id/lcp"
        android:layout_width="match_parent"
        app:thumbDrawable="@mipmap/ic_launcher"
        android:layout_below="@+id/text"
        android:layout_marginTop="20dp"
        app:colorPanelWidth="10dp"
        app:colorOrientation="horizontal"
        android:layout_height="wrap_content" />
  ```

