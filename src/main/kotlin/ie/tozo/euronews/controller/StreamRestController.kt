package ie.tozo.euronews.controller

import ie.tozo.euronews.model.Streams
import ie.tozo.euronews.service.FetchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StreamRestController(val fetchService: FetchService) {

    @GetMapping("/streams")
    fun getStreamUrl(): Streams {
        return Streams(fetchService.getUrls().map { it.key.code to it.value }.toMap())
    }
}