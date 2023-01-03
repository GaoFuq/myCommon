package com.gfq.common

import android.widget.LinearLayout
import com.gfq.common.base.BaseActivity
import com.gfq.common.databinding.MycommonWebActivityBinding
import com.gfq.common.system.AutoWired
import com.just.agentweb.AgentWeb

/**
 *  2022/3/10 20:22
 * @auth gaofuq
 * @description
 */
internal class WebActivity : BaseActivity<MycommonWebActivityBinding>(R.layout.mycommon_web_activity) {

    @AutoWired
    var html:String? = null

    override fun initView() {

        val agentWeb = AgentWeb.with(this)
            .setAgentWebParent(actBinding.rootView, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
            .get()
//            .go(url)

        agentWeb.urlLoader.loadDataWithBaseURL(null,html, "text/html", "utf-8", null)
    }

    override fun initClick() {
    }
}