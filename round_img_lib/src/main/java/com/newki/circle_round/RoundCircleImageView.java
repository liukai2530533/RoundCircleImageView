package com.newki.circle_round;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;


/**
 * 只能支持四周固定的圆角，或者圆形的图片
 * 支持设置自定义圆角或圆形背景颜色，（需要使用当前类提供的背景颜色方法）
 * <p>
 * 支持四个角各自定义角度
 */
public class RoundCircleImageView extends AppCompatImageView {

    private int mRoundRadius = 0;
    private boolean isCircleType;
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 2;

    private final RectF mDrawableRect = new RectF();
    private final Matrix mShaderMatrix = new Matrix();

    private final Paint mBitmapPaint = new Paint();
    private final Paint mRoundBackgroundPaint = new Paint();

    private Drawable mRoundBackgroundDrawable;

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBitmapHeight;

    private Bitmap mBackgroundBitmap;
    private BitmapShader mBackgroundBitmapShader;

    private ColorFilter mColorFilter;
    private float mDrawableRadius;
    private boolean mReady;
    private boolean mSetupPending;
    private float mTopLeft;
    private float mTopRight;
    private float mBottomLeft;
    private float mBottomRight;


    public RoundCircleImageView(Context context) {
        super(context);
        init();
    }

    public RoundCircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        //读取配置属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundCircleImageView);
        mRoundRadius = array.getDimensionPixelOffset(R.styleable.RoundCircleImageView_round_radius, 0);
        mTopLeft = array.getDimensionPixelOffset(R.styleable.RoundCircleImageView_topLeft, 0);
        mTopRight = array.getDimensionPixelOffset(R.styleable.RoundCircleImageView_topRight, 0);
        mBottomLeft = array.getDimensionPixelOffset(R.styleable.RoundCircleImageView_bottomLeft, 0);
        mBottomRight = array.getDimensionPixelOffset(R.styleable.RoundCircleImageView_bottomRight, 0);

        if (array.hasValue(R.styleable.RoundCircleImageView_round_background_color)) {
            int roundBackgroundColor = array.getColor(R.styleable.RoundCircleImageView_round_background_color, Color.TRANSPARENT);
            mRoundBackgroundDrawable = new ColorDrawable(roundBackgroundColor);
            mBackgroundBitmap = getBitmapFromDrawable(mRoundBackgroundDrawable);
        }

        if (array.hasValue(R.styleable.RoundCircleImageView_round_background_drawable)) {
            mRoundBackgroundDrawable = array.getDrawable(R.styleable.RoundCircleImageView_round_background_drawable);
            mBackgroundBitmap = getBitmapFromDrawable(mRoundBackgroundDrawable);
        }

        isCircleType = array.getBoolean(R.styleable.RoundCircleImageView_isCircle, false);

        array.recycle();

