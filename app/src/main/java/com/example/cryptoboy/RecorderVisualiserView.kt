package com.example.cryptoboy

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.ArrayList

class RecorderVisualizerView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    val LINE_WIDTH = 2 // width of visualizer lines

    val LINE_SCALE = 100 // scales visualizer lines

    var amplitudes // amplitudes for line lengths
            : MutableList<Float>? = null
    var width_new // width of this View
            = 0
    var height_new // height of this View
            = 0
    private var linePaint // specifies line drawing characteristics
            : Paint? = null



//    fun RecorderVisualizerView(context: Context?, attrs: AttributeSet?) {
//        linePaint = Paint() // create Paint for lines
//        linePaint!!.color = Color.RED // set color to green
//        linePaint!!.strokeWidth = LINE_WIDTH.toFloat() // set stroke width
//    }

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
            linePaint!!.color = Color.RED // set color to green
            linePaint!!.strokeWidth = LINE_WIDTH.toFloat() // set stroke width



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
        }
    }
}


