package xyz.kakeigakuen.kakeityan_agent_android

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import xyz.kakeigakuen.kakeityan_agent_android.client.BookClient
import xyz.kakeigakuen.kakeityan_agent_android.generator.HttpGenerator
import xyz.kakeigakuen.kakeityan_agent_android.util.BookParser
import java.util.ArrayList

class MainActivity : RxAppCompatActivity() {

    val RECEST_CODE = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefer = getSharedPreferences("user", Context.MODE_PRIVATE)
        var budget: TextView = findViewById(R.id.budget)
        budget.text = prefer.getInt("budget", 0).toString()
    }

    fun bookPost(view: View) {
        val prefer = getSharedPreferences("user", Context.MODE_PRIVATE)
        val item: EditText = findViewById(R.id.item)
        val cost: EditText = findViewById(R.id.cost)
        val httpgenerator = HttpGenerator()
        val postclient = httpgenerator.retrofit.create(BookClient::class.java)

        Log.i("action", "book post start")
        postclient.post(cost.text.toString(), prefer.getString("token", "0"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindToLifecycle(view)
                .subscribe({
                    if (it.token.toString() != "error") {
                        Log.i("action", "post_success")
                        val editor = prefer.edit()
                        editor.putInt("budget", it.budget)
                        editor.commit()
                        var budget: TextView = findViewById(R.id.budget)
                        budget.text = prefer.getInt("budget", 0).toString()
                    } else {
                        Log.i("action", "you miss email or password")
                    }
                }, {
                    Log.i("action", "login_failed")
                })
    }

    fun speechRecognizer (view: View) {
        try {
            intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "RecognizeSpeechEx")
            startActivityForResult(intent, RECEST_CODE)
        } catch (e: ActivityNotFoundException) {
            Log.e("action", e.toString())
        }
    }

    override fun onActivityResult (requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RECEST_CODE && resultCode == Activity.RESULT_OK) {
            val item: EditText = findViewById(R.id.item)
            val cost: EditText = findViewById(R.id.cost)
            var result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val parser = BookParser(result[0])
            item.setText(parser.item)
            cost.setText(parser.cost)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