        init();
    }

    private void init() {
        super.setScaleType(SCALE_TYPE);
        mReady = true;

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException("已经自带设置了，你无需再设置了");
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("已经自带设置了，你无需再设置了");
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        if (mBitmap == null && mBackgroundBitmap == null) {
            return;
        }

        if (isCircleType) {

            if (mRoundBackgroundDrawable != null && mBackgroundBitmap != null) {
                canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius, mRoundBackgroundPaint);
            }

            if (mBitmap != null) {
                canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius, mBitmapPaint);
            }

        } else {

            if (mTopLeft > 0 || mTopRight > 0 || mBottomLeft > 0 || mBottomRight > 0) {
                //使用单独的圆角
                if (mRoundBackgroundDrawable != null && mBackgroundBitmap != null) {
                    Path path = new Path();
                    path.addRoundRect(
                            mDrawableRect,
                            new float[]{mTopLeft, mTopLeft, mTopRight, mTopRight, mBottomRight, mBottomRight, mBottomLeft, mBottomLeft},
                            Path.Direction.CW);
                    canvas.drawPath(path, mRoundBackgroundPaint);
                }

                if (mBitmap != null) {
                    Path path = new Path();
                    path.addRoundRect(
                            mDrawableRect,
                            new float[]{mTopLeft, mTopLeft, mTopRight, mTopRight, mBottomRight, mBottomRight, mBottomLeft, mBottomLeft},
                            Path.Direction.CW);
                    canvas.drawPath(path, mBitmapPaint);
                }


            } else {
                //使用统一的圆角
                if (mRoundBackgroundDrawable != null && mBackgroundBitmap != null) {
                    canvas.drawRoundRect(mDrawableRect, mRoundRadius, mRoundRadius, mRoundBackgroundPaint);
                }

                if (mBitmap != null) {
                    canvas.drawRoundRect(mDrawableRect, mRoundRadius, mRoundRadius, mBitmapPaint);
                }
            }

        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        setup();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        setup();
    }

    @Override
    public void setBackgroundColor(int color) {
        setRoundBackgroundColor(color);
    }

    @Override
    public void setBackground(Drawable background) {
        setRoundBackgroundDrawable(background);
    }

    @Override
    public void setBackgroundDrawable(@Nullable Drawable background) {
        setRoundBackgroundDrawable(background);
    }

    @Override
    public void setBackgroundResource(int resId) {
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable drawable = getContext().getResources().getDrawable(resId);
        setRoundBackgroundDrawable(drawable);
    }

    @Override
    public Drawable getBackground() {
        return getRoundBackgroundDrawable();
    }

    public void setRoundBackgroundColor(@ColorInt int roundBackgroundColor) {
        ColorDrawable drawable = new ColorDrawable(roundBackgroundColor);
        setRoundBackgroundDrawable(drawable);
    }

    public void setRoundBackgroundColorResource(@ColorRes int circleBackgroundRes) {
        setRoundBackgroundColor(getContext().getResources().getColor(circleBackgroundRes));
    }

    public Drawable getRoundBackgroundDrawable() {
        return mRoundBackgroundDrawable;
    }

    public void setRoundBackgroundDrawable(Drawable drawable) {
        mRoundBackgroundDrawable = drawable;
        initializeBitmap();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (cf == mColorFilter) {
            return;
        }

        mColorFilter = cf;
        applyColorFilter();
        invalidate();
    }

    @Override
    public ColorFilter getColorFilter() {
        return mColorFilter;
    }

    private void applyColorFilter() {
        if (mBitmapPaint != null) {
            mBitmapPaint.setColorFilter(mColorFilter);
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initializeBitmap() {

        mBitmap = getBitmapFromDrawable(getDrawable());

        if (mRoundBackgroundDrawable != null) {
            mBackgroundBitmap = getBitmapFromDrawable(mRoundBackgroundDrawable);
        }

        setup();
    }

    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (getWidth() == 0 && getHeight() == 0) {
            return;
        }

        if (mBitmap == null && mBackgroundBitmap == null) {
            invalidate();
            return;
        }

        if (mBitmap != null) {
            mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mBitmapPaint.setAntiAlias(true);
            mBitmapPaint.setShader(mBitmapShader);
        }

        if (mRoundBackgroundDrawable != null && mBackgroundBitmap != null) {
            mBackgroundBitmapShader = new BitmapShader(mBackgroundBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mRoundBackgroundPaint.setAntiAlias(true);
            mRoundBackgroundPaint.setShader(mBackgroundBitmapShader);
        }

        Bitmap bitmap = mBitmap != null ? mBitmap : mBackgroundBitmap;
        mBitmapHeight = bitmap.getHeight();
        mBitmapWidth = bitmap.getWidth();

        mDrawableRect.set(calculateBounds());
        mDrawableRadius = Math.min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f);

        applyColorFilter();
        updateShaderMatrix();
        //重绘
        invalidate();
    }

    private RectF calculateBounds() {
        int availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        int sideLength = Math.min(availableWidth, availableHeight);

        float left = getPaddingLeft() + (availableWidth - sideLength) / 2f;
        float top = getPaddingTop() + (availableHeight - sideLength) / 2f;

        return new RectF(left, top, left + sideLength, top + sideLength);
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);

        if (mBitmapShader != null) {
            mBitmapShader.setLocalMatrix(mShaderMatrix);
        }
        if (mBackgroundBitmapShader != null) {
            mBackgroundBitmapShader.setLocalMatrix(mShaderMatrix);
        }
    }

}
