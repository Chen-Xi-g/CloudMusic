package com.alvin.music.domain.util

import android.os.Parcelable
import androidx.media3.common.Player
import com.tencent.mmkv.MMKV
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object BaseKV{
    object User: Delegates(){
        override fun zoneId(): String {
            return "user"
        }

        /**
         * 是否登录
         */
        var isLogin by boolean(false)

        /**
         * 存储Cookie
         */
        var cookie by string("")
    }

    object Playing: Delegates(){
        override fun zoneId(): String {
            return "playing"
        }

        /**
         * 播放模式
         */
        var playMode by int(Player.REPEAT_MODE_ALL)
    }
}

/**
 * <h3> 作用类描述：使用Android原生的SharedPreferences存储数据</h3>
 *
 * Kotlin 属性委托实现SP存储
 *
 * @author 高国峰
 */
abstract class Delegates {

    // 获取MMKV实例
    val kv by lazy { MMKV.mmkvWithID(zoneId()) }

    /**
     * 存储`Int`值
     *
     */
    fun int(defaultValue: Int = 0) = object : ReadWriteProperty<Any, Int> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Int {
            return kv.decodeInt(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
            kv.encode(property.name, value)
        }
    }

    /**
     * 存储`String`值
     *
     */
    fun string(defaultValue: String? = null) = object : ReadWriteProperty<Any, String?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): String? {
            return kv.decodeString(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
            kv.encode(property.name, value)
        }
    }

    /**
     * 存储`Long`值
     *
     */
    fun long(defaultValue: Long = 0L) = object : ReadWriteProperty<Any, Long> {

        override fun getValue(thisRef: Any, property: KProperty<*>): Long {
            return kv.decodeLong(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
            kv.encode(property.name, value)
        }
    }

    /**
     * 存储`Boolean`值
     *
     */
    fun boolean(defaultValue: Boolean = false) = object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return kv.decodeBool(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            kv.encode(property.name, value)
        }
    }

    /**
     * 存储`Float`值
     *
     */
    fun float(defaultValue: Float = 0.0f) = object : ReadWriteProperty<Any, Float> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Float {
            return kv.decodeFloat(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) {
            kv.encode(property.name, value)
        }
    }

    /**
     * 存储Parcelable数据
     *
     */
    inline fun <reified T : Parcelable> parcelable(
        defaultValue: T? = null
    ): ReadWriteProperty<Any, T?> =
        object : ReadWriteProperty<Any, T?> {
            override fun getValue(thisRef: Any, property: KProperty<*>): T? =
                kv.decodeParcelable(property.name, T::class.java, defaultValue)

            override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
                kv.encode(property.name, value)
            }
        }

    /**
     * 存储Serializable
     */
    inline fun <reified T> obj(): ReadWriteProperty<Any, T?> =
        object : ReadWriteProperty<Any, T?> {

            private val json = Json {
                ignoreUnknownKeys = true // JSON和数据模型字段可以不匹配
                coerceInputValues = true // 如果JSON字段是Null则使用默认值
            }

            override fun getValue(thisRef: Any, property: KProperty<*>): T?{
                kv.decodeString(property.name)?.let {
                    return json.decodeFromString(it)
                }
                return null
            }

            override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
                kv.encode(property.name, json.encodeToString(value))
            }
        }

    /**
     * 删除指定Key的Value
     *
     * @param key
     */
    fun removeValue(key: String) {
        kv.removeValueForKey(key)
    }

    /**
     * 删除指定Key的Value
     *
     * @param key
     */
    fun removeValues(vararg key: String) {
        kv.removeValuesForKeys(key)
    }

    /**
     * 清除所有数据
     *
     */
    fun clearAll() {
        kv.clearAll()
    }

    /**
     * 设置存储库的名称
     *
     */
    abstract fun zoneId(): String
}