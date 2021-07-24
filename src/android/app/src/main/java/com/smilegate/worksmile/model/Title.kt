package com.smilegate.worksmile.model

data class Title(val title: String) : BaseItem() {
    override fun getViewType(): Int {
        return BaseItemViewType.TITLE.viewType
    }
}
