package com.lamnguyen.user.utils.redis

data class CacheResult<K, V>(val found: Map<K, V>, val missing: List<K>) {
}
