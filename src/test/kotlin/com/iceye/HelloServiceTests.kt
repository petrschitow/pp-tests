package com.iceye

import com.iceye.clients.sendRequestToHelloService
import com.iceye.utils.getRandom
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.matchers.shouldBe

class HelloServiceTests : FunSpec() {

    init {
        table(
                headers("tag"),
                row("Peter"),
                row("PETER"),
                row("peter"),
                row("peter-${getRandom()}"),
                row("12334"),
                row(""),
                row("l")
        ).forAll { name ->
            test("Check hello-service with name $name") {
                val response = sendRequestToHelloService(name)
                response shouldBe "Hi there, ${name}!"
            }
        }
    }


}