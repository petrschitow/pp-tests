package com.iceye.clients

import com.iceye.constants.Endpoints.HELLO_SERVICE_URL
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When

fun sendRequestToHelloService(name: String): String {
    return Given {
        spec(RequestSpecBuilder()
                .addFilter(RequestLoggingFilter(LogDetail.URI))
                .setContentType(ContentType.JSON)
                .build())
        baseUri("$HELLO_SERVICE_URL/$name")
    } When {
        get()
    } Then {
        statusCode(200)
    } Extract {
        response().body.asString()
    }
}