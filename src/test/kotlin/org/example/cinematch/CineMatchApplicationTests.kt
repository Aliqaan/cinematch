package org.example.cinematch

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(properties = ["spring.flyway.enabled=false"])
class CineMatchApplicationTests {
    @Test
    fun contextLoads() {
    }
}
