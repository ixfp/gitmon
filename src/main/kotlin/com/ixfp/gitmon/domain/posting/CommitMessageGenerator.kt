package com.ixfp.gitmon.domain.posting

object CommitMessageGenerator {
    fun createMessage(title: String): String {
        return "Create \"$title\" - by Gitmon 👾"
    }

    fun updateMessage(title: String): String {
        return "Update \"$title\" - by Gitmon 👾"
    }

    fun deleteMessage(title: String): String {
        return "Delete \"$title\" - by Gitmon 👾"
    }

    fun imageUploadMessage(): String {
        return "Upload image - by Gitmon 👾"
    }
}
