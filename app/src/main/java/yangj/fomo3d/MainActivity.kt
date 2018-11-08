package yangj.fomo3d

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author YangJ
 */
class MainActivity : AppCompatActivity() {

    private var mList: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        initView()
    }

    private fun initData() {
        mList = ArrayList()
        mList?.add("http://exitscam.me/play")
        mList?.add("https://hyperdragons.alfakingdom.com/")
        mList?.add("http://www.cae4d.com/#/")
        mList?.add("https://ether-quest.com/game.html")
        mList?.add("https://dice2.win/games/dice")
        mList?.add("http://ddex.io")
        mList?.add("http://117.50.16.115:8601/Exchange")
        mList?.add("https://seele.skrskr.online/index")
    }

    private fun initView() {
        listView.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList)
        listView.setOnItemClickListener { parent, _, position, _ ->
            val url = parent.adapter.getItem(position) as String
            val intent = Intent(this, BrowserActivity::class.java)
            intent.putExtra("url", url)
            startActivity(intent)
        }
    }

}