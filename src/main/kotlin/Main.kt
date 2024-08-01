package com.rirfee

fun main() {
    val accessJwt = login()

    val posts = getTimeline(accessJwt, 1)
    posts.forEach {
        printPost(it.post)
    }
}