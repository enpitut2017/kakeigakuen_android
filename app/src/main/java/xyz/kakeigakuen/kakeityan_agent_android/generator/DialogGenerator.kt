package xyz.kakeigakuen.kakeityan_agent_android.generator

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import xyz.kakeigakuen.kakeityan_agent_android.util.BookDialog

/**
 * Created by paseri on 2018/02/04.
 */
open class DialogGenerator() : DialogFragment() {

    fun diarog_show(activity: Activity, title: String, text: String) {
        val f = DialogGenerator()
        val args = Bundle()
        args.putString("title", title)
        args.putString("text", text)
        f.arguments = args
        f.show(activity.fragmentManager, "messageDialog")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val ad = AlertDialog.Builder(activity)
        ad.setTitle(arguments.getString("title"))
        ad.setMessage(arguments.getString("text"))
        ad.setPositiveButton("OK", null)
        return ad.create()
    }
}