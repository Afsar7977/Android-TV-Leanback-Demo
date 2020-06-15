package com.afsar.kotlintv.Modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataSource(
    @SerializedName("googlevideos")
    @Expose
    var videoPojo: List<VideoPojo>
)