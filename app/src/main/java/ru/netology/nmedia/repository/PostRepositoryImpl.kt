package ru.netology.nmedia.repository

import android.annotation.SuppressLint

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository.*


class PostRepositoryImpl : PostRepository {
    fun getAll():List<Post>{
        return ApiService.api.getPosts()
            .execute()
            .let{
                if(!it.isSuccessful){
                 error("Response code is ${it.code()}")
                }
                it.body()?: throw RuntimeException("body is null")
            }
    }
    override fun getAllAsync(callback: RepositoryCallback<List<Post>>) {
    ApiService.api.getPosts()
             .enqueue(object : Callback <List<Post>>{
                 override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                     if(!response.isSuccessful){
                         callback.onError(RuntimeException(response.errorBody()?.string()))
                         return
                     }
                     val posts = response.body()
                     if (posts == null){
                         callback.onError(RuntimeException("No posts"))
                         return

                     }
                     callback.onSuccess(posts)
                 }

                 override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                     callback.onError(java.lang.Exception(t))
                 }
             })
    }
    override fun saveAsync(callback: RepositoryCallback<Post>) {
 ApiService.api.savePosts()
            .enqueue(object : Callback <Post>{
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.errorBody()?.string()))
                        return
                    }
                    val post = response.body()
                    if (post == null) {
                        callback.onError(RuntimeException("No posts"))
                        return
                    }
                    callback.onSuccess(post)
                }
                  override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(java.lang.Exception(t))
                }
            })

            }

    override fun removeByIdAsync(id: Long, callback: RepositoryCallback<Unit>) {
        ApiService.api.deletePosts(id)
            .enqueue(object:Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.errorBody()?.string()))
                        return
                    }
                        callback.onSuccess(Unit)
                }
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(RuntimeException("failed to delete post"))

                }
            })
    }

    override fun likeByIbAsync(
        id: Long,
        likedByMe: Boolean,
        callback: RepositoryCallback<Post>
    ) {
        when (likedByMe) {
            true -> deleteLike(id, callback)
            else -> liked(id, callback)
        }
    }


        fun liked(id: Long, callback: RepositoryCallback<Post>) {
   ApiService.api.likeById(id)
            .enqueue(object : Callback<Post> {

                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                           if (!response.isSuccessful) {
                               callback.onError(RuntimeException(response.errorBody()?.string()))
                               return
                    } else {
                        callback.onSuccess(response.body()!!)
                    }
                }
                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(RuntimeException("failed to like"))
                }
            })

    }

    fun deleteLike(id: Long, callback: RepositoryCallback<Post>) {
         ApiService.api.unLikeById(id)
             .enqueue(object :Callback<Post>{
                 override fun onResponse(call: Call<Post>, response: Response<Post>) {
                     if (!response.isSuccessful) {
                         callback.onError(RuntimeException(response.errorBody()?.string()))
                         return
                     } else {
                         callback.onSuccess(response.body()!!)
                 }}

                 override fun onFailure(call: Call<Post>, t: Throwable) {
                     callback.onError(RuntimeException("not delete like"))
                 }


             })
    }
}