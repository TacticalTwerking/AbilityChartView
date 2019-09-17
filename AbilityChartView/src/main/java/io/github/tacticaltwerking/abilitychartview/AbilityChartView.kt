package io.github.tacticaltwerking.abilitychartview


import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

import java.text.DecimalFormat

/**
 * Created by TacticalTwerking on 16/6/23.
 * GitHub   https://github.com/TacticalTwerking
 */
class AbilityChartView : View {


    private var mAnimationDuration: Long = 0
    private var mAnimationDelay:Long = 50L
    private var mSize = -1
    private var mCirclePadding = -1
    private var mViewCenter = -1
    private val mCircleStrokeWidth = 4
    private var mSides = 5
    private var mActuallyRadius = -1
    //private int mHighLightPadding = 5;
    private val mRotateOffset = 90
    //private float mPieces = 0;
    private var mLabelTxtSize = -1f
    private var mLabelTxtColor: Int = 0
    private var mMinimalValuesPercentage = -1f
    private var mProgressValues: FloatArray? = null
    private var mLatestProgressValues: FloatArray? = null
    private var mAnimationProgress: FloatArray? = null
    private var mLabels: Array<String?>? = arrayOf()
    private var mPaintCircle: Paint? = null
    private var mPaintPolygon: Paint? = null
    //private Paint mPaintArcs;
    private var mPaintGrid: Paint? = null
    private var mPaintLabels: Paint? = null
    private var mPaintAvatar: Paint? = null
    private var mCenterImageResId = -1
    private var mAnimationRunning = false
    private var mShowGrid: Boolean = false
    private var mBitmapAvatar: Bitmap? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(context, attrs)
    }

    fun initial(side: Int, values: FloatArray) {
        if (mAnimationRunning) {
            return
        }
        mLatestProgressValues = if (mSides == side) mProgressValues else null
        mSides = side
        mProgressValues = values
        //mPieces = mSides > 0 ? 360f / mSides : 0;
    }

    fun initial(side: Int, values: FloatArray, labels: Array<String?>) {
        if (mAnimationRunning) {
            return
        }
        mLatestProgressValues = if (mSides == side) mProgressValues else null
        mSides = side
        mProgressValues = values
        mLabels = labels
        //mPieces = mSides > 0 ? 360f / mSides : 0;
    }


    fun animateProgress() {
        if (mAnimationRunning) {
            return
        }
        mAnimationProgress =
            if (null != mLatestProgressValues) mLatestProgressValues else FloatArray(mSides)
        for (i in 0 until mSides) {
            doAnimate(i)
        }
    }


    private fun doAnimate(progress: Int) {
        mAnimationRunning = true
        if (null == mLatestProgressValues) {
            mLatestProgressValues = FloatArray(mSides)
            mLatestProgressValues!![progress] = 0f
        }
        val animator =
            ValueAnimator.ofFloat(mLatestProgressValues!![progress], mProgressValues!![progress])
        animator.duration = mAnimationDuration
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener { animation ->
            mAnimationProgress?.set(progress, animation.animatedValue as Float)

            invalidate()
        }
        animator.startDelay = mAnimationDelay * progress
        if (progress == mSides - 1) {
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    mAnimationRunning = false
                }

                override fun onAnimationCancel(animation: Animator) {
                    mAnimationRunning = false
                }

                override fun onAnimationRepeat(animation: Animator) {}
            })
        }
        animator.start()
    }

    private fun init(context: Context, attrs: AttributeSet?) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.AbilityChartView, 0, 0)

        val colorGrid = a.getColor(
            R.styleable.AbilityChartView_acv_grid_color,
            resources.getColor(R.color.defaultGridColor,context.theme)
        )
        val colorPolygon = a.getColor(
            R.styleable.AbilityChartView_acv_polygon_color,
            resources.getColor(R.color.defaultPolygonColor,context.theme)
        )
        val colorCircle = a.getColor(
            R.styleable.AbilityChartView_acv_circle_color,
            resources.getColor(R.color.defaultCircleColor,context.theme)
        )

        mAnimationDelay =
            a.getInt(R.styleable.AbilityChartView_acv_animation_delay, 50).toLong()
        mAnimationDuration =
            a.getInt(R.styleable.AbilityChartView_acv_animation_duration, 800).toLong()
        val circleWidth =
            a.getDimensionPixelSize(R.styleable.AbilityChartView_acv_circle_width, 2).toFloat()
        val gridWidth =
            a.getDimensionPixelSize(R.styleable.AbilityChartView_acv_grid_width, 1).toFloat()
        mMinimalValuesPercentage =
            a.getFraction(R.styleable.AbilityChartView_acv_minimal_value_percentage, 1, 1, 0.4f)
        mCenterImageResId =
            a.getResourceId(R.styleable.AbilityChartView_acv_center_image, R.drawable.ic_launcher)
        mLabelTxtSize =
            a.getDimensionPixelSize(R.styleable.AbilityChartView_acv_label_txt_size, -1).toFloat()
        mLabelTxtColor = a.getColor(
            R.styleable.AbilityChartView_acv_label_txt_color,
            resources.getColor(android.R.color.black,context.theme)
        )
        mShowGrid = a.getBoolean(R.styleable.AbilityChartView_acv_show_grid, true)
        a.recycle()


        mPaintCircle = Paint()
        mPaintCircle!!.strokeWidth = mCircleStrokeWidth.toFloat()
        mPaintCircle!!.color = colorCircle
        mPaintCircle!!.isAntiAlias = true
        mPaintCircle!!.strokeCap = Paint.Cap.ROUND
        mPaintCircle!!.shader = null
        mPaintCircle!!.style = Paint.Style.STROKE

        mPaintPolygon = Paint(mPaintCircle)
        mPaintPolygon!!.color = colorPolygon
        mPaintPolygon!!.strokeWidth = circleWidth
        mPaintPolygon!!.style = Paint.Style.FILL

        mPaintGrid = Paint(mPaintCircle)
        mPaintGrid!!.strokeWidth = gridWidth
        mPaintGrid!!.color = colorGrid

        mPaintAvatar = Paint()
        mPaintAvatar!!.isAntiAlias = true
        mPaintAvatar!!.color = Color.WHITE
        mPaintAvatar!!.style = Paint.Style.FILL

        //mPaintArcs = new Paint(mPaintCircle);
        //mPaintArcs.setColor(Color.parseColor("#88b6a9"));
        //mPaintArcs.setStrokeCap(Paint.Cap.SQUARE);
        //mPaintArcs.setStrokeWidth(5);
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        initProperties()
        drawCircle(canvas)

        for (i in 0 until mSides) {
            val angle = Math.PI * 2 / mSides * i - Math.toRadians(mRotateOffset.toDouble())
            if (mShowGrid) {
                drawInnerLines(canvas, angle)
            }
            drawLabels(canvas, i, angle)
        }

        drawPolygon(canvas)
        drawAvatar(canvas)
    }


    private fun drawAvatar(canvas: Canvas) {

        mPaintAvatar!!.setShadowLayer(10f, 10f, 10f, android.R.color.black)

        canvas.drawCircle(
            mViewCenter.toFloat(), mViewCenter.toFloat(),
            mActuallyRadius.toFloat() * mMinimalValuesPercentage * .6f, mPaintAvatar!!
        )

        canvas.drawBitmap(
            mBitmapAvatar!!,
            (mViewCenter - mBitmapAvatar!!.width / 2).toFloat(),
            (mViewCenter - mBitmapAvatar!!.height / 2).toFloat(), mPaintGrid
        )
    }


    private fun drawLabels(canvas: Canvas, i: Int, angle: Double) {
        if (null == mLabels || mLabels!!.size != mSides) {
            return
        }

        val maxLength = mActuallyRadius

        val strNumericValues =
            DecimalFormat("##").format((mAnimationProgress!![i] * 100).toDouble())

        val textBoundLabel = Rect()
        val textBoundNumeric = Rect()

        mPaintLabels!!.getTextBounds(mLabels!![i], 0, mLabels!![i]!!.length, textBoundLabel)
        mPaintLabels!!.getTextBounds(strNumericValues, 0, strNumericValues.length, textBoundNumeric)

        val actuallyValues = (maxLength + textBoundLabel.width()).toFloat()

        val x = (Math.cos(angle) * actuallyValues + mViewCenter).toInt().toFloat()
        val y = (Math.sin(angle) * actuallyValues + mViewCenter).toInt().toFloat()

        //Draw Label
        canvas.drawText(
            mLabels!![i].toString(),
            x - textBoundLabel.width() / 2f, y + textBoundLabel.height() / 2f, mPaintLabels!!
        )
        //Draw Progress Value
        canvas.drawText(
            strNumericValues,
            x - textBoundNumeric.width() / 2f, y - textBoundLabel.height() / 2f, mPaintLabels!!
        )

    }

    private fun drawInnerLines(canvas: Canvas, angle: Double) {
        val actuallyValues = mActuallyRadius.toFloat()
        val x = (Math.cos(angle) * actuallyValues + mViewCenter).toInt().toFloat()
        val y = (Math.sin(angle) * actuallyValues + mViewCenter).toInt().toFloat()
        canvas.drawLine(mViewCenter.toFloat(), mViewCenter.toFloat(), x, y, mPaintGrid!!)
    }


    private fun drawPolygon(canvas: Canvas) {

        val path = Path()
        var minimalValues = 0f
        var startX = 0f
        var startY = 0f
        val maxHill = mActuallyRadius - mCircleStrokeWidth * 2

        if (mMinimalValuesPercentage > 0) {
            minimalValues = maxHill * mMinimalValuesPercentage
        }
        for (i in 0 until mSides) {

            var actuallyValues = maxHill * mAnimationProgress!![i]
            if (minimalValues > 0) {
                actuallyValues = minimalValues + (maxHill - minimalValues) * mAnimationProgress!![i]
            }

            val angle = Math.PI * 2 / mSides * i - Math.toRadians(mRotateOffset.toDouble())

            val x = (Math.cos(angle) * actuallyValues + mViewCenter).toInt().toFloat()
            val y = (Math.sin(angle) * actuallyValues + mViewCenter).toInt().toFloat()

            if (i == 0) {
                startX = x
                startY = y
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        path.lineTo(startX, startY)
        canvas.drawPath(path, mPaintPolygon!!)
    }

    private fun drawCircle(canvas: Canvas) {
        //Outside circle
        canvas.drawCircle(
            mViewCenter.toFloat(),
            mViewCenter.toFloat(),
            mActuallyRadius.toFloat(),
            mPaintCircle!!
        )
        //Inside circle
        canvas.drawCircle(
            mViewCenter.toFloat(),
            mViewCenter.toFloat(),
            mActuallyRadius * mMinimalValuesPercentage,
            mPaintGrid!!
        )
    }

    private fun initProperties() {
        if (mSize == -1) {
            mSize = Math.min(height, width)
            mCirclePadding = mSize / 6
            mViewCenter = mSize / 2
            mActuallyRadius = mViewCenter - mCirclePadding
            mProgressValues = FloatArray(mSides)
            mAnimationProgress = FloatArray(mSides)

            mPaintLabels = Paint(mPaintGrid)
            mPaintLabels!!.setTextSize(if (mLabelTxtSize == -1f) mSize / 30f else mLabelTxtSize)
            mPaintLabels!!.color = if (mLabelTxtColor == -1) Color.BLACK else mLabelTxtColor
            mPaintLabels!!.style = Paint.Style.FILL
            mPaintLabels!!.isAntiAlias = true
            //mPieces = mSides > 0 ? 360f / mSides : 0;

            mBitmapAvatar = BitmapFactory.decodeResource(resources, mCenterImageResId)

        }
    }


}