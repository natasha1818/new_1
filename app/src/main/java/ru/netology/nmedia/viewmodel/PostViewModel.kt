package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.util.SingleLiveEvent
import ru.netology.nmedia.repository.PostRepository.RepositoryCallback as RepositoryCallback1

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    likes = 0,
    published = "",
    authorAvatar = " ",
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {

        _data.postValue(FeedModel(loading = true))
        repository.getAllAsync(object : RepositoryCallback1<List<Post>> {
            override fun onSuccess(value: List<Post>) {
                _data.postValue(FeedModel(posts = value, empty = value.isEmpty()))
            }

            override fun onError() {
                _data.postValue(FeedModel(error = true))
            }

        })

    }
    fun save() {
        edited.value?.let {

            repository.saveAsync(object : RepositoryCallback1<Post> {
                override fun onSuccess(value: Post) {
                    _postCreated.postValue(Unit)
                }

                override fun onError() {
                    _data.postValue(FeedModel(error = true))
                }
            })
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(id: Long,likedByMe: Boolean) {
        val old = _data.value?.posts.orEmpty()

        repository.likeByIbAsync(id,likedByMe, object : RepositoryCallback1<Post> {
            override fun onSuccess(value: Post) {
                _data.postValue(_data.value?.copy(posts = old.map { post ->
                    if (post.id == id) value else post
                }))
            }
            override fun onError() {
                _data.postValue(_data.value?.copy(posts = old))
           }
        })
    }


    fun removeById(id: Long) {

        // Оптимистичная модель
        val old = _data.value?.posts.orEmpty()
        repository.removeByIdAsync(id, object : RepositoryCallback1<Unit> {
            override fun onSuccess(value: Unit) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .filter { it.id != id }
                    )
                )
            }

            override fun onError() {
                _data.postValue(_data.value?.copy(posts = old))
            }
        })
    }
}




