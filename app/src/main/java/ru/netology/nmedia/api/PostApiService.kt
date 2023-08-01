package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://10.0.2.2:9999/api/slow/"
private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()


interface PostApiService {
    @GET("posts")
    fun getPosts(): Call<List<Post>>

    @DELETE("posts/{id}")
    fun deletePosts(@Path("id") id: Long): Call<Unit>

    @POST("posts")
    fun savePosts(): Call<Post>

    @DELETE("posts/{id}/likes")
    fun unLikeById(@Path("id") id: Long): Call<Post>

    @POST("posts/{id}/likes")
    fun likeById(@Path("id") id: Long): Call<Post>
}

object ApiService{
    val api: PostApiService by lazy{
        retrofit.create(PostApiService::class.java)

    }
}