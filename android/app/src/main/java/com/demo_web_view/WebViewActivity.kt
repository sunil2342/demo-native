package com.demo_web_view

import android.app.Activity
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.KeyEvent.ACTION_DOWN
import android.view.KeyEvent.ACTION_UP
import android.view.MotionEvent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.SimpleExoPlayer


class WebViewActivity : AppCompatActivity() {

    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var webView: WebView
    private lateinit var videoView: VideoView
    private lateinit var ivBackward:ImageView
    private lateinit var ivForward:ImageView
    private var WEB_URL = "https://experience-dev.sourcesync.io/today-home-owner-overlay"
    private var isPlay: Boolean = false
    private var isFirstTime: Boolean = true
    private lateinit var ivPlayPause: ImageView
    private lateinit var playerLayout:RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        prepareMediaPlayer()
        webViewLoad()
    }

    private fun prepareMediaPlayer() {

        videoView = findViewById(R.id.videoView)
        webView = findViewById(R.id.webView)
        val uri: Uri = Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/mp4/BigBuckBunny.mp4")
        videoView.setVideoURI(uri)
        //  videoView.start()
        webView.settings.javaScriptEnabled = true
        ivBackward=findViewById(R.id.ivForward)
        ivForward=findViewById(R.id.ivBackward)
        ivBackward.setOnClickListener {
            var currentPosition = videoView.currentPosition+10000
            videoView.seekTo(currentPosition)
        }
        ivForward.setOnClickListener {
            if (videoView.currentPosition>10000)
            {
                videoView.seekTo(0)
            }
            else{
                var currentPosition = videoView.currentPosition-10000
                videoView.seekTo(currentPosition)
            }
        }
    }

    private fun webViewLoad() {
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.settings.domStorageEnabled = true
        webView.clearHistory()
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.webViewClient = MywebViewClient(this)
        webView.webViewClient = WebViewClient()
        webView.setBackgroundColor(Color.TRANSPARENT)
        webView?.addJavascriptInterface(
            JavaScriptInterface(this@WebViewActivity),
            "SourceInterface"
        )
        ivPlayPause = findViewById(R.id.ivPlayPause)
        ivPlayPause.setOnClickListener {
            println("ivPlayPause=========== " + ivPlayPause)
            playPause()
        }

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val params = videoView.layoutParams as RelativeLayout.LayoutParams
        params.width = metrics.widthPixels
        params.height = metrics.heightPixels
        params.leftMargin = 0
        videoView.layoutParams = params

    }

    fun emit(listener: String, listener1: String, listener2: String) {
        // do whatever you want to do with listener you will get play/pause/seek
        println("postMessage======= " + listener)
    }

    override fun onDestroy() {
        simpleExoPlayer.playWhenReady = false
        super.onDestroy()
    }
    inner class MywebViewClient internal constructor(private val activity: Activity) :
        WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return true
        }
        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            webView.loadUrl(url)
            return true
        }
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }
    }
    private fun loadUrl() {
        webView?.loadUrl(WEB_URL)
    }
    private fun playPause() {
        if (isFirstTime) {
            isFirstTime = false
            loadUrl()
        }
        if (isPlay) {
            isPlay = false
            ivPlayPause.setBackgroundResource(R.mipmap.play)
            videoView.pause()
        } else {
            isPlay = true
            ivPlayPause.setBackgroundResource(R.mipmap.pause)
            videoView.start()
        }
    }
    fun action(msg: String) {
        println("msg======= " + msg)
        isPlay = false
        ivPlayPause.setBackgroundResource(R.mipmap.play)
        videoView.pause()
    }

    override fun dispatchKeyEvent(mReady: KeyEvent): Boolean {
        println("dispatchKeyEvent================= ")
        if (mReady.action == ACTION_DOWN)
        {
            println("dispatchKeyEvent=======ACTION_DOWN========== ")
        }
        else if (mReady.action == ACTION_UP)
        {
            println("dispatchKeyEvent=======ACTION_UP========== ")
        }
        return super.dispatchKeyEvent(mReady)
    }

    override fun dispatchGenericMotionEvent(ev: MotionEvent): Boolean {
        println("dispatchGenericMotionEvent================= " + ev.orientation)
        println("dispatchGenericMotionEvent================= " + ev.action)
        return super.dispatchGenericMotionEvent(ev)
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        println("onKeyDown================= " + keyCode)
        return super.onKeyDown(keyCode, event)
    }
    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        println("onGenericMotionEvent================= ")
        return true
    }
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        println("onKeyUp================= ")
        return super.onKeyUp(keyCode, event)
    }
}