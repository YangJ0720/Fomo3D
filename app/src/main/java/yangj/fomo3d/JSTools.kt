package yangj.fomo3d

import android.content.Context
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * @author YangJ
 * @date 2018/7/27
 */
class JSTools {

    companion object {

        /**
         * 获取js文件内容
         *
         * @param context  参数为当前上下文对象
         * @param fileName 参数为要获取的js文件名称
         * @return
         */
        fun getJSContent(context: Context, fileName: String): String {
            var inputStream: InputStream? = null
            var outputStream: ByteArrayOutputStream? = null
            try {
                inputStream = context.assets.open(fileName)
                outputStream = ByteArrayOutputStream()
                var len: Int
                val buffer = ByteArray(2048)
                while (true) {
                    len = inputStream.read(buffer)
                    if (len == -1) break
                    outputStream?.write(buffer, 0, len)
                }
                return String(outputStream.toByteArray())
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        }
    }

}