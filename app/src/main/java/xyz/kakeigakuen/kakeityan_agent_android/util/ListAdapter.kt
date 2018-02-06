package xyz.kakeigakuen.kakeityan_agent_android.util

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_list.view.*
import xyz.kakeigakuen.kakeityan_agent_android.R
import xyz.kakeigakuen.kakeityan_agent_android.model.Book
import java.security.AccessControlContext
import java.text.SimpleDateFormat

/**
 * Created by paseri on 2018/02/07.
 */
class ListAdapter(val context: Context, val books: Array<Book>): BaseAdapter() {
    val layoutInflater : LayoutInflater

    init {
        layoutInflater = LayoutInflater.from(context)
    }

    override  fun getCount(): Int {
        return books.count()
    }

    override fun getItem(position: Int): Any {
        return books.get((position))
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, inconvertView: View?, parent: ViewGroup?): View {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var convertView = inconvertView
        convertView = layoutInflater.inflate(R.layout.activity_list, parent, false)
        convertView.findViewById<TextView>(R.id.item).text = books.get(position).item.toString()
        convertView.findViewById<TextView>(R.id.cost).text = books.get(position).cost.toString()
        convertView.findViewById<TextView>(R.id.date).text = sdf.format(books.get(position).time)

        return convertView
    }
}