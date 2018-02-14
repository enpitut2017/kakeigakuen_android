package xyz.kakeigakuen.kakeityan_agent_android.model

/**
 * Created by paseri on 2018/02/02.
 */
data class List(
        val error: Boolean,
        val message: Map<String, String>,
        val token: String,
        val list: Array<Book>
)