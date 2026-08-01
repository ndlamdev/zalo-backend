/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:20 PM-23/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.services.redis

import com.lamnguyen.chatws.utils.properties.RSocketMetadataProperty
import com.lamnguyen.chatws.utils.redis.ICacheRedis
import reactor.core.publisher.Mono

interface IRSocketMetadataCacheManager : ICacheRedis<RSocketMetadataProperty> {
    fun cache(user: String, metadata: RSocketMetadataProperty): Mono<RSocketMetadataProperty>
    fun clear(user: String)
}