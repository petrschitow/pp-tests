package com.iceye

import org.junit.jupiter.api.Test

class HelloServiceTests {

    @Parameterized.Parameters(name = "Test {index}: update cart with param " +
            "promoEndTime = {0} " +
            "returnedPromoTime = {1} " +
            "promoPrice = {2} " +
            "returnedPromoPrice = {3}")
    fun testData(): Collection<Any>{

    }

    @Test
    fun test(){

    }
}