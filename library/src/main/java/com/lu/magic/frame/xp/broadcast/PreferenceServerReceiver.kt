package com.lu.magic.frame.xp.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lu.magic.frame.xp.bean.ContractRequest
import com.lu.magic.frame.xp.PreferenceServerImpl
import com.lu.magic.frame.xp.bean.ContractResponse2
import com.lu.magic.frame.xp.util.KxGson
import com.lu.magic.frame.xp.util.log.XPLogUtil

/**
 * 偏好设置服务广播接收器
 */
class PreferenceServerReceiver(val config: BRConfig) : BroadcastReceiver() {
    private var preferenceServerImpl: PreferenceServerImpl? = null

    private fun initPreferenceImplIfNeed(context: Context?) {
        if (context == null) {
            return
        }
        if (preferenceServerImpl == null || preferenceServerImpl?.context != context) {
            preferenceServerImpl = PreferenceServerImpl(context)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        initPreferenceImplIfNeed(context)
        var response: ContractResponse2? = null
        val requestJson = intent?.getStringExtra("request")
        XPLogUtil.i("receiver:", requestJson)
        val request: ContractRequest? = try {
            ContractRequest.fromJson(requestJson)
        } catch (e: Exception) {
            null
        }
        if (request != null) {
            response = preferenceServerImpl?.call(request)
        }
        context?.let {
            val intent = Intent()
            intent.putExtra("requestId", request?.requestId)
            //发给客户端
            intent.action = config.clientAction
            val responseJson = KxGson.GSON.toJson(response)
            intent.putExtra("response", responseJson)
            XPLogUtil.i("replay:", responseJson)
            it.sendBroadcast(intent)
        }
    }
}

