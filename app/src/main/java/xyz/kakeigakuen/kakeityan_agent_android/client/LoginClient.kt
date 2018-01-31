package xyz.kakeigakuen.kakeityan_agent_android.client

import retrofit2.http.POST
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Query
import rx.Observable
import xyz.kakeigakuen.kakeityan_agent_android.model.User

/**
 * Created by paseri on 2018/01/20.
 */

interface LoginClient {

    @FormUrlEncoded

    @POST("/api/login")
    fun login(@Field("email") email: String, @Field("password") password: String): Observable<User>
}
