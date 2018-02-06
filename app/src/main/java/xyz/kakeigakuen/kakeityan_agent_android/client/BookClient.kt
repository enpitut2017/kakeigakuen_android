package xyz.kakeigakuen.kakeityan_agent_android.client

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable
import xyz.kakeigakuen.kakeityan_agent_android.model.User

/**
 * Created by paseri on 2018/01/20.
 */

interface BookClient {

    @FormUrlEncoded

    @POST("/api/books")
    fun post(@Field("costs") costs: String, @Field("items") item: String, @Field("token") token: String): Observable<User>
}