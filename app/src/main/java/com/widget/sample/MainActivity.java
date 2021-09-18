package com.widget.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.widget.linearcolorpicker.LinearColorPicker;

public class MainActivity extends AppCompatActivity {

private LinearColorPicker mVerticalColorPicker;
private LinearColorPicker mHorizontalColorPicker;
private View mVerticalColor;
private View mHorizontalColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVerticalColorPicker = findViewById(R.id.vertical_color_picker);
        mHorizontalColorPicker = findViewById(R.id.horizontal_color_picker);
        mVerticalColor = findViewById(R.id.vertical_color);
        mHorizontalColor = findViewById(R.id.horizontal_color);

        mVerticalColorPicker.setOnColorSelectListener((color, progress) -> mVerticalColor.setBackgroundColor(color));
        mHorizontalColorPicker.setOnColorSelectListener((color, progress) -> mHorizontalColor.setBackgroundColor(color));
    }
}