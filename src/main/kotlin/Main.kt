package com.rirfee

import com.rirfee.basic.*

fun main() {
    val res = login()
    val accessJwt = res.accessJwt
    val handle = res.handle

    val sampleUri = "at://did:plc:vgr7plhqeczx7jgulo4psukh/app.bsky.feed.post/3kyminlmrcz2z"
    val sampleCid = "bafyreihdder7nuplhsypjhdtbv62vd2dmiy46amv7panqqbxfbkgie5zpq"
}