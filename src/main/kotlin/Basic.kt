package com.rirfee

import work.socialhub.kbsky.BlueskyFactory
import work.socialhub.kbsky.api.entity.app.bsky.feed.*
import work.socialhub.kbsky.api.entity.com.atproto.server.ServerCreateSessionRequest
import work.socialhub.kbsky.domain.Service.BSKY_SOCIAL
import work.socialhub.kbsky.model.app.bsky.actor.ActorDefsProfileView
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsFeedViewPost
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsPostView
import work.socialhub.kbsky.model.app.bsky.feed.FeedGetLikesLike

/**
 * ログイン
 * @return accessJwt
 */
fun login(): String {
    val bsky = BlueskyFactory
        .instance(BSKY_SOCIAL.uri)
        .server()
        .createSession(
            ServerCreateSessionRequest().also {
                it.identifier = Config.BSKY_IDENTIFIER
                it.password = Config.BSKY_PASSWORD
            }
        )
    return bsky.data.accessJwt
}

/**
 * 投稿
 *
 * @param accessJwt accessJwt
 * @param text 投稿内容
 */
fun post(accessJwt: String, text: String) {
    BlueskyFactory
        .instance(BSKY_SOCIAL.uri)
        .feed()
        .post(
            FeedPostRequest(accessJwt).also { it.text = text }
        )
}

/**
 * タイムラインの取得
 *
 * @param accessJwt
 * @param limit 取得数
 * @return 取得したタイムライン (List)
 */
fun getTimeline(accessJwt: String, limit: Int = 10): List<FeedDefsFeedViewPost> {
    val res = BlueskyFactory
        .instance(BSKY_SOCIAL.uri)
        .feed()
        .getTimeline(
            FeedGetTimelineRequest(accessJwt).also {
                it.limit = limit
            }
        )

    return res.data.feed
}

/**
 * アドレスからポストの取得
 *
 * @param accessJwt
 * @param uris アドレス(at://)
 * @return
 */
fun getPosts(accessJwt: String, uris: List<String>): List<FeedDefsPostView> {
    val res = BlueskyFactory
        .instance(BSKY_SOCIAL.uri)
        .feed()
        .getPosts(
            FeedGetPostsRequest(accessJwt).also {
                it.uris = uris
            }
        )
    println(res.json)

    return res.data.posts
}

/**
 * リポスト一覧の取得
 *
 * @param accessJwt
 * @param uri
 * @return
 */
fun getRepostedBy(accessJwt: String, uri: String): List<ActorDefsProfileView> {
    val res = BlueskyFactory
        .instance(BSKY_SOCIAL.uri)
        .feed()
        .getRepostedBy(
            FeedGetRepostedByRequest(accessJwt).also {
                it.uri = uri
            }
        )

    return res.data.repostedBy
}

/**
 * お気に入りの取得
 *
 * @param accessJwt
 * @param uri
 * @return
 */
fun getLikes(accessJwt: String, uri: String): List<FeedGetLikesLike> {
    val res = BlueskyFactory
        .instance(BSKY_SOCIAL.uri)
        .feed()
        .getLikes(
            FeedGetLikesRequest(accessJwt).also {
                it.uri = uri
            }
        )
    return res.data.likes
}

/**
 * postの表示用関数
 * @param post Postのデータ (FeedDefsPostView)
 */
fun printPost(post: FeedDefsPostView) {
    val embeds = post.embed?.asImages?.images?.map { it.thumb }

    val text =
"""***** --------------- *****
[${post.author?.displayName} (${post.author?.handle})]
${post.record?.asFeedPost?.text}
💬${post.replyCount}  🔁${post.repostCount}  💕${post.likeCount}
EMBEDS:
${embeds?.joinToString(separator = "\n") { it.toString() } ?: "" }
URI:${post.uri}
"""
    println(text)
}



