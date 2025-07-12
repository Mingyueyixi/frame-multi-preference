package com.lu.magic.frame.xp.socket


import android.content.Context
import android.util.Log
import com.google.gson.JsonObject
import com.lu.magic.frame.xp.PreferenceServerImpl
import com.lu.magic.frame.xp.bean.ContractRequest
import com.lu.magic.frame.xp.util.IOUtil
import com.lu.magic.frame.xp.util.KxGson
import com.lu.magic.frame.xp.util.log.XPLogUtil
import java.net.ServerSocket
import java.net.Socket


class PreferencesSocketServer {
    private val context: Context
    private val port: Int
    private val timeout: Int
    private val pref: PreferenceServerImpl
    private var serverThread: Thread? = null

    private constructor(context: Context, port: Int, timeout: Int = 5000) {
        this.context = context
        this.port = port
        this.timeout = timeout
        pref = PreferenceServerImpl(context)
    }


    companion object {
        @JvmStatic
        val sCachePool: HashMap<Int, PreferencesSocketServer?> = hashMapOf()

        fun isServerAlive(port: Int): Boolean {
            return sCachePool[port]?.isAlive() == true
        }

        @JvmStatic
        fun getServer(context: Context, port: Int, timeout: Int = 5000): PreferencesSocketServer {
            var result = sCachePool[port]
            if (result == null || !result.isAlive()) {
                result = PreferencesSocketServer(context, port, timeout)
                sCachePool[port] = result
            }
            return result
        }
    }

    fun isAlive(): Boolean {
        return serverThread?.isAlive == true
    }

    @Synchronized
    fun start() {
        if (serverThread?.isAlive == true) {
            return
        }
        serverThread = Thread {
            runCatching {
                val ss = ServerSocket(port)
                while (true) {
                    val socket = ss.accept()
                    socket.soTimeout = timeout

                    Session(pref, socket).start()
                }
            }
        }
        serverThread?.start()
    }


    class Session(val pref: PreferenceServerImpl, override val socket: Socket) : BaseSession(socket) {

        fun start() {
            Thread{
                handle()
            }.start()
        }

        fun handle() {
            initStreamIf()

            runCatching {
                var line: String? = null
                bReader?.let {
                    val lastMills = System.currentTimeMillis()
                    while (true) {
                        line = it.readLine()
                        if (line != null) {
                            break
                        }
                        val currMills = System.currentTimeMillis()
                        if (currMills - lastMills > socket.soTimeout) {
                            break
                        }
                    }
                }
                XPLogUtil.i(">>>socket client send: ", line)
                val request = KxGson.GSON.fromJson(line, ContractRequest::class.java)
                pref.call(request).let { KxGson.GSON.toJson(it) }
            }.onSuccess {
                sendToClient(it)
            }.onFailure {
                //read timeout or other exception
                val json = JsonObject().apply {
                    addProperty("exception", Log.getStackTraceString(it))
                }
                sendToClient(json.toString())
            }
//            让客户端关闭
            IOUtil.closeQuietly(bWriter, oWriter, oStream, bReader, iReader, iStream, socket)
            XPLogUtil.i(">>>socket server close: " ,  socket)
        }

        private fun sendToClient(text: String) {
            XPLogUtil.i(">>>socket server send: ", text)
            bWriter?.let {
                it.write(text)
                it.newLine()
                it.flush()
            }
        }
    }


}