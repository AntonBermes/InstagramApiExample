package com.antonbermes.instagramapiexample.ui.authorization

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.os.Build
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.antonbermes.instagramapiexample.repository.Status

class MyWebViewClient(
    private val statusListener: ((Status) -> Unit),
    private val urlListener: ((String) -> Boolean)
) : WebViewClient() {

    // For new devices
    @TargetApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        val url = request.url.toString()
        return urlListener.invoke(url)
    }

    // For old devices
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        return urlListener.invoke(url)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        statusListener.invoke(Status.StartLoading)
    }

    override fun onReceivedError(
        view: WebView?, request: WebResourceRequest?, error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        statusListener.invoke(Status.StartLoading)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        statusListener.invoke(Status.StopLoading)
    }
}