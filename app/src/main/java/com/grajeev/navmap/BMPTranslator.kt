package com.grajeev.navmap

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.math.cos
import kotlin.math.sin

class BMPTranslator {
    private lateinit var bitMapImage: Bitmap
    private lateinit var mockOrigin: Pair<Int, Int>
//    private var scaleX: Float = 1.0
//    private var scaleY: Float = 1.0
    private var scale: Float = 0.05f
    private var angle: Float = 302.00f

    fun decodeBitmapFromResource(res: Resources, resId: Int) {
        bitMapImage = BitmapFactory.decodeResource(res, resId)
    }

    fun setMockOrigin(x: Int, y: Int){
        mockOrigin = Pair(x,y)
    }
    fun setScale(x: Float){
        scale = x
    }

//    fun setScales(x: Float, y: Float){
//        scaleX = x
//        scaleY = y
//    }

    fun translatePointWithMockOrigin(x: Int, y: Int): Pair<Int, Int> {
        return Pair(x-mockOrigin.first,y-mockOrigin.second)
    }

    fun vectorScaleTransFormation(x: Int, y: Int): Pair<Float, Float>{
        return Pair(x * scale,y * scale)
    }

    fun vectorRotationalTransFormation(x: Float, y: Float, angle: Float): Pair<Float, Float>{
        return Pair(x * cos(angle) + y * sin(angle), -x * sin(angle) + y * cos(angle))
    }
}