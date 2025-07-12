package com.lu.magic.frame.xp.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.random.Random

class KxGson {
    companion object {
        @JvmStatic
        val GSON: Gson by lazy<Gson> {
            GsonBuilder().create()
        }
        @JvmStatic
        val GSON_PRETTY: Gson by lazy<Gson> {
            GsonBuilder().setPrettyPrinting().create()
        }

        @JvmStatic
        fun getListType(type: Type?): Type? {
            return TypeToken.getParameterized(MutableList::class.java, type).type
        }
        @JvmStatic
        fun getSetType(type: Type?): Type? {
            return TypeToken.getParameterized(MutableSet::class.java, type).type
        }
        @JvmStatic
        fun getMapType(keyType: Type?, valueType: Type?): Type? {
            return TypeToken.getParameterized(MutableMap::class.java, keyType, valueType).type
        }

        @JvmStatic
        fun getArrayType(type: Type?): Type? {
            return TypeToken.getArray(type).type
        }

        /**
         * 获取参数化类型，只能java使用，kotlin 使用会有问题，kotLin 使用 object : TypeToken<xxx>(){}.type，封装不了
         */
        @JvmStatic
        fun getType(rawType: Type, vararg typeArguments: Type): Type {
            // 判断调用方是kotlin 还是java
            return TypeToken.getParameterized(rawType, *typeArguments).type

        }

    }

}

