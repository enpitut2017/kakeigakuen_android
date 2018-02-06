package xyz.kakeigakuen.kakeityan_agent_android.util

import android.app.Activity
import xyz.kakeigakuen.kakeityan_agent_android.generator.DialogGenerator

/**
 * Created by paseri on 2018/02/04.
 */
class LoginError: DialogGenerator() {
    fun show(activity: Activity) {
        val title = "ログイン"
        val text = "ログインに失敗しました。メールアドレスとパスワードをお確かめください"
        this.diarog_show(activity, title, text)
    }
}