package com.lamnguyen.user.utils.annotation

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class GrpcPreAuthorizeHasAnyAuthority(
    /**
     * @return the Spring-EL expression to be evaluated before invoking the protected
     * method
     */
    vararg val value: String = []
)
