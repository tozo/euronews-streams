package ie.tozo.euronews.config

import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
class ApplicationConfig {

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        val clientHttpRequestFactory = HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build())
        return builder.requestFactory { clientHttpRequestFactory }.build()
    }
}