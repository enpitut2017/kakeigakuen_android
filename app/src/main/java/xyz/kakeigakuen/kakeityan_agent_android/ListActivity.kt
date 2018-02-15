package xyz.kakeigakuen.kakeityan_agent_android

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.ListView
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import xyz.kakeigakuen.kakeityan_agent_android.client.ListClient
import xyz.kakeigakuen.kakeityan_agent_android.client.LoginClient
import xyz.kakeigakuen.kakeityan_agent_android.generator.HttpGenerator
import xyz.kakeigakuen.kakeityan_agent_android.util.BaseSectionAdapter
import xyz.kakeigakuen.kakeityan_agent_android.util.ListError
import xyz.kakeigakuen.kakeityan_agent_android.util.SectionGenerator
import java.text.SimpleDateFormat

class ListActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        var view : ViewGroup = findViewById(android.R.id.content)
        listUpdate(view.getChildAt(0))
    }

    fun listUpdate(view: View) {

        val listview : ListView = findViewById(R.id.listview)

        val prefer = getSharedPreferences("user", Context.MODE_PRIVATE)
        val httpgenerator = HttpGenerator()
        val listclient = httpgenerator.retrofit.create(ListClient::class.java)

        Log.i("action", "liststart")
        listclient.update(prefer.getString("token", "0"))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .bindToLifecycle(view)
            .subscribe({
                if ( ! it.error) {
                    val sectiongenerator = SectionGenerator(it.list)
                    val listadapter = BaseSectionAdapter(this, sectiongenerator.listHeader, sectiongenerator.listBody)
                    listview.adapter = listadapter
                } else {
                    Log.i("action", "you miss email or password")
                    val listerror = ListError()
                    listerror.show(this)
                }
            }, {
                Log.i("action", "login_failed")
            })
    }

    fun main (view: View) {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}
