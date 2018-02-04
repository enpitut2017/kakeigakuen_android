package xyz.kakeigakuen.kakeityan_agent_android.util

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle

/**
 * Created by paseri on 2018/02/04.
 */
class BookDialog() : DialogFragment() {

    fun show(activity: Activity, item: String, cost: String) {
        val f = BookDialog()
        val args = Bundle()
        args.putString("title", "商品の登録")
        args.putString("text", item + " (" + cost + "円) を登録しました")
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