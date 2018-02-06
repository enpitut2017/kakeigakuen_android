package xyz.kakeigakuen.kakeityan_agent_android.util

/**
 * Created by paseri on 2018/02/06.
 */
class HashMapToString {
    fun ChangeToString(input: Map<String, String>): String {
        var st: String = ""
        input.forEach{
            st = st + it.value + "\n"
        }
        if (st != "") st = st.substring(0, st.length - 1)
        return st
    }
}