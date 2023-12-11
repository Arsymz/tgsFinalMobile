package com.example.recyclerview.data

import android.icu.text.CaseMap.Title
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Music(
    val gambar:Int,
    val judul:String,
    val data_deskripsi: String,
    val musicId:Int
):Parcelable