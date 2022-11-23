package com.example.kakit.surfaceviewexample

class FlashLightCone {
    private var mX=0
    private var mY=0
    private var mRadius=0
    constructor(viewWidth:Int, viewLength:Int){
        mX=viewWidth/2
        mY=viewLength/2
        mRadius=if(viewWidth <= viewLength) mX/3 else mY/3
    }

    fun update(newX:Int, newY:Int){
        mX=newX
        mY=newY
    }

    fun getX():Int{
        return mX
    }

    fun getY():Int{
        return mY
    }

    fun getRadius():Int{
        return mRadius
    }
}