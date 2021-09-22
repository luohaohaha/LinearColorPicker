package com.widget.linearcolorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Project: LinearColorPicker<br/>
 * Package: com.widget.linearcolorpicker<br/>
 * ClassName: LinearColorPicker<br/>
 * Description: TODO<br/>
 * Date: 2021-09-18 2:54 下午 <br/>
 * <p>
 * Author luohao<br/>
 * Version 1.0<br/>
 * since JDK 1.6<br/>
 * <p>
 */
public class LinearColorPicker extends View {
    /**
     * Tht color picker thumb
     */
    private Drawable mThumb;
    /**
     * The thumb sliding distance
     */
    private float mThumbOffset = 0;
    private int mPaddingLeft;
    private int mPaddingTop;
    /**
     * The color paint
     */
    private Paint mPaint = null;
    /**
     * The color picker direction
     */
    private int mOrientation;
    /**
     * The color picker height
     */
    private float mColorPanelHeight;
    /**
     * The round corner
     */
    private float mRoundCorner = 0;
    /**
     * The color picker color gradient array
     */
    private int[] mColors = new int[]{ // 渐变色数组
            0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF0000};

    private static final int MAX_PROGRESS = 100;
    /**
     * The offset to be subtracted is half of the thumb
     */
    private float mOffset = 0f;

    /**
     * The default height
     */
    private int mDefaultHeight = 50;

    /**
     * The direction enum
     */
    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 0;

    /**
     * The current progress
     */
    private int mCurrentProgress = -1;

    public LinearColorPicker(Context context) {
        super(context);
        initAttrs(context, null);
    }

    public LinearColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public LinearColorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {

        if (null == attrs)
            return;
        setClickable(true);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LinearColorPicker);
        // thumb
        mThumb = a.getDrawable(R.styleable.LinearColorPicker_pickerThumbDrawable);
        // direction
        mOrientation = a.getInt(R.styleable.LinearColorPicker_pickerOrientation, HORIZONTAL);
        // height
        mColorPanelHeight = a.getDimensionPixelOffset(R.styleable.LinearColorPicker_pickerHeight, mDefaultHeight);
        // round corner
        mRoundCorner = a.getDimensionPixelOffset(R.styleable.LinearColorPicker_pickerRoundCorner, 0);
        // color array
        int colorArrayId = a.getResourceId(R.styleable.LinearColorPicker_pickerGradientArray, -1);
        if (-1 != colorArrayId) {
            mColors = getResources().getIntArray(colorArrayId);
        }
        //recycle attrs
        a.recycle();

        if (null != mThumb) {
            mThumb.setBounds(0, 0, mThumb.getIntrinsicWidth(), mThumb.getIntrinsicWidth());
        }
        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();

        //init offset
        mOffset = null == mThumb ? 0 : mThumb.getIntrinsicWidth() / 2;
        lastX = mOffset;
        lastY = mOffset;

