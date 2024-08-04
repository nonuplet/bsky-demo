package com.rirfee

fun main() {
    val accessJwt = login()

    val timeline = getTimeline(accessJwt, 3)
    timeline.forEach {
        printPost(it.post)
    }

    val uri = "at://did:plc:vgr7plhqeczx7jgulo4psukh/app.bsky.feed.post/3kyminlmrcz2z"

    val posts = getPosts(accessJwt, listOf(uri))
    posts.forEach {
        printPost(it)
    }

    val likes = getLikes(accessJwt, uri)
    println(likes.filter { it.actor.handle == "99noe-test.bsky.social" }.map {
        """
            actor: ${it.actor.displayName} (${it.actor.handle})
            createdAt: ${it.createdAt}
            indexedAt: ${it.indexedAt}
        """.trimIndent()
    })

    val reposts = getRepostedBy(accessJwt, uri)
    println(reposts.filter { it.handle == "99noe-test.bsky.social" }.map {
        """
            actor: ${it.displayName} (${it.handle})
            indexedAt: ${it.indexedAt}
        """.trimIndent()
    })
}