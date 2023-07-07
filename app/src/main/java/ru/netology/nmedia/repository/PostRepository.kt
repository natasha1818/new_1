package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
//    fun getAll(): List<Post>
//    fun likeById(id: Long,likedByMe:Boolean): Post
//    fun save(post: Post)
//    fun removeById(id: Long)

    fun getAllAsync(callback: RepositoryCallback<List<Post>>)
  //  fun likeByIdAsync(id:Long,callback:RepositoryCallback<Post>):Post
    fun saveAsync(callback: RepositoryCallback<Post>)
    fun removeByIdAsync(id:Long,callback: RepositoryCallback<Post>)
    fun likeByIbAsync(id: Long,callback: RepositoryCallback<Post>):Post
    interface RepositoryCallback <T> {
        fun onSuccess(value: T)
        fun onError()
    }
}
