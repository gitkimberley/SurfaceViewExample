package com.example.kakit.surfaceviewexample

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.*

/**
 * TODO: document your custom view class.
 */
class GameView @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet?=null): SurfaceView(mContext, attrs), Runnable {

    private var mSurfaceHolder: SurfaceHolder
    private val mPaint:Paint
    private val mPath:Path
    private var mViewWidth=0
    private var mViewHeight=0
    private var mFlashLightCone:FlashLightCone?=null
    private var mBitmap:Bitmap?=null
    private var mBitmapX = 0
    private var mBitmapY=0
    private var mWinnerRect:RectF?=null
    private var mRunning= false
    private var mGameThread:Thread?=null
    init {
        mSurfaceHolder = holder
        mPaint = Paint()
        mPaint.color= Color.DKGRAY
        mPath=Path()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth=w
        mViewHeight=h
        mFlashLightCone= FlashLightCone(mViewWidth, mViewHeight)
        mPaint.textSize=(mViewHeight/5).toFloat()
        mBitmap = BitmapFactory.decodeResource(mContext.resources, R.drawable.android)
        setUpBitmap()
    }

    private fun setUpBitmap(){
        mBitmapX = Math.floor(Math.random()+(mViewWidth-mBitmap!!.width)).toInt()
        mBitmapY = Math.floor(Math.random()+(mViewHeight-mBitmap!!.height)).toInt()

        mWinnerRect = RectF(
            mBitmapX.toFloat(),
            mBitmapX.toFloat(),
            (mBitmapX+mBitmap!!.width).toFloat(),
            (mBitmapY+mBitmap!!.height).toFloat()
        )
    }

    fun pause(){
        mRunning=false
        try{
            mGameThread!!.join()

        }catch(e:InterruptedException){

        }
    }

    fun resume(){
        mRunning=true
        mGameThread= Thread(this)
        mGameThread!!.start()
    }

    private fun updateFrame(newX:Int, newY:Int){
        mFlashLightCone!!.update(newX,newY)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when(event.action){
            MotionEvent.ACTION_DOWN ->{
                setUpBitmap()
                updateFrame(x.toInt(),y.toInt())
                invalidate()
            }
            MotionEvent.ACTION_MOVE ->{
                updateFrame(x.toInt(), y.toInt())
                invalidate()
            }
            else ->{}
        }
        //return super.onTouchEvent(event)
        return true
    }

    override fun run() {
        var canvas:Canvas
        while (mRunning){
            if(mSurfaceHolder.surface.isValid){
                val x:Int? = mFlashLightCone?.getX()
                val y:Int?= mFlashLightCone?.getY()
                val radius:Int? = mFlashLightCone?.getRadius()
                canvas = mSurfaceHolder.lockCanvas()
                canvas.save()
                canvas.drawColor(Color.WHITE)
                canvas.drawBitmap(mBitmap!!, mBitmapX.toFloat(), mBitmapY.toFloat(), mPaint)
                mPath.addCircle(x!!.toFloat(), y!!.toFloat(), radius!!.toFloat(), Path.Direction.CCW)
                canvas.clipOutPath(mPath)
                canvas.drawColor(Color.BLACK)
                if(x>mWinnerRect!!.left && x < mWinnerRect!!.right && y >mWinnerRect!!.top && y < mWinnerRect!!.bottom){
                    canvas.drawColor(Color.WHITE)
                    canvas.drawBitmap(mBitmap!!,mBitmapX.toFloat(), mBitmapY.toFloat(), mPaint)
                    canvas.drawText("WIN",(mViewHeight/3).toFloat(), (mViewHeight/2).toFloat(), mPaint )
                }
            //}
                mPath.rewind()
                canvas.restore()
                mSurfaceHolder.unlockCanvasAndPost(canvas)

            }
        }
    }
}

