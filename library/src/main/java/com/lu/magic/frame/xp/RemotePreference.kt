package com.lu.magic.frame.xp

import android.content.Context

open class RemotePreference(context: Context, impl: RemotePreferencesImpl) : SPreference(context, impl) {

    fun isConnected(): Boolean {
        (impl as RemotePreferencesImpl).let {
            return SPreference.isServerConnected(it.port)
        }
    }

}