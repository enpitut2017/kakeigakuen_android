package xyz.kakeigakuen.kakeityan_agent_android.util

import android.app.Activity
import xyz.kakeigakuen.kakeityan_agent_android.generator.DialogGenerator

/**
 * Created by paseri on 2018/02/04.
 */
class NetworkError: DialogGenerator () {
    fun show(activity: Activity) {
        val title = "通信エラー"
        val text = "サーバーとの通信に失敗しました"
        this.diarog_show(activity, title, text)
    }
}