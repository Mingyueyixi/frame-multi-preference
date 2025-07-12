package com.lu.magic.frame.xpreference

import com.lu.magic.frame.xp.bean.ContractResponse
import com.lu.magic.frame.xp.util.KxGson
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class GsonKotlinUnitTest {
    @Test
    fun addition_isCorrect() {
//        val ty = GsonUtils.getType(ContractResponse::class.java, Boolean::class.java)
//        val ty = object:TypeToken<ContractResponse<Boolean>>(){}
//        val resp: ContractResponse<*> = KxGson.GSON.fromJson("""
//            {
//               "responseId": "112",
//               "data": true
//            }
//        """.trimIndent(), ty)
//        println(resp)



//        Type argument boolean does not satisfy bounds for type variable T declared by class com.lu.magic.frame.xp.bean.ContractResponse
//java.lang.IllegalArgumentException: Type argument boolean does not satisfy bounds for type variable T declared by class com.lu.magic.frame.xp.bean.ContractResponse
//	at com.google.gson.reflect.TypeToken.getParameterized(TypeToken.java:370)
//	at com.lu.magic.frame.xpreference.ExampleUnitTest.addition_isCorrect(ExampleUnitTest.kt:27)
//	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
//	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
//	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
//	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
//	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:59)
//	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
//        val type = object : TypeToken<ContractResponse<Boolean>>() {}.type
//        kotlin 获取泛型信息，不会成功，报错如上，java可以。kotlin编译器的问题
        val type = KxGson.getType(ContractResponse::class.java, java.lang.Boolean.TYPE)
        val response = KxGson.GSON.fromJson<ContractResponse<Boolean>>(
            "            { \n" +
                    "               \"responseId\": \"112\",\n" +
                    "               \"data\": true\n" +
                    "            }           ", type
        )

        println(response)
    }
}