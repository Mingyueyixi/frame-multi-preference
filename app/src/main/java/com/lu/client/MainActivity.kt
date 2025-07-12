package com.lu.client

import android.os.Bundle
import android.telephony.SmsMessage
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lu.frame.xpreference.R
import com.lu.magic.frame.xp.SPreference
import org.json.JSONException
import org.json.JSONObject
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var mEtResult: EditText
    private lateinit var mEtInput: EditText
    val sPreference by lazy {
        SPreference.getRemoteImpl(this, "test", "test", 10010)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mEtInput = findViewById<EditText>(R.id.et_input)
        mEtResult = findViewById<EditText>(R.id.tv_result)
        findViewById<View>(R.id.btn_connect_test).setOnClickListener {
            val result = sPreference.isConnected()
            Toast.makeText(this, "连接结果：$result", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.btn_random_input).setOnClickListener {
            randomGenData()
        }
        findViewById<View>(R.id.btn_random_insert).setOnClickListener {
            randomGenData()
            submit()
        }

        findViewById<View>(R.id.btn_submit).setOnClickListener {
            submit()
        }
        findViewById<View>(R.id.btn_read_all).setOnClickListener {
            try {
                val json = JSONObject(sPreference.all)
                mEtResult.setText(json.toString(2))
            } catch (e: JSONException) {
                Toast.makeText(this, "解析失败", Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<View>(R.id.btn_clear_all).setOnClickListener {
            sPreference.edit().clear().commit().let {
                if (it) {
                    Toast.makeText(this, "清空成功", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "清空失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun submit() {
        var text = mEtInput.text
        var key: String? = null
        var value: String? = null
        try {
            val json = JSONObject(text.toString())
            key = json.optString("key")
            value = json.optString("value")
        } catch (e: JSONException) {
            Toast.makeText(this, "输入格式错误", Toast.LENGTH_SHORT).show()
        }
        sPreference.edit().putString(key, value).commit().let {
            if (it) {
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun randomGenData() {
        val key = Random.nextInt().toString()
        val json = JSONObject().put("key", key).put("value", randomZh(2, 16))
        mEtInput.setText(json.toString())
    }


    /**
     * 随机生成中文
     */
    private fun randomZh(from: Int, to: Int): String {
        val sb = StringBuilder()
        val len = Random.nextInt(from, to)
        for (i in 0 until len) {
            // 随机汉字，范围：常用简体字
            var ch: Char = (0x4e00 + Random.nextInt(0x9fa5 - 0x4e00 + 1)).toChar()
            sb.append(ch)
        }
        return sb.toString()
    }
}