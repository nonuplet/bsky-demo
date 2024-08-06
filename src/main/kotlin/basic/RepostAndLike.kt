package com.rirfee.basic

import work.socialhub.kbsky.BlueskyFactory
import work.socialhub.kbsky.api.entity.app.bsky.feed.*
import work.socialhub.kbsky.domain.Service.BSKY_SOCIAL
import work.socialhub.kbsky.model.app.bsky.actor.ActorDefsProfileView
import work.socialhub.kbsky.model.app.bsky.feed.FeedGetLikesLike
import work.socialhub.kbsky.model.com.atproto.repo.RepoStrongRef

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
 * 特定のユーザがリポストを行ったかどうか
 *
 * @param accessJwt
 * @param uri
 * @param handle
 * @return
 */
fun isReposted(accessJwt: String, uri: String, handle: String): Boolean {
    val reposts = getRepostedBy(accessJwt, uri)
    return reposts.any { it.handle == handle }
}

/**
 * リポストする
 *
 * @param accessJwt
 * @param uri
 * @param cid
 * @return
 */
fun repost(accessJwt: String, uri: String, cid: String): FeedRepostResponse {
    val ref = RepoStrongRef(uri, cid)
    val res = BlueskyFactory
        .instance(BSKY_SOCIAL.uri)
        .feed()
        .repost(
            FeedRepostRequest(accessJwt).also {
                it.subject = ref
            }
        )
    return res.data
}

/**
 * リポストを削除
 *　TODO: これだけだと削除が成功したか判別できない、無効なアドレスを送ってもそのまま通ってしまう(リポストは実行されない)
 * @param accessJwt
 * @param repostUri (app.bsky.feed.repostのアドレスが必要)
 */
fun deleteRepost(accessJwt: String, repostUri: String) {
    BlueskyFactory
        .instance(BSKY_SOCIAL.uri)
        .feed()
        .deleteRepost(
            FeedDeleteRepostRequest(accessJwt).also {
                it.uri = repostUri
            }
        )
    println("repost deleted")
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
 * お気に入り
 *
 * @param accessJwt
 * @param uri
 * @param cid
 * @return
 */
fun like(accessJwt: String, uri: String, cid: String): FeedLikeResponse {
    val ref = RepoStrongRef(uri, cid)
    val res = BlueskyFactory
        .instance(BSKY_SOCIAL.uri)
        .feed()
        .like(
            FeedLikeRequest(accessJwt).also {
                it.subject = ref
            }
        )
    return res.data
}

/**
 * お気に入りの削除
 * TODO: deleteRepost()と同様に、削除が成功したか判別できない
 * @param accessJwt
 * @param likeUri (app.bsky.feed.likeのアドレスが必要)
 */
fun deleteLike(accessJwt: String, likeUri: String) {
    BlueskyFactory
        .instance(BSKY_SOCIAL.uri)
        .feed()
        .deleteLike(
            FeedDeleteLikeRequest(accessJwt).also {
                it.uri = likeUri
            }
        )
    println("like deleted")
}

/**
 * 特定のユーザがお気に入りしたかどうか
 *
 * @param accessJwt
 * @param uri
 * @param handle
 * @return
 */
fun isLiked(accessJwt: String, uri: String, handle: String): Boolean {
    val likes = getLikes(accessJwt, uri)
    return likes.any { it.actor.handle == handle }
}

fun sampleRepostAndLike() {
    val res = login()
    val accessJwt = res.accessJwt
    val handle = res.handle

    val sampleUri = "at://did:plc:vgr7plhqeczx7jgulo4psukh/app.bsky.feed.post/3kyminlmrcz2z"
    val sampleCid = "bafyreihdder7nuplhsypjhdtbv62vd2dmiy46amv7panqqbxfbkgie5zpq"

    // リポスト一覧を取得
    println("***** getRepostedBy *****")
    getRepostedBy(accessJwt, sampleUri).forEach {
        println("${it.handle} ... ${it.indexedAt}")
    }

    // リポストする
    println("***** repost *****")
    val repostRes = repost(accessJwt, sampleUri, sampleCid)
    println("reposted uri: ${repostRes.uri}")

    // 自分がリポストしたか判定
    println("***** isReposted *****")
    println("reposted? : ${isReposted(accessJwt, sampleUri, handle)}")

    // リポスト削除
    deleteRepost(accessJwt, repostRes.uri)

    // お気に入りの取得
    println("***** getLikes *****")
    getLikes(accessJwt, sampleUri).forEach {
        println("${it.actor.handle} ... ${it.indexedAt}")
    }

    // お気に入りする
    val resLike = like(accessJwt, sampleUri, sampleCid)
    println("liked uri: ${resLike.uri}")

    // 自分がお気に入りしたか判定
    println("***** isLiked *****")
    println("liked? : ${isLiked(accessJwt, sampleUri, handle)}")

    // お気に入り削除
    deleteLike(accessJwt, resLike.uri)
}

fun main() {
    sampleRepostAndLike()
}