package com.rirfee.basic

import com.rirfee.Config
import work.socialhub.kbsky.BlueskyFactory
import work.socialhub.kbsky.api.entity.app.bsky.feed.*
import work.socialhub.kbsky.api.entity.com.atproto.server.ServerCreateSessionRequest
import work.socialhub.kbsky.api.entity.com.atproto.server.ServerCreateSessionResponse
import work.socialhub.kbsky.domain.Service.BSKY_SOCIAL
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsFeedViewPost
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsPostView
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsReasonRepost

/**
 * ログイン
 * @return accessJwt
 */
fun login(): ServerCreateSessionResponse {
    val res = BlueskyFactory
        .instance(BSKY_SOCIAL.uri)
        .server()
        .createSession(
            ServerCreateSessionRequest().also {
                it.identifier = Config.BSKY_IDENTIFIER
                it.password = Config.BSKY_PASSWORD
            }
        )
    return res.data
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
URI:${post.uri}
CID:${post.cid}
"""
    println(text)
    printEmbedImages(post)
    printEmbedExternal(post)
}

/**
 * リポスト表示
 *
 * @param reason
 */
fun printRepost(reason: FeedDefsReasonRepost?) {
    if (reason == null) return
    println("reposted by ${reason.by?.displayName} (${reason.by?.handle})")
}

fun printEmbedImages(post: FeedDefsPostView) {
    if (post.embed?.asImages == null) return
    val images = post.embed?.asImages?.images
    println("IMAGES:")
    images?.forEach {
        println(it.thumb)
    }
}

fun printEmbedExternal(post: FeedDefsPostView) {
    if (post.embed?.asExternal == null) return
    val external = post.embed?.asExternal?.external
    println("EXTERNAL:")
    println("title: ${external?.title}\nthumb: ${external?.thumb}\nlink: ${external?.uri}")
}

fun basicSample() {
    val res = login()
    val accessJwt = res.accessJwt

    val sampleUri = "at://did:plc:vgr7plhqeczx7jgulo4psukh/app.bsky.feed.post/3kyminlmrcz2z"

    // タイムラインの取得
    val timeline = getTimeline(accessJwt, 10)
    timeline.forEach {
        printPost(it.post)
        printRepost(it.reason)
    }

    // 投稿
    post(accessJwt, "サンプル投稿")

    // 指定した投稿を取得
    val posts = getPosts(accessJwt, listOf(sampleUri))
    posts.forEach {
        printPost(it)
    }
}

fun main() {
    basicSample()
}