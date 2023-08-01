package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAllAsync(callback: RepositoryCallback<List<Post>>)
    fun saveAsync(callback: RepositoryCallback<Post>)
    fun removeByIdAsync(id: Long, callback: RepositoryCallback<Unit>)
    fun likeByIbAsync(id: Long, likedByMe: Boolean, callback: RepositoryCallback<Post>)
    interface RepositoryCallback<T> {
        fun onSuccess(value: T)
        fun onError(e:Exception)
    }
}


