package com.booksy.data.models

import com.google.gson.annotations.SerializedName

// modelo para api de google books
data class GoogleBooksResponse(
    val items: List<GoogleBookItem>? = null
)

data class GoogleBookItem(
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String,
    val authors: List<String>? = null,
    @SerializedName("imageLinks")
    val imageLinks: ImageLinks? = null,
    val description: String? = null
)

data class ImageLinks(
    val thumbnail: String? = null
)