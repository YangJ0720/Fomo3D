Android客户端与以太坊Dapp交互
==

    这应该是目前全网Android WebView与以太坊Dapp交互最全的Demo

apk下载
------
    [下载apk点我](https://github.com/YangJ0720/Fomo3D/blob/master/apk/app-debug.apk) 
              
## 效果图
<img src="https://github.com/YangJ0720/Fomo3D/blob/master/gif/fomo3d.gif" width="480" height="800"/>
<img src="https://github.com/YangJ0720/Fomo3D/blob/master/gif/cae4d.gif" width="480" height="800"/>

## 要点
#### 1.获得以太坊合约JS文件
这个以太坊合约JS文件我是直接从Trust Wallet反编译得到，一共两个文件分别为：<br>
`init.js`<br>
`tiger.js`<br>
文件名我改过了不必在意，其中init.js是我们特别需要关注的，这个文件前三行代码如下：
```
const addressHex = "%1$s";
const rpcURL = "%2$s";
const chainID = "%3$s";
```
这里我已经修改好了，他们分别代表 **钱包地址**、**以太坊主网和测试网络节点**、**区块链网络ID** ，稍后我们会用到
<br>
继续往下关注这几个地方：
```
tiger.signTransaction(id, tx.to || null, tx.value, nonce, gasLimit, gasPrice, data);
...
tiger.signMessage(id, data);
...
tiger.signPersonalMessage(id, data);
...
tiger.signTypedMessage(id, JSON.stringify(data))
...
```
这里的tiger会对应Native中的Java对象，在Android代码中设置addJavascriptInterface第二个参数就对应这里的tiger

还有就是主网和测试网设置，如下：
```
// main net
val address = "0xf22978ed49631b68409a16afa8e123674115011e"
val rpc = "https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk"
val chainID = "1"
// kovan
val address = "0xcc2df29b38742ae73fe72dcf0b9aaa0cbf91d27a"
val rpc = "https://kovan.infura.io/llyrtzQ3YhkdESt2Fzrk"
val chainID = "42"
```
测试网就可以在未正式发布的合约中开发测试



#### 2.注入JS时机
一般情况下我们用都是在页面加载完毕的时候注入JS代码，例如在：
```
override fun onPageFinished(view: WebView?, url: String?) {
  super.onPageFinished(view, url)
  // 执行JS注入操作
}
```
但是在本例中，assets文件夹中的两个JS文件必须在页面刚开始加载的时候执行注入否则会注入失败。我是在onPageStarted中注入的

------
#### 3.注入JS版本问题
在Android中注入JS有两个方法分别是 **loadUrl()** 和 **evaluateJavascript()**<br>
如果是币圈的朋友可能会发现各大钱包均只支持Android 5.0以上版本，因为loadUrl这个方法注入这两个JS文件的时候始终是注入失败的，具体原因没有深究。不过这里可以通过远程注入JS的方式实现，但但但但是这种方式受到网络影响注入成功需要一定的时间，体验很差不建议

------
#### 4.以太坊Dapp交互
将JS文件注入到页面之后执行相关合约操作就可以主动调起Android Native了，例如：<br>
1.在Dapp中购买钥匙主动调起Android Native执行转账<br>
2.在Android Native执行转账完成后回调Dapp通知转账完毕<br>
3.或者Android Native执行转账然后取消转账，也需要回调Dapp通知转账取消<br>
由Android Native回调Dapp的代码如下：
```
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
```
可以看到取消交易只需要传入 **cancelled** 就可以了，如果转账完成需要拿到服务端提供的交易hash值并传入。<br>
对了，这里一定要有一定的延时，就像我延时了50毫秒。不然的话有可能你会遇到某Dapp不能把正在交易的对话框关闭
