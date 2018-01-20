package xyz.kakeigakuen.kakeityan_agent_android

import android.util.Log
import android.view.View
import xyz.kakeigakuen.kakeityan_agent_android.client.LoginClient
import xyz.kakeigakuen.kakeityan_agent_android.generator.HttpGenerator
import xyz.kakeigakuen.kakeityan_agent_android.model.User
import rx.schedulers.Schedulers
import rx.android.schedulers.AndroidSchedulers
import com.trello.rxlifecycle.kotlin.bindToLifecycle

/**
 * Created by paseri on 2018/01/20.
 */

class LoginUtil {
    var user: User
    val loginclient: LoginClient
    val httpgenerator: HttpGenerator

    constructor() {
        Log.i("LoginUtil", "setup start")
        user = User("", 0)
        httpgenerator = HttpGenerator()
        loginclient = httpgenerator.retrofit.create(LoginClient::class.java)
        Log.i("LoginUtil", "setup finish")
    }

    fun login(email: String, password: String, view: View): Boolean {
        var result = ""
        Log.i("action", "loginstart")
        loginclient.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindToLifecycle(view)
                .subscribe({
                    Log.i("action", "login_success")
                    result = "success"
                    user = it
                }, {
                    Log.i("action", "login_failed")
                    result = "error"
                })
        if (result != "success") return false
        Log.i("token", user.token)
        Log.i("budget", user.budget.toString())
        return true
    }
}
