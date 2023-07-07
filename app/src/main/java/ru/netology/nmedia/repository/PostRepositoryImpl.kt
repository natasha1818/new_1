package ru.netology.nmedia.repository

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository.*
import java.io.IOException

import java.util.concurrent.TimeUnit


class PostRepositoryImpl: PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}


    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

//    override fun getAll(): List<Post> {
//        val request: Request = Request.Builder()
//            .url("${BASE_URL}/api/slow/posts")
//            .build()
//        val posts: List<Post> = client.newCall(request)
//            .execute()
//            .let { it.body?.string() ?: throw RuntimeException("body is null") }
//            .let {
//                gson.fromJson(it, typeToken.type)
//            }
//        return posts
//    }
    override fun getAllAsync(callback: RepositoryCallback<List<Post>>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object :Callback{
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError()
                }
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string() ?: throw RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(body, typeToken.type))
                    } catch (e: Exception) {
                        callback.onError()
                    }
                }

            })
    }

//    override fun likeByIdAsync(id: Long,callback: RepositoryCallback<Post>):Post {}
//
//                  val post =  when (callbacklikedByMe) {
//            true -> deleteLike(id)
//        else-> liked(id)
//   }return post}

    override fun saveAsync(callback: RepositoryCallback<Post>) {

        val request: Request = Request.Builder()
           .post(gson.toJson(callback).toRequestBody(jsonType) )
          .url("${BASE_URL}/api/slow/posts")
         .build()

        client.newCall(request)
            .enqueue(object :Callback{
                override fun onFailure(call: Call, e: IOException) {
                   callback.onError()
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string() ?: throw RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(body,Post::class.java))
                    } catch (e: Exception) {
                        callback.onError()
                    }
                    }

            })
  }



   override fun removeByIdAsync(id: Long,callback: RepositoryCallback<Post>) {
       val request: Request = Request.Builder()
            .delete()
           .url("${BASE_URL}/api/slow/posts/$id")
           .build()
       client.newCall(request)
           .enqueue(object: Callback{
               override fun onFailure(call: Call, e: IOException) {
                   callback.onError()
               }

               override fun onResponse(call: Call, response: Response) {
                   if(!response.isSuccessful){
                       callback.onError()
                   }else{
                       val body = response.body?.string() ?: throw RuntimeException("body is null")
                       callback.onSuccess(gson.fromJson(body,typeToken.type))
                   }

               }

           } )
    }


    override fun likeByIbAsync(id: Long, callback: RepositoryCallback<Post>): Post {

        val post =  when (post.likedBeMe) {
            true -> deleteLike(id)
            else-> liked(id)
        }
        return post
    }
//    override fun save(post: Post) {
//        val request: Request = Request.Builder()
//            .post(gson.toJson(post).toRequestBody(jsonType))
//            .url("${BASE_URL}/api/slow/posts")
//            .build()
//
//        client.newCall(request)
//            .execute()
//            .close()
//    }
//
//    override fun removeById(id: Long) {
//        val request: Request = Request.Builder()
//            .delete()
//            .url("${BASE_URL}/api/slow/posts/$id")
//            .build()
//
//        client.newCall(request)
//            .execute()
//            .close()
//    }

       @SuppressLint("SuspiciousIndentation")
       fun liked (id:Long):Post{
        val request: Request = Request.Builder()
            .post(EMPTY_REQUEST)
            .url("${BASE_URL}/api/slow/posts/$id/likes")
            .build()
           return client.newCall(request)
               .execute()
               .let { it.body?.string() ?: throw RuntimeException("body is null") }
               .let {
                   gson.fromJson(it, Post::class.java)
               }
    }
    fun deleteLike(id:Long):Post{
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id/likes")
            .build()
        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, Post::class.java)
            }
           }
}
