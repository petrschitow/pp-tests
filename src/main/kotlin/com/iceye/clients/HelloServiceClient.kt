package com.iceye.clients

import com.iceye.constants.Endpoints.HELLO_SERVICE_URL
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When

fun sendRequestToHelloService(name: String): String {
    return Given {
        baseUri("$HELLO_SERVICE_URL/$name")
    } When {
        get()
    } Then {
        statusCode(200)
    } Extract {
        response().toString()
    }
}