package xyz.kakeigakuen.kakeityan_agent_android

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import xyz.kakeigakuen.kakeityan_agent_android.client.LoginClient
import xyz.kakeigakuen.kakeityan_agent_android.generator.HttpGenerator

class LoginActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefer = getSharedPreferences("user", Context.MODE_PRIVATE)
        if (prefer.getString("token", "0") != "0") {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        setContentView(R.layout.activity_login)
    }

    fun onClickLogin(view: View) {
        val email: EditText = findViewById(R.id.email)
        val password: EditText = findViewById(R.id.password)
        val httpgenerator = HttpGenerator()
        val loginclient = httpgenerator.retrofit.create(LoginClient::class.java)

        Log.i("action", "loginstart")
        loginclient.login(email.text.toString(), password.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindToLifecycle(view)
                .subscribe({
                    if (it.token.toString() != "error") {
                        Log.i("action", "login_success")
                        val prefer = getSharedPreferences("user", Context.MODE_PRIVATE)
                        val editor = prefer.edit()
                        editor.putString("token", it.token)
                        editor.putInt("budget", it.budget)
                        editor.commit()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.i("action", "you miss email or password")
                    }
                }, {
                    Log.i("action", "login_failed")
                })
    }
}
