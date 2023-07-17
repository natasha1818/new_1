package ru.netology.nmedia.dto

data class Post(
    var id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val authorAvatar: String? = null,
)
 object Attachment {
    val url: String? = null
    val description: String? = null
    val type: String? = null
}


