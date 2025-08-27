/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 3:20 PM-01/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.configs.grpc.registor

class GrpcAuthorizationInterceptorManager(vararg clazzRegister: Class<*>) {
    private val mapper = mutableMapOf<String, Class<*>>()

    init {
        clazzRegister.forEach {
            mapper[it.name] = it
        }
    }

    fun getClass(className: String): Class<*>? {
        return mapper.getOrDefault(className, null)
    }
}
