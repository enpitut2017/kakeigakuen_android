package xyz.kakeigakuen.kakeityan_agent_android.model

/**
 * Created by paseri on 2018/01/20.
 */
data class User(
        val error: Boolean,
        val message: Map<String, String>,
        val token: String,
        val rest: Int,
        val budget: Int
)