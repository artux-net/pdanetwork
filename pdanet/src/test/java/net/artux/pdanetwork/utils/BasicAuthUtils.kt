package net.artux.pdanetwork.utils

import org.springframework.http.RequestEntity

fun <T : RequestEntity.HeadersBuilder<T>?> RequestEntity.HeadersBuilder<T>.withBasicAuth(): T = this.headers {
    it.setBasicAuth("admin@artux.net", "pass")
}
