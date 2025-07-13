package com.lu.magic.frame.xp.socket

import com.lu.magic.frame.xp.bean.ContractRequest
import com.lu.magic.frame.xp.bean.ContractResponse2
import com.lu.magic.frame.xp.util.log.XPLogUtil
import java.net.Socket
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class PreferencesSocketClient(val host: String = "127.0.0.1", val port: Int) {
    private var clientThread: Thread? = null
    private var requestCallBackPool: MutableMap<String, CallBack?> = mutableMapOf()

    fun testConnect(): Boolean {
        var result: Boolean = false
        try {
            val latch = CountDownLatch(1)
            result = false
            Thread(Runnable {
                runCatching {
                    val socket = Socket(host, port)
                    socket.soTimeout = 5000
                    result = socket.isConnected
                    socket.close()
                    latch.countDown()
                }.onFailure {
                    XPLogUtil.e(it)
                }
            }).start()
            latch.await(1000L, TimeUnit.MILLISECONDS)
        } catch (e: Throwable) {
            XPLogUtil.e(e)
        }
        return result
    }

    fun request(request: ContractRequest, callBack: CallBack) {
        if (clientThread?.isAlive == true) {
            return
        }
        clientThread = Thread {
            runCatching {
                val socket = Socket(host, port)
                Session(this, socket).handle(request)
            }.onFailure {
                try {
                    XPLogUtil.e(it)
                    val eText = ContractResponse2(request.requestId, null, it).toJson();
                    requestCallBackPool.remove(request.requestId)?.onResponse(eText)
                } catch (e: Exception) {
                    XPLogUtil.e(e)
                }
            }
        }
        requestCallBackPool[request.requestId] = callBack
        clientThread?.start()
    }

    class Session(val client: PreferencesSocketClient, override val socket: Socket) : BaseSession(socket) {

        fun handle(request: ContractRequest) {
            initStreamIf()
            runCatching {
                sendToServer(request.toJson())
            }
            runCatching {
                bReader?.let {
                    var line: String? = null
                    while (true) {
                        line = it.readLine()
                        if (line != null) {
                            break
                        }
                    }
                    val resp = ContractResponse2.fromJson(line)
                    val func = client.requestCallBackPool.remove(resp?.responseId)
                    func?.onResponse(line)
                }
            }
        }

        private fun sendToServer(text: String) {
            bWriter?.let {
                it.write(text)
                it.newLine()
                it.flush()
            }
        }
    }

    fun interface CallBack {
        fun onResponse(text: String?)
    }

}

