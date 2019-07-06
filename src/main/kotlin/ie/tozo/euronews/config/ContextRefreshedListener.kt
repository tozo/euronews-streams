package ie.tozo.euronews.config

import ie.tozo.euronews.service.FetchService
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener

@Configuration
@Profile("!test")
class ContextRefreshedListener(val fetchService: FetchService) {

    @EventListener(ContextRefreshedEvent::class)
    fun contextRefreshedEvent() {
        fetchService.fetchUrl()
    }
}