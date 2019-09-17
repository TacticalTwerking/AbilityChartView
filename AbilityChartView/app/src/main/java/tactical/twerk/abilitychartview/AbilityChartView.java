package tactical.twerk.abilitychartview;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.text.DecimalFormat;

/**
 * Created by TacticalTwerking on 16/6/23.
 * GitHub   https://github.com/TacticalTwerking
 */
public class AbilityChartView extends View {


    private static final long ANIMATION_DURATION = 800L;
    private static final long ANIMATION_DELAY = 50L;
    private int mSize = -1;
    private int mCirclePadding = -1;
    private int mViewCenter = -1;
    private int mCircleStrokeWidth = 4;
    private int mSides = 5;
    private int mActuallyRadius = -1;
    //private int mHighLightPadding = 5;
    private int mRotateOffset = 90;
    //private float mPieces = 0;
    private int mLabelTxtSize = -1;
    private int mLabelTxtColor;
    private float mMinimalValuesPercentage = -1;
    private float[] mProgressValues;
    private float[] mLatestProgressValues;
    private float[] mAnimationProgress;
    private String[] mLabels = new String[]{};
    private Paint mPaintCircle;
    private Paint mPaintPolygon;
    //private Paint mPaintArcs;
    private Paint mPaintGrid;
    private Paint mPaintLabels;
    private Paint mPaintAvatar;
    private int mCenterImageResId = -1;
    private boolean mAnimationRunning = false;
    private boolean mShowGrid;
    private Bitmap mBitmapAvatar;

    public AbilityChartView(Context context) {
        super(context);
        init(context,null);
    }

    public AbilityChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AbilityChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AbilityChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void initial(int side,float []values){
        if (mAnimationRunning){
            return;
        }
        mLatestProgressValues = mSides == side?mProgressValues:null;
        mSides = side;
        mProgressValues = values;
        //mPieces = mSides > 0 ? 360f / mSides : 0;
    }

    public void initial(int side,float []values,String []labels){
        if (mAnimationRunning){
            return;
        }
        mLatestProgressValues = mSides == side?mProgressValues:null;
        mSides = side;
        mProgressValues = values;
        mLabels = labels;
        //mPieces = mSides > 0 ? 360f / mSides : 0;
    }


    public void animateProgress() {
        if (mAnimationRunning) {
            return;
        }
        mAnimationProgress = null!=mLatestProgressValues?mLatestProgressValues:new float[mSides];
        for (int i = 0; i < mSides; i++) {
            doAnimate(i);
        }
    }


