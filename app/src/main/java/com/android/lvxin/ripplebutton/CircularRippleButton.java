package com.android.lvxin.ripplebutton;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

/**
 * @ClassName: RippleView
 * @Description: TODO
 * @Author: lvxin
 * @Date: 4/19/16 17:47
 */
public class CircularRippleButton extends View {
    float cx;
    float cy;
    float radiusOuter;
    float radiusInner;
    int mWidth;
    private int mScreenWidth;
    private int mScreenHeight;
    private Paint mRipplePaint = new Paint();
    private boolean isStartRipple;
    private int heightPaddingTop;
    private int heightPaddingBottom;
    private int widthPaddingLeft;
    private int widthPaddingRight;
    private int rippleFirstRadius = 0;
    private int rippleSecondRadius = -33;
    private Paint textPaint = new Paint();
    private int rippleWidth = dip2px(3);
    private int rippleColor = Color.WHITE;
    private int textResId;
    private int textSize;
    private int textColor;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
            if (isStartRipple) {
                rippleFirstRadius++;
                if (rippleFirstRadius > 100) {
                    rippleFirstRadius = 0;
                }
                rippleSecondRadius++;
                if (rippleSecondRadius > 100) {
                    rippleSecondRadius = 0;
                }
                sendEmptyMessageDelayed(0, 10);
            }
        }
    };

    /**
     * @param context
     * @param attrs
     */
    public CircularRippleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CircularRippleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        initPaint();

    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularRippleButton, 0, 0);
        //Reading values from the XML layout
        try {
            rippleWidth = typedArray.getDimensionPixelSize(R.styleable.CircularRippleButton_crb_ripple_width, rippleWidth);
            rippleColor = typedArray.getColor(R.styleable.CircularRippleButton_crb_ripple_color, rippleColor);

            textSize = typedArray.getDimensionPixelSize(R.styleable.CircularRippleButton_crb_text_size, textSize);
            textResId = typedArray.getResourceId(R.styleable.CircularRippleButton_crb_text, textResId);
            textColor = typedArray.getColor(R.styleable.CircularRippleButton_crb_text_color, textColor);
        } finally {
            typedArray.recycle();
        }
    }

    private void initPaint() {
        mRipplePaint.setColor(rippleColor);
        mRipplePaint.setAntiAlias(true);
        mRipplePaint.setStyle(Paint.Style.FILL);
        mRipplePaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mh = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int mw = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int min = Math.min(mh, mw);
        cx = min / 2;
        cy = min / 2;
        radiusInner = min / 4;
        radiusOuter = radiusInner + 15;
        mWidth = min;
        setMeasuredDimension(min, min);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawRipple(canvas);
        drawInnerCircle(canvas);
        drawText(canvas);

    }

    /**
     * 绘制圆形按钮的背景
     *
     * @param canvas
     */
    private void drawInnerCircle(Canvas canvas) {
        mRipplePaint.setStyle(Paint.Style.FILL);
        mRipplePaint.setAlpha(100);
        canvas.drawCircle(cx, cy, radiusOuter, mRipplePaint);
        mRipplePaint.setAlpha(255);
        canvas.drawCircle(cx, cy, radiusInner, mRipplePaint);
    }

    /**
     * 绘制波纹
     *
     * @param canvas
     */
    private void drawRipple(Canvas canvas) {
        if (isStartRipple) {
            float f1 = 3 * mWidth / 20;
            mRipplePaint.setAlpha(100);
            mRipplePaint.setStyle(Paint.Style.STROKE);
            mRipplePaint.setStrokeWidth(dip2px(3));
//            canvas.drawCircle(cx, cy, radiusOuter, mRipplePaint);
            int i1 = (int) (100.0F - (100.0F - 0.0F) / 100.0F * rippleFirstRadius);
            mRipplePaint.setAlpha(i1);
            canvas.drawCircle(cx, cy, radiusOuter + 2 + f1 * rippleFirstRadius / 100.0F, mRipplePaint);
            if (rippleSecondRadius >= 0) {
                int i3 = (int) (100.0F - (100.0F - 0.0F) / 100.0F * rippleSecondRadius);
                mRipplePaint.setAlpha(i3);
                canvas.drawCircle(cx, cy, radiusOuter + 2 + f1 * rippleSecondRadius / 100.0F, mRipplePaint);
            }
        }
    }

    /**
     * 绘制里面的文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        try {
            String text = getResources().getString(textResId);
            float textLength = textPaint.measureText(text);
            Paint.FontMetrics fm = textPaint.getFontMetrics();
            int textHeight = (int) Math.ceil(fm.descent - fm.ascent);
            canvas.drawText(text, (mWidth - textLength) / 2, mWidth / 2 + textHeight / 4, textPaint);
//            if (4 == text.length()) {
//                String text1 = text.substring(0, 2);
//                String text2 = text.substring(2, text.length());
//                float length1 = textPaint.measureText(text1);
//                float length2 = textPaint.measureText(text2);
//                Paint.FontMetrics fm = textPaint.getFontMetrics();
//                int textHeight = (int) Math.ceil(fm.descent - fm.ascent);
//                canvas.drawText(text1, (mWidth - length1) / 2, mWidth / 2 - 10, textPaint);
//                canvas.drawText(text2, (mWidth - length2) / 2, mWidth / 2 + textHeight - 2, textPaint);
//            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenWidth = w;
        mScreenHeight = h;
        confirmSize();
        invalidate();
    }

    private void confirmSize() {
        int minScreenSize = Math.min(mScreenWidth, mScreenHeight);
        int widthOverSize = mScreenWidth - minScreenSize;
        int heightOverSize = mScreenHeight - minScreenSize;
        heightPaddingTop = (getPaddingTop() + heightOverSize / 2);
        heightPaddingBottom = (getPaddingBottom() + heightOverSize / 2);
        widthPaddingLeft = (getPaddingLeft() + widthOverSize / 2);
        widthPaddingRight = (getPaddingRight() + widthOverSize / 2);

    }

    /**
     * 开始
     */
    public void startRipple() {
        if (!isStartRipple) {
            isStartRipple = true;
            handler.sendEmptyMessage(0);
        }
    }

    /**
     * 停止
     */
    public void stopRipple() {
        if (isStartRipple) {
            isStartRipple = false;
            handler.removeMessages(0);
            invalidate();
        }
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setTextColor(int color) {
        if (textPaint != null) {
            textPaint.setColor(color);
        }
        invalidate();
    }
}
