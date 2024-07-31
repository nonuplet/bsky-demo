package com.rirfee

import work.socialhub.kbsky.BlueskyFactory
import work.socialhub.kbsky.api.entity.com.atproto.server.ServerCreateSessionRequest
import work.socialhub.kbsky.domain.Service.BSKY_SOCIAL

fun main() {
    val bsky = BlueskyFactory
        .instance(BSKY_SOCIAL.uri)
        .server()
        .createSession(
            ServerCreateSessionRequest().also {
                it.identifier = Config.BSKY_IDENTIFIER
                it.password = Config.BSKY_PASSWORD
            }
        )

    val accessJwt = bsky.data.accessJwt
}