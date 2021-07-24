package com.smilegate.worksmile.model.chat


import com.google.gson.annotations.SerializedName

data class SlangList(
    @SerializedName("forbidden_words")
    var slangList: List<Slang>
)

data class Slang(
    @SerializedName("wid")
    var wid: Int,
    @SerializedName("word")
    var word: String
)