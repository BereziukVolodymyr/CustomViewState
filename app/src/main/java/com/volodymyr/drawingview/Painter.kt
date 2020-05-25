package com.volodymyr.drawingview

import android.graphics.Paint
import android.graphics.Path

class Painter{

    private val TRESHHOLD = 4f
    private var lastX: Float = 0f
    private var lastY: Float = 0f
    var path: Path? = null
    var paint: Paint? = null


    fun down(x: Float, y: Float){
        path?.reset()
        path?.moveTo(x,y)
        lastX = x
        lastY = y
    }

    fun move(x: Float, y: Float){
        val dx: Float = Math.abs(x - lastX)
        val dy: Float = Math.abs(y - lastY)

        if (dx >= TRESHHOLD || dy >= TRESHHOLD) {
            path?.quadTo(lastX, lastY, (x + lastX)/2, (y + lastY)/2)
            lastX = x
            lastY = y
        }
    }

    fun up(x: Float, y: Float){
        path?.lineTo(x, y)
    }

    fun reset(){
        path?.reset()
    }

}