package xyz.kakeigakuen.kakeityan_agent_android.util

import android.app.Activity
import xyz.kakeigakuen.kakeityan_agent_android.generator.DialogGenerator

/**
 * Created by paseri on 2018/02/04.
 */
class BookError: DialogGenerator() {
    fun show(activity: Activity) {
        val title = "商品の登録"
        val text = "商品の登録に失敗しました"
        this.diarog_show(activity, title, text)
    }
}