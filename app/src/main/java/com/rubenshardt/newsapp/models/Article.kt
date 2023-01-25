package com.rubenshardt.newsapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rubenshardt.newsapp.utils.Constants
import kotlinx.parcelize.Parcelize

@Entity(tableName = Constants.ARTICLES_TABLE)
@Parcelize
data class Article(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val author: String,
    val content: String,
    val date: String,
    val imageUrl: String,
    val readMoreUrl: String?,
    val time: String,
    val title: String,
    val url: String?,
    var category: String,
    var read: Boolean = false
) : Parcelable