        //init paint
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mColorPanelHeight);
    }

    public Drawable getThumb() {
        return mThumb;
    }

    public void setThumb(Drawable thumb) {
        this.mThumb = thumb;
        requestLayout();
    }

    public int getOrientation() {
        return mOrientation;
    }

    /**
     * The  direction {@link #HORIZONTAL} or {@link #VERTICAL}. Default value is
     * {@link #HORIZONTAL}.
     *
     * @param orientation
     */
    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
        requestLayout();
    }

    public float getColorPanelHeight() {
        return mColorPanelHeight;
    }

    public void setColorPanelHeight(float panelHeight) {
        this.mColorPanelHeight = panelHeight;
        if (null == mPaint)
            return;
        mPaint.setStrokeWidth(mColorPanelHeight);
        requestLayout();
    }

    public int[] getColors() {
        return mColors;
    }

    public void setColors(int... colors) {
        this.mColors = colors;
        requestLayout();
    }

    public void setColors(List<Integer> colors) {
        if (null == colors || 0 == colors.size())
            return;
        mColors = new int[colors.size()];
        for (int i = 0, count = colors.size(); i < count; i++) {
            mColors[i] = colors.get(i);
        }
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //Set up gradient rendering
        if (null == mPaint.getShader()) {
            Shader s = isHorizontal()
                    ? new LinearGradient(mOffset, 0, getMeasuredWidth() - mOffset, 0, mColors, null, Shader.TileMode.CLAMP)
                    : new LinearGradient(0, mOffset, 0, getMeasuredHeight() - mOffset, mColors, null, Shader.TileMode.CLAMP);
            mPaint.setShader(s);
        }

        //Draw a gradient color bar
        RectF rect;
        if (isHorizontal()) {
            float top = (getMeasuredHeight() - mColorPanelHeight) / 2;
            rect = new RectF(mOffset, top, getMeasuredWidth() - mOffset, top+mColorPanelHeight);
        } else {
            float left = (getMeasuredWidth() - mColorPanelHeight) / 2;
            rect = new RectF(left, mOffset, left+mColorPanelHeight, getMeasuredHeight() - mOffset);
        }
        canvas.drawRoundRect(rect, mRoundCorner,mRoundCorner,mPaint);
        //Draw thumb
        if (mThumb != null) {
            canvas.save();

            if (isHorizontal()) {
                canvas.translate(mPaddingLeft + mThumbOffset, mPaddingTop);
            } else {
                canvas.translate(mPaddingLeft, mPaddingTop + mThumbOffset);
            }
            mThumb.draw(canvas);
            canvas.restore();
        }

        //callback current color
        measureProgress();
        if (null != mSelectListener) {
            mSelectListener.onColorSelect(getCurrentColor(isHorizontal() ? lastX : lastY), mCurrentProgress);
        }
    }

    /**
     * Set progress value
     *
     * @param progress
     */
    public void setProgress(final int progress) {
        post(new Runnable() {
            @Override
            public void run() {
                if (isHorizontal()) {
                    mThumbOffset = (progress * (getMeasuredWidth() - 2 * mOffset)) / MAX_PROGRESS;
                    lastX = mThumbOffset + mOffset;
                } else {
                    mThumbOffset = (progress * (getMeasuredHeight() - 2 * mOffset)) / MAX_PROGRESS;
                    lastY = mThumbOffset + mOffset;
                }

                invalidate();
            }
        });
    }

    /**
     * Set progress bar by color
     *
     * @param color
     */
    public void setProgressByColor(final int color) {
        post(new Runnable() {
            @Override
            public void run() {

                measureProgressFromColor(color);
            }
        });
    }

    /**
     * Calculate progress by color<br/>
     * TODO For the time being, I only think of this stupid way, hoping there is a better way
     *
     * @param color
     */
    private void measureProgressFromColor(int color) {
        int xCount = isHorizontal() ? getMeasuredWidth() : getMeasuredHeight();
        for (int colorPoint = 0; colorPoint < xCount - 2 * mOffset; colorPoint++) {
            int currentColor = getCurrentColor(colorPoint);
            int offset = Math.abs(currentColor - color);
            Log.d(getClass().getSimpleName(), "==============>>>" + offset);
            if (offset <= 260) {
                if (isHorizontal()) {
                    lastX = colorPoint;
                    mThumbOffset = lastX - mOffset;
                } else {
                    lastY = colorPoint;
                    mThumbOffset = lastY - mOffset;
                }
                measureProgress();
                setProgress(mCurrentProgress);
                break;
            }
        }

    }

    /**
     * Calculate current progress
     */
    private void measureProgress() {
        int lineWidth = isHorizontal() ? getMeasuredWidth() : getMeasuredHeight();
        mCurrentProgress = (int) ((mThumbOffset / (lineWidth - 2 * mOffset)) * MAX_PROGRESS);
    }

    /**
     * Get the current progress value
     *
     * @return
     */
    public int getCurrentProgress() {

        return mCurrentProgress;
    }

    private float lastX = 0;
    private float lastY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                return isHorizontal() ? handleBoundHorizontal(event) : handleBoundVertical(event);
        }
        return super.onTouchEvent(event);
    }


    /**
     * Processing vertical boundary value
     *
     * @param event
     */
    private boolean handleBoundHorizontal(MotionEvent event) {
        mThumbOffset += (event.getX() - lastX);
        float colorMaxWidth = getMeasuredWidth() - 2 * mOffset;
        if (mThumbOffset <= 0) {
            mThumbOffset = 0;
            lastX = mOffset;
            invalidate();
            return true;
        }
        if (mThumbOffset > colorMaxWidth) {
            mThumbOffset = colorMaxWidth;
            lastX = getMeasuredWidth() - mOffset;
            invalidate();
            return true;
        }
        lastX = event.getX();
        invalidate();
        return true;
    }

    /**
     * Processing horizontal boundary value
     *
     * @param event
     */
    private boolean handleBoundVertical(MotionEvent event) {
        mThumbOffset += (event.getY() - lastY);
        if (mThumbOffset <= 0) {
            mThumbOffset = 0;
            lastY = mOffset;
            invalidate();
            return true;
        }
        if (mThumbOffset > getMeasuredHeight() - 2 * mOffset) {
            mThumbOffset = getMeasuredHeight() - 2 * mOffset;
            lastY = getMeasuredHeight() - mOffset;
            invalidate();
            return true;
        }
        lastY = event.getY();
        invalidate();
        return true;
    }

    private int ave(int startColor, int endColor, int unit, int step) {
        return startColor + (endColor - startColor) * step / unit;
    }

    /**
     * Get the current color
     *
     * @return
     */
    private int getCurrentColor(float colorPoint) {
        int unit = (int) ((isHorizontal() ? getMeasuredWidth() - 2 * mOffset : getMeasuredHeight() - 2 * mOffset) / (mColors.length - 1));
        int position = (int) (colorPoint - mOffset);
        int i = position / unit;
        int step = position % unit;
        if (i >= mColors.length - 1)
            return mColors[mColors.length - 1];
        int c0 = mColors[i];
        int c1 = mColors[i + 1];
        int a = ave(Color.alpha(c0), Color.alpha(c1), unit, step);
        int r = ave(Color.red(c0), Color.red(c1), unit, step);
        int g = ave(Color.green(c0), Color.green(c1), unit, step);
        int b = ave(Color.blue(c0), Color.blue(c1), unit, step);
        return Color.argb(a, r, g, b);
    }


    /**
     * Determine if the current direction is horizontal
     *
     * @return
     */
    private boolean isHorizontal() {
        return HORIZONTAL == mOrientation;
    }

    private OnColorSelectListener mSelectListener;

    /**
     * set callback
     *
     * @param mSelectListener
     */
    public void setOnColorSelectListener(OnColorSelectListener mSelectListener) {
        this.mSelectListener = mSelectListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 计算宽高
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    /**
     * measure width
     *
     * @param measureSpec
     * @return width
     */
    private int measureWidth(int measureSpec) {

        int specSize = MeasureSpec.getSize(measureSpec);

        int width = null == mThumb ? mDefaultHeight : mThumb.getIntrinsicWidth();
        return isHorizontal() ? specSize : width + getPaddingLeft() + getPaddingRight();
    }

    /**
     * measure height
     *
     * @param measureSpec
     * @return height
     */
    private int measureHeight(int measureSpec) {
        int specSize = MeasureSpec.getSize(measureSpec);
        int width = null == mThumb ? mDefaultHeight : mThumb.getIntrinsicWidth();
        return isHorizontal() ? width + getPaddingTop() + getPaddingBottom() : specSize;
    }

    public interface OnColorSelectListener {
        void onColorSelect(int color, int progress);
    }
}
