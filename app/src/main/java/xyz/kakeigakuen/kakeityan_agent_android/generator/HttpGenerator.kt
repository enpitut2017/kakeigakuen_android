package xyz.kakeigakuen.kakeityan_agent_android.generator

import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by paseri on 2018/01/20.
 */

class HttpGenerator {
    var gson: Gson
    var retrofit: Retrofit

    constructor() {
        Log.i("HttpGenerator", "setup start")
        gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
        retrofit = Retrofit.Builder()
                .baseUrl("https://kakeigakuen.xyz")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build()
        Log.i("HttpGenerator", "setup finish")
    }
}
