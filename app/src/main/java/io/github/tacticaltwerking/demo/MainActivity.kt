package io.github.tacticaltwerking.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {


    var mSides = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_less.setOnClickListener {
            if (mSides >=3) {
                mSides -= 1
                et_sides.setText(mSides.toString())
            }
        }

        btn_more.setOnClickListener {
            if (mSides == -1) {
                mSides = 3
            } else {
                mSides += 1
            }
            et_sides.setText(mSides.toString())
        }

        et_sides.addTextChangedListener {
            mSides = if (null == it
                || TextUtils.isEmpty(it.toString())
                || Integer.valueOf(it.toString()) < 3
            ) {
                -1
            } else {
                Integer.valueOf(it.toString())
            }
        }

        btnSwitch.setOnClickListener {
            val random = Random()
            val sides = if (mSides >= 3) mSides else random.nextInt(5) + 10
            val mProgressValues = FloatArray(sides)
            for (i in 0 until sides) {
                mProgressValues[i] = random.nextFloat()
                if (random.nextInt() % 3 == 0) {
                    mProgressValues[i] = 1f
                }
            }
            val mLabels = arrayOfNulls<String>(sides)
            for (i in 0 until sides) {
                mLabels[i] = "label$i"
            }

            ppv.initial(sides, mProgressValues, mLabels)
            ppv.animateProgress()
        }
    }
}
