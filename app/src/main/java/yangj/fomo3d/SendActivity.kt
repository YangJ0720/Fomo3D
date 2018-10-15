package yangj.fomo3d

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_send.*
import java.util.*

/**
 * @author YangJ
 */
class SendActivity : AppCompatActivity() {

    private var mIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 模拟转账操作取消
        if (mIntent == null) {
            setResult(Activity.RESULT_CANCELED)
        }
    }

    private fun initView() {
        // 显示从游戏页面传递过来的参数
        val builder = StringBuilder()
        builder.append("data = ${intent.getStringExtra("data")}\n")
        builder.append("from = ${intent.getStringExtra("from")}\n")
        builder.append("to = ${intent.getStringExtra("to")}\n")
        builder.append("value = ${intent.getStringExtra("value")}")
        println("builder = $builder")
        textView.text = builder
        // 模拟转账操作成功，需要从服务端获取一个交易哈希用于返回游戏页面
        button.setOnClickListener {
            mIntent = Intent()
            val value = "0x2843df2f1ce31d48faf17ec8f0582c2d350b794f06b25853e8d130c140407649"
            mIntent?.putExtra("hash", value)
            setResult(Activity.RESULT_OK, mIntent)
            finish()
        }
    }
}
