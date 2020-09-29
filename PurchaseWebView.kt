package com.gyogpop.ooatelier_student.Views.P3_TutoringPurchase

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebSettings
import com.gyogpop.ooatelier_student.Components.SallyActivity
import com.gyogpop.ooatelier_student.Iamport.DanalWebViewClient
import com.gyogpop.ooatelier_student.R
import kotlinx.android.synthetic.main.activity_tutoring_purchase.*

class PurchaseWebView : SallyActivity(){

    private val APP_SCHEME = "iamporttest://"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)
        viewInit()
        setData()
        btnConfig()
    }

    override fun viewInit() {
        super.viewInit()
    }

    override fun setData() {
        super.setData()
    }

    override fun btnConfig() {
        super.btnConfig()
    }

    private fun showPurchaseView(){
        webview.visibility = View.VISIBLE
        webview.webViewClient = DanalWebViewClient(this)
        val settings = webview.settings
        settings.javaScriptEnabled = true

        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        val cookieManager: CookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(webview, true)

        val intent = intent
        val intentData: Uri? = intent.data

        if (intentData == null) {
            webview.loadUrl("file:///android_asset/www/iamport-webview.html")
        } else {
            //isp 인증 후 복귀했을 때 결제 후속조치
            val url: String = intentData.toString()
            if (url.startsWith(APP_SCHEME)) {
                val redirectURL : String = url.substring(APP_SCHEME.length + 3)
                webview.loadUrl(redirectURL)
            }
        }
    }
}