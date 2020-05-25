package com.volodymyr.drawingview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class DrawingView : View {
    var painter: Painter? = null
    private var cachedCanvas: Canvas? = null
    private var bitmapPaint: Paint = Paint(Paint.DITHER_FLAG)
    lateinit var cachedBitmap: Bitmap

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initialize(w,h)
    }

    private fun initialize(w: Int, h: Int){
        cachedBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        cachedBitmap.eraseColor(Color.WHITE)
        cachedCanvas = Canvas(cachedBitmap)
    }

    fun clean(){
        cachedBitmap.eraseColor(Color.WHITE)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawBitmap(cachedBitmap, 0f, 0f, bitmapPaint)
        canvas!!.drawPath(painter!!.path!!, painter!!.paint!!)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event!!.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                painter?.down(x,y)
                invalidate()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                painter?.move(x,y)
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP ->{
                painter?.up(x,y)
                cachedCanvas?.drawPath(painter!!.path!!, painter!!.paint!!)
                painter?.reset()
                invalidate()
                return true
            }
            else -> {
                return super.onTouchEvent(event)
            }
        }
    }


}