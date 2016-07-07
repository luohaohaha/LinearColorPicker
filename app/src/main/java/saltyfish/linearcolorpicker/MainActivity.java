package saltyfish.linearcolorpicker;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearColorPicker colorPicker= (LinearColorPicker) findViewById(R.id.lcp);
//        colorPicker.setOrientation(LinearColorPicker.VERTICAL);
//        colorPicker.setColors(Color.RED,Color.GREEN,Color.BLUE);
//        colorPicker.setColorPanelWidth(10);
        final TextView textView= (TextView) findViewById(R.id.text);
        colorPicker.setOnColorSelectListener(new LinearColorPicker.OnColorSelectListener() {
            @Override
            public void onColorSelect(int color) {
                textView.setBackgroundColor(color);
            }
        });
    }
}
