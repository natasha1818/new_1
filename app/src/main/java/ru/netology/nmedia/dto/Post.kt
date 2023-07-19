package ru.netology.nmedia.dto

import ru.netology.nmedia.entity.AttachmentType

data class Post(
    var id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val authorAvatar: String? = null,
    val attachment: Attachment? = null,
)
data class Attachment(
    var url: String,
    var type: AttachmentType,
)


