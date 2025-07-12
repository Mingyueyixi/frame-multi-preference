package com.lu.magic.frame.xp.socket

import android.content.Context
import com.lu.magic.frame.xp.bean.ContractRequest
import com.lu.magic.frame.xp.bean.ContractResponse2
import com.lu.magic.frame.xp.util.log.XPLogUtil
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class RequestTransfer(val port: Int) {

    fun request(context: Context, request: ContractRequest, listener: OnResponseListener?) {
        PreferencesSocketClient(port = port).request(request) {
            val resp: ContractResponse2? = ContractResponse2<Any?>.fromJson(it)
            listener?.onResponse(request, resp)
        }
    }

    fun requestWithBlock(context: Context, request: ContractRequest): ContractResponse2 {
        val latch = CountDownLatch(1)
        var response: ContractResponse2? = null
        var isSuccessCallBack = false
        request(context, request) { req, resp ->
            response = resp
            isSuccessCallBack = true
            latch.countDown()
        }
        runCatching {
            latch.await(5L, TimeUnit.SECONDS)
        }.onFailure {
            XPLogUtil.w(it)
            response = ContractResponse2(null, it)
        }
        if (!isSuccessCallBack) {
            XPLogUtil.w("Request Session is TimeOut for await")
            response = ContractResponse2(null, TimeoutException("wait response timeout!"))
        }
        return response ?: ContractResponse2(null, null)
    }


    fun interface OnResponseListener {
        fun onResponse(req: ContractRequest, resp: ContractResponse2?)
    }
}