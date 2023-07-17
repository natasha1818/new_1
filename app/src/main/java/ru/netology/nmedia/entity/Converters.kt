package ru.netology.nmedia.entity

import androidx.room.TypeConverter
import ru.netology.nmedia.dto.Attachment

class Converters {
    @TypeConverter
    fun attachmentToString(attchment:Attachment) = "$attchment"
    @TypeConverter
    fun stringToAttachment(value:String) {
        val url = value.substringBefore(':')
        val description = value.substringBefore(':')
        val type= value.substringAfter(':')
    }
}