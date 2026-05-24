package com.nestly.backend.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api/health")
class HealthController {
    @GetMapping
    fun health(): Map<String, Any> {
        return mapOf(
            "status" to "ok",
            "timestamp" to Instant.now(),
        )
    }
}