    private void doAnimate( final int progress) {
        mAnimationRunning = true;
        if (null==mLatestProgressValues){
            mLatestProgressValues = new float[mSides];
           mLatestProgressValues[progress] = 0;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(mLatestProgressValues[progress],mProgressValues[progress]);
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimationProgress[progress] = (float) animation.getAnimatedValue();

                invalidate();
            }
        });
        animator.setStartDelay(ANIMATION_DELAY * progress);
        if (progress== mSides -1){
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @Override
                public void onAnimationEnd(Animator animation) {
                    mAnimationRunning = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mAnimationRunning = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
        }
        animator.start();
    }

    private void init(Context context,AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AbilityChartView, 0, 0);

        int colorGrid = a.getColor(R.styleable.AbilityChartView_acv_grid_color,getResources().getColor(R.color.defaultGridColor));
        int colorPolygon = a.getColor(R.styleable.AbilityChartView_acv_polygon_color,getResources().getColor(R.color.defaultPolygonColor));
        int colorCircle = a.getColor(R.styleable.AbilityChartView_acv_circle_color,getResources().getColor(R.color.defaultCircleColor));
        float circleWidth = a.getDimensionPixelSize(R.styleable.AbilityChartView_acv_circle_width,2);
        float gridWidth = a.getDimensionPixelSize(R.styleable.AbilityChartView_acv_grid_width,1);
        mMinimalValuesPercentage = a.getFraction(R.styleable.AbilityChartView_acv_minimal_value_percentage,1,1,0.4f);
        mCenterImageResId = a.getResourceId(R.styleable.AbilityChartView_acv_center_image,R.mipmap.ic_launcher);
        mLabelTxtSize = a.getDimensionPixelSize(R.styleable.AbilityChartView_acv_label_txt_size,-1);
        mLabelTxtColor = a.getColor(R.styleable.AbilityChartView_acv_label_txt_color,getResources().getColor(android.R.color.black));
        mShowGrid = a.getBoolean(R.styleable.AbilityChartView_acv_show_grid,true);
        //do something with str

        a.recycle();


        mPaintCircle = new Paint();
        mPaintCircle.setStrokeWidth(mCircleStrokeWidth);
        mPaintCircle.setColor(colorCircle);
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStrokeCap(Paint.Cap.ROUND);
        mPaintCircle.setShader(null);
        mPaintCircle.setStyle(Paint.Style.STROKE);

        mPaintPolygon = new Paint(mPaintCircle);
        mPaintPolygon.setColor(colorPolygon);
        mPaintPolygon.setStrokeWidth(circleWidth);
        mPaintPolygon.setStyle(Paint.Style.FILL);
        //
        //mPaintArcs = new Paint(mPaintCircle);
        //mPaintArcs.setColor(Color.parseColor("#88b6a9"));
        //mPaintArcs.setStrokeCap(Paint.Cap.SQUARE);
        //mPaintArcs.setStrokeWidth(5);

        mPaintGrid = new Paint(mPaintCircle);
        mPaintGrid.setStrokeWidth(gridWidth);
        mPaintGrid.setColor(colorGrid);


        mPaintAvatar = new Paint();
        mPaintAvatar.setAntiAlias(true);
        mPaintAvatar.setColor(Color.WHITE);
        mPaintAvatar.setStyle(Paint.Style.FILL);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initProperties();
        drawCircle(canvas);

        for (int i = 0; i < mSides; i++) {
            double angle = ((Math.PI * 2 / mSides) * i) - (Math.toRadians(mRotateOffset));
            if (mShowGrid){
                drawInnerLines(canvas,angle);
            }
            drawLabels(canvas,i,angle);
        }

        drawPolygon(canvas);
        drawAvatar(canvas);
    }


    private void drawAvatar(Canvas canvas) {

        mPaintAvatar.setShadowLayer(10,10,10,android.R.color.black);
        canvas.drawCircle(mViewCenter, mViewCenter, mActuallyRadius * mMinimalValuesPercentage * .6f, mPaintAvatar);
        canvas.drawBitmap(mBitmapAvatar, mViewCenter - (mBitmapAvatar.getWidth() / 2), mViewCenter - (mBitmapAvatar.getHeight() / 2), mPaintGrid);


    }


    //private void drawArcs(Canvas canvas,int i) {
    //
    //    List<Integer> maxValues = getMaxValues(mProgressValues);
    //    RectF rectF = new RectF(mCirclePadding, mCirclePadding, (mActuallyRadius * 2) + mCirclePadding, (mActuallyRadius * 2) + mCirclePadding);
    //    if (maxValues.size()>i){
    //        int sweepAngle = (int) (mPieces * mAnimationProgress[maxValues.get(i)]);
    //        int startAngle = (int) ((maxValues.get(i) * mPieces) - (sweepAngle / 2) - mRotateOffset);
    //        if (mSides == 0) {
    //            mHighLightPadding = 0;
    //        }
    //
    //        if (sweepAngle != 0) {
    //            canvas.drawArc(rectF, startAngle + mHighLightPadding, sweepAngle - mHighLightPadding, false, mPaintArcs);
    //        }
    //    }
    //}

    private void drawLabels(Canvas canvas,int i,double angle) {
        if (null == mLabels || mLabels.length != mSides) {
            return;
        }

        int maxLength = mActuallyRadius;

        String strNumericValues = new DecimalFormat("##").format(mAnimationProgress[i] * 100);

        Rect textBoundLabel = new Rect();
        Rect textBoundNumeric = new Rect();

        mPaintLabels.getTextBounds(mLabels[i], 0, mLabels[i].length(), textBoundLabel);
        mPaintLabels.getTextBounds(strNumericValues, 0, strNumericValues.length(), textBoundNumeric);

        float actuallyValues = maxLength + textBoundLabel.width();

        float x = (int) (Math.cos(angle) * actuallyValues + mViewCenter);
        float y = (int) (Math.sin(angle) * actuallyValues + mViewCenter);

        //Draw Label
        canvas.drawText(mLabels[i], x - (textBoundLabel.width() / 2f), y + (textBoundLabel.height() / 2f), mPaintLabels);
        //Draw Progress Value
        canvas.drawText(strNumericValues, x - (textBoundNumeric.width() / 2f), y - (textBoundLabel.height() / 2f), mPaintLabels);

    }

    private void drawInnerLines(Canvas canvas,double angle) {
        float actuallyValues = mActuallyRadius;
        float x = (int) (Math.cos(angle) * actuallyValues + mViewCenter);
        float y = (int) (Math.sin(angle) * actuallyValues + mViewCenter);
        canvas.drawLine( mViewCenter, mViewCenter,x,y, mPaintGrid);
    }


    private void drawPolygon(Canvas canvas) {

        Path path = new Path();
        float minimalValues = 0;
        float startX = 0;
        float startY = 0;
        int maxHill = mActuallyRadius - mCircleStrokeWidth * 2;

        if (mMinimalValuesPercentage > 0) {
            minimalValues = maxHill * mMinimalValuesPercentage;
        }
        for (int i = 0; i < mSides; i++) {

            float actuallyValues = maxHill * mAnimationProgress[i];
            if (minimalValues > 0) {
                actuallyValues = minimalValues + (maxHill - minimalValues)  * mAnimationProgress[i];
            }

            double angle = ((Math.PI * 2 / mSides) * i) - (Math.toRadians(mRotateOffset));

            float x = (int) (Math.cos(angle) * actuallyValues + mViewCenter);
            float y = (int) (Math.sin(angle) * actuallyValues + mViewCenter);

            if (i == 0) {
                startX = x;
                startY = y;
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.lineTo(startX, startY);
        canvas.drawPath(path, mPaintPolygon);
    }

    private void drawCircle(Canvas canvas) {
        //Outside circle
        canvas.drawCircle(mViewCenter, mViewCenter, mActuallyRadius, mPaintCircle);
        //Inside circle
        canvas.drawCircle(mViewCenter, mViewCenter, mActuallyRadius*mMinimalValuesPercentage, mPaintGrid);
    }

    private void initProperties() {
        if (mSize == -1) {
            mSize = Math.min(getHeight(), getWidth());
            mCirclePadding = mSize / 6;
            mViewCenter = mSize / 2;
            mActuallyRadius = mViewCenter - mCirclePadding;
            mProgressValues = new float[mSides];
            mAnimationProgress = new float[mSides];

            mPaintLabels = new Paint(mPaintGrid);
            mPaintLabels.setTextSize(mLabelTxtSize == -1?mSize / 30f:mLabelTxtSize);
            mPaintLabels.setColor(mLabelTxtColor==-1?Color.BLACK:mLabelTxtColor);
            mPaintLabels.setStyle(Paint.Style.FILL);
            mPaintLabels.setAntiAlias(true);
            //mPieces = mSides > 0 ? 360f / mSides : 0;

            mBitmapAvatar = BitmapFactory.decodeResource(getResources(),mCenterImageResId);

        }
    }
    //
    //private List<Integer> getMaxValues(float[] source) {
    //    List<Integer> maxArrayIndex = new ArrayList<>();
    //    float maxValue = source[0];
    //    int maxValuesIndex = 0;
    //    boolean notUnique = false;
    //    for (int i = 1; i < source.length; i++) {
    //        if (maxValue < source[i]) {
    //            maxValue = source[i];
    //            maxValuesIndex = i;
    //        } else if (maxValue == source[i]) {
    //            notUnique = true;
    //        }
    //    }
    //    if (notUnique) {
    //        for (int i = 0; i < source.length; i++) {
    //            if (maxValue == source[i]) {
    //                maxArrayIndex.add(i);
    //            }
    //        }
    //    } else {
    //        maxArrayIndex.add(maxValuesIndex);
    //    }
    //    return maxArrayIndex;
    //}
}
