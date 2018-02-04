package xyz.kakeigakuen.kakeityan_agent_android.util

import android.app.Activity
import xyz.kakeigakuen.kakeityan_agent_android.generator.DialogGenerator

/**
 * Created by paseri on 2018/02/04.
 */
class BookDialog : DialogGenerator() {

    fun show(activity: Activity, item: String, cost: String) {
        val title = "商品の登録"
        val text = item + " (" + cost + "円) を登録しました"
        this.diarog_show(activity, title, text)
    }
}