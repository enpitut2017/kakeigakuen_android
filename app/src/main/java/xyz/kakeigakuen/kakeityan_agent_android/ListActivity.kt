package xyz.kakeigakuen.kakeityan_agent_android

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import xyz.kakeigakuen.kakeityan_agent_android.client.ListClient
import xyz.kakeigakuen.kakeityan_agent_android.client.LoginClient
import xyz.kakeigakuen.kakeityan_agent_android.generator.HttpGenerator

class ListActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
    }

    fun listUpdate(view: View) {
        val prefer = getSharedPreferences("user", Context.MODE_PRIVATE)
        val httpgenerator = HttpGenerator()
        val listclient = httpgenerator.retrofit.create(ListClient::class.java)

        Log.i("action", "liststart")
        listclient.update(prefer.getString("token", "0"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindToLifecycle(view)
                .subscribe({
                    if (it.token.toString() != "error") {
                       Log.i("list", it.list.size.toString())
                    } else {
                        Log.i("action", "you miss email or password")
                    }
                }, {
                    Log.i("action", "login_failed")
                })
    }
}
