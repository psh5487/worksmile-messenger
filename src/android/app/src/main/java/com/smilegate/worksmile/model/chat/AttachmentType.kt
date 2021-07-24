package com.smilegate.worksmile.model.chat

enum class AttachmentType(val type: String, val viewType: Int) {
    IMAGE("image", 101), FILE("file", 102), LINK("link", 103), NOTIFY("notify", 104);

    companion object {
        fun type(type: String): AttachmentType {
            return values().firstOrNull { it.type == type } ?: IMAGE
        }
    }
}