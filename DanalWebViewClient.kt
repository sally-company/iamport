package com.gyogpop.ooatelier_student.Iamport

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.gson.Gson
import com.gyogpop.ooatelier_student.Components.SallyActivity
import java.net.URISyntaxException


class DanalWebViewClient : WebViewClient {

    private lateinit var activity : SallyActivity
    private val iamportID : String = "imp94150156"
    private val app_scheme : String = "iamporttest"
    private val redirect_url : String = "https://api.ooatelier.kr/v1/payments/complete"

    constructor(activity : SallyActivity){
        this.activity = activity
    }

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        if (!request.url.toString().startsWith("http://") && !request.url.toString().startsWith("https://") && !request.url.toString().startsWith("javascript:")) {
            var intent: Intent? = null
            return try {
                intent = Intent.parseUri(request.url.toString(), Intent.URI_INTENT_SCHEME) //IntentURI처리
                val uri: Uri = Uri.parse(intent.dataString)
                activity.startActivity(Intent(Intent.ACTION_VIEW, uri))
                true
            } catch (ex: URISyntaxException) {
                false
            } catch (e: ActivityNotFoundException) {
                if (intent == null) return false
                if (handleNotFoundPaymentScheme(intent.scheme)) return true
                val packageName = intent.getPackage()
                if (packageName != null) {
                    activity.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$packageName")
                        )
                    )
                    return true
                }
                false
            }
        }

        return false
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        if(view != null && !url.isNullOrEmpty()){
            val gson = Gson()
            val pay = Pay()
            val payJson = gson.toJson(pay)
            view.evaluateJavascript("IMP.init('$iamportID');", null)
            view.evaluateJavascript("IMP.request_pay('$payJson' , https://api.ooatelier.kr/v1/payments/complete);", null)
        }
    }

    protected fun handleNotFoundPaymentScheme(scheme: String?): Boolean {
        //PG사에서 호출하는 url에 package정보가 없어 ActivityNotFoundException이 난 후 market 실행이 안되는 경우
        if (PaymentScheme.ISP.equals(scheme, ignoreCase = true)) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_ISP)
                )
            )
            return true
        } else if (PaymentScheme.BANKPAY.equals(scheme, ignoreCase = true)) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + PaymentScheme.PACKAGE_BANKPAY)
                )
            )
            return true
        }
        return false
    }
}