package yangj.fomo3d

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.KeyEvent
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_browser.*

/**
 * @author YangJ
 */
class BrowserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)
        initView()
    }

    override fun onResume() {
        super.onResume()
        webView.resumeTimers()
    }

    override fun onPause() {
        super.onPause()
        webView.pauseTimers()
    }

    override fun onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            webView.clearHistory()
            webView.clearCache(true)
            val viewGroup = webView.parent as ViewGroup
            viewGroup.removeView(webView)
            webView.destroy()
        }
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val hash = data?.getStringExtra("hash")
        Handler().postDelayed({
            if (TextUtils.isEmpty(hash)) {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    webView.loadUrl("javascript:executeCallback(8888, \"cancelled\", null)")
                } else {
                    webView.evaluateJavascript("executeCallback(8888, \"cancelled\", null)", null)
                }
            } else {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    webView.loadUrl("javascript:executeCallback(8888, null, \"$hash\")")
                } else {
                    webView.evaluateJavascript("executeCallback(8888, null, \"$hash\")", null)
                }
            }
        }, 50)
    }

    private fun initView() {
        webView.setupProgressBar(progressBar)
        webView.addJavascriptInterface(SendETH(this), "tiger")
        webView.loadUrl(intent.getStringExtra("url"))
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (webView.canGoBack()) {
                webView.goBack()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

}
