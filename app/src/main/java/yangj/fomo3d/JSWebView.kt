package yangj.fomo3d

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import java.io.ByteArrayOutputStream

/**
 * @author YangJ
 * @date 2018/7/30
 */
class JSWebView : WebView {

    /**
     * js注入成功标识，该js文件只需要注入一次，true表示注入成功，false表示未注入
     */
    private var mInjection = false

    private var mProgressBar: ProgressBar? = null

    @JvmOverloads
    constructor(context: Context) : super(context) {
        initialize()
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize()
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        initialize()
    }

    private fun initialize() {
        // 设置debug模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        settings.builtInZoomControls = false
        settings.allowContentAccess = true
        settings.blockNetworkImage = false
        settings.blockNetworkLoads = false
        settings.builtInZoomControls = false
        settings.cursiveFontFamily = "cursive"
        settings.databaseEnabled = true
        settings.displayZoomControls = true
        settings.domStorageEnabled = true
        settings.fantasyFontFamily = "fantasy"
        settings.fixedFontFamily = "monospace"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.mediaPlaybackRequiresUserGesture = true
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            settings.offscreenPreRaster = false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.safeBrowsingEnabled = false
        }
        settings.sansSerifFontFamily = "sans-serif"
        settings.serifFontFamily = "serif"
        settings.standardFontFamily = "sans-serif"
        // 如果访问的页面中要与Javascript交互，则WebView必须设置支持Javascript
        settings.javaScriptEnabled = true
        // 支持通过JS弹窗
        settings.javaScriptCanOpenWindowsAutomatically = false
        // 设置可以访问文件
        settings.allowFileAccess = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.allowFileAccessFromFileURLs = true
            settings.allowUniversalAccessFromFileURLs = true
        }
        // 将图片调整到适合WebView的大小
        settings.useWideViewPort = false
        // 缩放至屏幕的大小
        settings.loadWithOverviewMode = false
        // 支持自动加载图片
        settings.loadsImagesAutomatically = true
        // 设置编码格式
        settings.defaultTextEncodingName = "utf-8"
        // 使用缓存的策略，这里使用默认
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
        }
        // 设置WebViewClient
        webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                WebIconDatabase.getInstance().retainIconForPageUrl(url)
                if (mProgressBar?.visibility == View.GONE) {
                    mProgressBar?.visibility = View.VISIBLE
                }
                if (mInjection) {
                    clearCache(true)
                    mInjection = false
                }
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                println("url = $url")
                if (mInjection) {
                    return
                }
                if ((url?.endsWith(".js")!! || url?.endsWith(".css"))) {
                    onJsLocal()
                    mInjection = true
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (mProgressBar?.visibility == View.VISIBLE) {
                    mProgressBar?.visibility = View.GONE
                }
                Log.v("JSWebView", "onPageFinished")
            }
        }
        // 设置WebChromeClient
        webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                mProgressBar?.progress = newProgress
            }
        }
    }

    /**
     * 设置进度条并绑定
     */
    fun setupProgressBar(progressBar: ProgressBar) {
        mProgressBar = progressBar
    }

    /**
     * 本地JS文件注入
     */
    private fun onJsLocal() {
        val builder = StringBuilder(JSTools.getJSContent(context, "tiger.js"))
        // main net
        val address = "0xf22978ed49631b68409a16afa8e123674115011e"
        val rpc = "https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk"
        val chainID = "1"
        // kovan
        // val address = "0xcc2df29b38742ae73fe72dcf0b9aaa0cbf91d27a"
//        val rpc = "https://kovan.infura.io/llyrtzQ3YhkdESt2Fzrk"
//        val chainID = "42"
        // 拼接JS文件内容
        val js = String.format(JSTools.getJSContent(context, "init.js"), address, rpc, chainID)
        builder.append(js)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            loadUrl("javascript:$builder")
        } else {
            evaluateJavascript(builder.toString(), null)
        }
        Log.v("JSWebView", "onJsLocal")
    }

}