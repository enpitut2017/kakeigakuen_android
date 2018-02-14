package xyz.kakeigakuen.kakeityan_agent_android.client

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable
import xyz.kakeigakuen.kakeityan_agent_android.model.List

/**
 * Created by paseri on 2018/02/02.
 */
interface ListClient {

    @FormUrlEncoded

    @POST("/api/book_list")
    fun update(@Field("token") token: String): Observable<List>
}