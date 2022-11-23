package com.example.kakit.surfaceviewexample

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private var mGameView:GameView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        mGameView= GameView(this)
        setContentView(mGameView)
    }

    override fun onPause() {
        super.onPause()
        mGameView?.pause()
    }

    override fun onResume() {
        super.onResume()
        mGameView?.resume()
    }
}