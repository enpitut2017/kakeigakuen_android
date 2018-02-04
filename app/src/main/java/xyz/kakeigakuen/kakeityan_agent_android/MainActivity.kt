package xyz.kakeigakuen.kakeityan_agent_android

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
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
import xyz.kakeigakuen.kakeityan_agent_android.util.BookDialog
import xyz.kakeigakuen.kakeityan_agent_android.util.BookParser

class MainActivity : RxAppCompatActivity() {

    val RECEST_CODE = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefer = getSharedPreferences("user", Context.MODE_PRIVATE)
        val budget: TextView = findViewById(R.id.budget)
        budget.text = prefer.getInt("budget", 0).toString()
        val rest: TextView = findViewById(R.id.rest)
        rest.text = prefer.getInt("rest", 0).toString()
    }

    fun bookPost(view: View) {
        val prefer = getSharedPreferences("user", Context.MODE_PRIVATE)
        val item: EditText = findViewById(R.id.item)
        val cost: EditText = findViewById(R.id.cost)
        val httpgenerator = HttpGenerator()
        val postclient = httpgenerator.retrofit.create(BookClient::class.java)

        if ((cost.text.toString() == "") || (item.text.toString() == "")) return

        Log.i("action", "book post start")
        postclient.post(cost.text.toString(), item.text.toString(), prefer.getString("token", "0"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindToLifecycle(view)
                .subscribe({
                    if (it.token.toString() != "error") {
                        Log.i("action", "post_success")
                        val editor = prefer.edit()
                        editor.putInt("budget", it.budget)
                        editor.putInt("rest", it.rest)
                        editor.commit()
                        val budget: TextView = findViewById(R.id.budget)
                        budget.text = prefer.getInt("budget", 0).toString()
                        val rest: TextView = findViewById(R.id.rest)
                        rest.text = prefer.getInt("rest", 0).toString()
                        val item: EditText = findViewById(R.id.item)
                        val cost: EditText = findViewById(R.id.cost)
                        val send_itme = item.text.toString()
                        val send_cost = cost.text.toString()
                        item.setText("")
                        cost.setText("")
                        val bookdialog = BookDialog()
                        bookdialog.show(this, send_itme, send_cost)
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

    fun logout (view: View) {
        val prefer = getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor = prefer.edit()
        editor.putString("token", "0")
        editor.putInt("budget", 0)
        editor.putInt("rest", 0)
        editor.commit()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}
