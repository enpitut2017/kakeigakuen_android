package xyz.kakeigakuen.kakeityan_agent_android.util

import xyz.kakeigakuen.kakeityan_agent_android.model.Book
import xyz.kakeigakuen.kakeityan_agent_android.model.BookSectionBody
import xyz.kakeigakuen.kakeityan_agent_android.model.BookSectionHeader
import java.text.SimpleDateFormat

/**
 * Created by tomoya on 18/02/14.
 */
class SectionGenerator{
    val listHeader: List<BookSectionHeader>
    val listBody: List<MutableList<BookSectionBody>>

    constructor(books: Array<Book>) {
        listHeader = mutableListOf<BookSectionHeader>()
        listBody = mutableListOf<MutableList<BookSectionBody>>()

        var now = ""
        var count = -1
        val sdf = SimpleDateFormat("MM-dd")
        var time = ""

        books.forEach {
            time = sdf.format(it.time).toString()
            if (now != time) {
                listHeader.add(BookSectionHeader(it.time))
                count++
                listBody.add(mutableListOf<BookSectionBody>())
                now = time
            }
            listBody[count].add(BookSectionBody(it.item.toString(), it.cost.toString()))
        }
    }
}