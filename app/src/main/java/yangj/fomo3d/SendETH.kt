package yangj.fomo3d

import android.content.Intent
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.webkit.JavascriptInterface

/**
 * @author YangJ
 * @date 2018/7/27
 */
class SendETH(activity: AppCompatActivity) : Object() {

    private val mActivity = activity

    @JavascriptInterface
    fun signTransaction(id: Int, to : String, value: String, nonce: Int, gasLimit: String?, gasPrice: String?, data: String) {
        println("id = $id")
        println("nonce = $nonce")
        println("gasLimit = $gasLimit")
        println("gasPrice = $gasPrice")
        val intent = Intent(mActivity, SendActivity::class.java)
        intent.putExtra("data", data)
        intent.putExtra("from", "")
        intent.putExtra("to", to)
        intent.putExtra("value", value)
        mActivity.startActivityForResult(intent, 0)
    }

    @JavascriptInterface
    fun signMessage(id: Int, data: Any?) {
        println("<----------------- signMessage ----------------->")
    }

    @JavascriptInterface
    fun signPersonalMessage(id: Int, data: String?) {
        println("<----------------- signPersonalMessage ----------------->")
        println("id = $id, data = $data")
        println("<----------------- signPersonalMessage ----------------->")
    }

    @JavascriptInterface
    fun signTypedMessage(id: Int, json: String) {
        println("<----------------- signTypedMessage ----------------->")
    }
}