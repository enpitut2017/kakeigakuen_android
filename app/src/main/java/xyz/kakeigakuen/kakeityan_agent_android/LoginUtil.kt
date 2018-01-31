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
    var set_flag: Boolean
    var set_result: String

    constructor() {
        Log.i("LoginUtil", "setup start")
        user = User("", 0)
        httpgenerator = HttpGenerator()
        loginclient = httpgenerator.retrofit.create(LoginClient::class.java)
        set_flag = false
        set_result = "failed"
        Log.i("LoginUtil", "setup finish")
    }

    fun login(email: String, password: String, view: View) {
        var result = ""
        Log.i("action", "loginstart")
        loginclient.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindToLifecycle(view)
                .subscribe({
                    if (it.token.toString() != "error") {
                        Log.i("action", "login_success")
                        this.set_result = "success"
                        this.set_flag = true
                        user = it
                    } else {
                        Log.i("action", "you miss email or password")
                    }
                }, {
                    Log.i("action", "login_failed")
                    this.set_result = "error"
                    this.set_flag = false
                })
    }
}
