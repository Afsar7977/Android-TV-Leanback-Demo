package com.afsar.kotlintv.Modal

import java.io.Serializable

data class VideosSource(
    var description: String,
    var sources: List<String>,
    var card: String,
    var background: String,
    var title: String,
    var studio: String
) : Serializable {

    override fun toString(): String {
        return "VideosSource{" +
                ", title='" + title + '\'' +
                ", videoUrl='" + sources + '\'' +
                ", backgroundImageUrl='" + background + '\'' +
                ", cardImageUrl='" + card + '\'' +
                '}'
    }
}
