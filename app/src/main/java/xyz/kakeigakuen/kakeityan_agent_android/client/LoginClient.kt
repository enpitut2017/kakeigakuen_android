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

    @POST("/api/login")
    fun login(@Query("email") email: String, @Query("password") password: String): Observable<User>
}
