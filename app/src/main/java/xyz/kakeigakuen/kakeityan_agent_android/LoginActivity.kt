package xyz.kakeigakuen.kakeityan_agent_android

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.trello.rxlifecycle.components.support.RxAppCompatActivity

class LoginActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onClickLogin(view: View) {
        val email: EditText = findViewById(R.id.email)
        val password: EditText = findViewById(R.id.password)
        val loginutil = LoginUtil()
        loginutil.login(email.text.toString(), password.text.toString(), view)
        Log.i("result", loginutil.set_result.toString())
    }
}
