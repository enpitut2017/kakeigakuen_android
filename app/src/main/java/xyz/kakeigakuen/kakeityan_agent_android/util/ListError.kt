package xyz.kakeigakuen.kakeityan_agent_android.util

import android.app.Activity
import xyz.kakeigakuen.kakeityan_agent_android.generator.DialogGenerator

/**
 * Created by paseri on 2018/02/07.
 */
class ListError: DialogGenerator() {
    fun show(activity: Activity) {
        val title = "一覧の取得"
        val text = "データの取得に失敗しました"
        this.diarog_show(activity, title, text)
    }
}