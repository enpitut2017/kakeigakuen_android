package xyz.kakeigakuen.kakeityan_agent_android.util

import android.app.Activity
import xyz.kakeigakuen.kakeityan_agent_android.generator.DialogGenerator

/**
 * Created by paseri on 2018/02/05.
 */
class SignUpError: DialogGenerator() {
    fun show(activity: Activity) {
        val title = "新規作成"
        val text = "ブラウザを開くことができませんでした"
        this.diarog_show(activity, title, text)
    }
}