package com.afsar.kotlintv.Presenter

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter
import com.afsar.kotlintv.Modal.VideosSource

class DetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {

    override fun onBindDescription(
            viewHolder: AbstractDetailsDescriptionPresenter.ViewHolder,
            item: Any) {
        val videosSource = item as VideosSource

        viewHolder.title.text = videosSource.title
        viewHolder.subtitle.text = videosSource.studio
        viewHolder.body.text = videosSource.description
    }
}
