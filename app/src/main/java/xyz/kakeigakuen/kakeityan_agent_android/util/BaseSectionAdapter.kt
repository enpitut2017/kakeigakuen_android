package xyz.kakeigakuen.kakeityan_agent_android.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import xyz.kakeigakuen.kakeityan_agent_android.R
import xyz.kakeigakuen.kakeityan_agent_android.model.BookSectionBody
import xyz.kakeigakuen.kakeityan_agent_android.model.BookSectionHeader
import xyz.kakeigakuen.kakeityan_agent_android.model.IndexPath
import java.text.SimpleDateFormat

/**
 * Created by tomoya on 18/02/14.
 */
class BaseSectionAdapter(val context: Context, val header: List<BookSectionHeader>, val body: List<List<BookSectionBody>>): BaseAdapter(){
    val INDEX_PATH_ROW_HEAD = -1
    val ITEM_VIEW_TIPE_HEADER = 0
    val ITEM_VIEW_TYPE_ROW = 1

    val layoutinfater: LayoutInflater
    val IndexPathList: List<IndexPath>

    init {
        layoutinfater = LayoutInflater.from(context)
        IndexPathList = getIndexPathList(header, body)
    }

    override  fun getCount(): Int {
        return IndexPathList.size
    }

    override fun getItem(position: Int): Any {
        val indexpath = IndexPathList.get(position)
        if (isHeader(indexpath)) {
            return header.get(indexpath.section)
        } else {
            return body.get(indexpath.section).get(indexpath.row)
        }
    }

    override fun getItemId(position: Int): Long {
        return position as Long
    }

    override fun getView(position: Int, inconvertView: View?, parent: ViewGroup?): View {
        val indexPath = IndexPathList.get(position)
        // ヘッダー行とデータ行とで分岐します。
        if (isHeader(indexPath)) {
            return ViewSetHeader(inconvertView, parent, indexPath.section);
        } else {
            return ViewSetBody(inconvertView, parent, indexPath);
        }
    }

    fun getIndexPathList(sectionlists: List<BookSectionHeader>, rowlists: List<List<BookSectionBody>>): List<IndexPath> {
        var indexPathList = ArrayList<IndexPath>()
        var i = 0
        var j = 0
        sectionlists.forEach {
            var sectionIndexPath = IndexPath(i, INDEX_PATH_ROW_HEAD)
            indexPathList.add(sectionIndexPath)
            rowlists[i].forEach {
                var rowIndexPath = IndexPath(i, j)
                indexPathList.add(rowIndexPath)
                j++
            }
            i++
            j = 0
        }

        return indexPathList
    }

    fun isHeader(postion: Int): Boolean {
        val indexpath = IndexPathList.get(postion)
        return isHeader(indexpath)
    }

    fun isHeader(indexpath: IndexPath): Boolean {
        if (INDEX_PATH_ROW_HEAD == indexpath.row) {
            return true
        } else {
            return false
        }
    }

    public fun ViewSetHeader(convertView: View?, parent: ViewGroup?, section: Int): View {
        val sdf = SimpleDateFormat("MM-dd")
        var returnConvertView = convertView
        returnConvertView = layoutinfater.inflate(R.layout.headergenerator, parent, false)
        returnConvertView.findViewById<TextView>(R.id.date).text = sdf.format(header.get(section).data).toString()
        return returnConvertView
    }

    public fun ViewSetBody(convertView: View?, parent: ViewGroup?, indexpath: IndexPath) : View {
        var returnConvertView = convertView
        returnConvertView = layoutinfater.inflate(R.layout.bodygenerator, parent, false)
        returnConvertView.findViewById<TextView>(R.id.item).text = body.get(indexpath.section).get(indexpath.row).item.toString()
        returnConvertView.findViewById<TextView>(R.id.cost).text = body.get(indexpath.section).get(indexpath.row).cost.toString()
        return returnConvertView
    }

    override public fun getItemViewType(position: Int): Int {
        return 2
    }

    override public fun isEnabled(position: Int): Boolean {
        if (isHeader(position)) {
            // ヘッダー行の場合は、タップできないようにします。
            return false;
        } else {
            return super.isEnabled(position);
        }
    }
}