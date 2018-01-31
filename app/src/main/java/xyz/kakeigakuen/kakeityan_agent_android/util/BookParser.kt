package xyz.kakeigakuen.kakeityan_agent_android.util

/**
 * Created by tomoya on 18/01/31.
 */
class BookParser {

    var item: String
    var cost: String

    constructor(str: String) {
        var str_tmp = str
        str_tmp = str_tmp.replace(Regex("""\s|　|\.|,|円"""), "")
        str_tmp = str_tmp.replace(Regex("""万"""), "0000")
        str_tmp = str_tmp.replace(Regex("""千"""), "000")
        item = str_tmp.replace(Regex("""\d+"""), "")
        cost = str_tmp.replace(Regex("""\D+"""), "")
    }
}