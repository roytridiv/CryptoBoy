package com.example.cryptoboy

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Region
import android.util.AttributeSet
import android.view.View
import java.util.ArrayList
import android.view.ViewGroup.MarginLayoutParams
import android.graphics.DashPathEffect





class RecorderVisualizerView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    val LINE_WIDTH = 10 // width of visualizer lines

    val LINE_SCALE = 50 // scales visualizer lines

    var amplitudes // amplitudes for line lengths
            : MutableList<Float>? = null
    var width_new // width of this View
            = 0
    var height_new // height of this View
            = 0
    private var linePaint // specifies line drawing characteristics
            : Paint? = null

    private var linePaint2 // specifies line drawing characteristics
            : Paint? = null


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        width_new = w // new width of this View
        height_new = h // new height of this View
        amplitudes = ArrayList<Float>(width / LINE_WIDTH)
    }


    // clear all amplitudes to prepare for a new visualization
    open fun clear() {
        amplitudes!!.clear()
    }


    // add the given amplitude to the amplitudes ArrayList
    open fun addAmplitude(amplitude: Float) {
        amplitudes!!.add(amplitude) // add newest to the amplitudes ArrayList

        // if the power lines completely fill the VisualizerView
        if (amplitudes!!.size * LINE_WIDTH >= width) {
            amplitudes!!.removeAt(0) // remove oldest power value
        }
    }

    // draw the visualizer with scaled lines representing the amplitudes
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        // create Paint for lines
        linePaint = Paint()
        linePaint!!.style = Paint.Style.STROKE
        linePaint!!.color = resources.getColor(R.color.red_light) // set color to green
        linePaint!!.strokeWidth = LINE_WIDTH.toFloat() // set stroke width

//        linePaint!!.setAntiAlias(true)
//        linePaint!!.setDither(true)
//        linePaint!!.setColor(Color.WHITE)
//        linePaint!!.setARGB(255, 0, 0, 0)
//        linePaint!!.setStyle(Paint.Style.STROKE)
//        linePaint!!.setPathEffect(DashPathEffect(floatArrayOf(10f, 40f), 0f))
//        linePaint!!.setStrokeWidth(12f)

//        linePaint2 = Paint()
//        linePaint2!!.style = Paint.Style.STROKE
//        linePaint2!!.color = Color.BLACK // set color to green
//        linePaint2!!.strokeWidth = LINE_WIDTH.toFloat()

//        val minX = getPaddingLeft()
//       val maxX = getWidth() - getPaddingLeft() - getPaddingRight()
//        val minY = getPaddingTop()
//        val maxY = getHeight() - getPaddingTop() - getPaddingBottom()


        val middle = height / 2 // get the middle of the View
        var curX = 0f // start curX at zero

        // for each item in the amplitudes ArrayList
        for (power in amplitudes!!) {
            val scaledHeight: Float = power / LINE_SCALE // scale the power
            curX += LINE_WIDTH.toFloat() // increase X by LINE_WIDTH
            // draw a line representing this item in the amplitudes ArrayList
            canvas.drawLine(
                curX, middle + scaledHeight / 2, curX, middle
                        - scaledHeight / 2, linePaint!!
            )

            curX+= 2f


//            canvas.drawRect(0f, 0f, 10f, getHeight().toFloat(), linePaint2!!)
//            canvas.drawLine(
//                curX, 300.0f, curX, middle
//                        - scaledHeight / 2, linePaint2!!
//            )
//            canvas.clipRect(minX.toFloat(), minY.toFloat(), maxX.toFloat(), maxY.toFloat() , Region.Op.REPLACE)

            //draw left rectangle


            //draw right rectangle
//            canvas.drawRect(getWidth() - 10f, 0f, getWidth().toFloat(), getHeight().toFloat(), linePaint2!!)


        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val margins = MarginLayoutParams::class.java.cast(layoutParams)
        val margin = pxToDp(35)
        margins.topMargin = 0
        margins.bottomMargin = 0
        margins.leftMargin = margin
        margins.rightMargin = margin
        layoutParams = margins
    }

    private fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }
}


