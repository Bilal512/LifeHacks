package com.simbaone.lifehacks.browser

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.simbaone.lifehacks.base.BaseActivity
import com.simbaone.lifehacks.constants.Extras
import com.simbaone.lifehacks.databinding.ActivityBrowserBinding

class BrowserActivity : BaseActivity() {

    companion object {
        fun start(context: Context, url: String, heading: String) {
            context.startActivity(Intent(context, BrowserActivity::class.java).apply {
                putExtra(Extras.URL, url)
                putExtra(Extras.HEADING, heading)
            })
        }
    }

    private lateinit var binding: ActivityBrowserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBrowserBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.compToolbar.apply {
            intent.getStringExtra(Extras.HEADING)?.let {
                tvTitle.text = it
            }
            ivBack.setOnClickListener { finish() }
        }

        binding.webView.apply {
            webViewClient = MyWebViewClient()
            intent.getStringExtra(Extras.URL)?.let {
                loadUrl(it)
            }
        }
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            view?.loadUrl(request?.url.toString())
            return true
        }
    }
}