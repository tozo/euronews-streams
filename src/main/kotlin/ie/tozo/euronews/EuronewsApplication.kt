package ie.tozo.euronews

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties
class EuronewsApplication

fun main(args: Array<String>) {
    runApplication<EuronewsApplication>(*args)
}