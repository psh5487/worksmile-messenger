package com.smilegate.worksmile.model

enum class BaseItemViewType(val viewType: Int) {
    EMPTY(0), TITLE(1), ROOM(2), USER(3);

    companion object {
        fun get(viewType: Int): BaseItemViewType {
            return values().firstOrNull { it.viewType == viewType } ?: EMPTY
        }
    }
}