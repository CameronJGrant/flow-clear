package com.flowclear.app

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge-to-edge immersive mode
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        window.statusBarColor = 0xFF0A0A0F.toInt()
        window.navigationBarColor = 0xFF0A0A0F.toInt()

        webView = WebView(this).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.setSupportZoom(false)
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            setBackgroundColor(0xFF0A0A0F.toInt())
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    val url = request?.url?.toString() ?: return false
                    if (url.startsWith("http://") || url.startsWith("https://")) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        return true
                    }
                    return false
                }
            }
            overScrollMode = View.OVER_SCROLL_NEVER
        }

        setContentView(webView)
        webView.loadUrl("file:///android_asset/flow-clear.html")
    }

    @Deprecated("Use OnBackPressedCallback instead")
    override fun onBackPressed() {
        webView.evaluateJavascript("handleBackButton()") { result ->
            if (result == "false") {
                @Suppress("DEPRECATION")
                super.onBackPressed()
            }
        }
    }
}
