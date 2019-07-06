package ie.tozo.euronews.scheduler

import ie.tozo.euronews.service.FetchService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Component
class StreamScheduler(private val restTemplate: RestTemplate,
                      private val fetchService: FetchService) {

    private val logger: Logger = LoggerFactory.getLogger(StreamScheduler::class.java)

    @Scheduled(cron = "#{@config.checkStreamCron}")
    internal fun checkStreamUrl() {
        logger.info("checking stream urls")

        for (entry in fetchService.getUrls()) {
            try {
                logger.info("checking ${entry.value}")
                val entity = restTemplate.exchange(entry.value, HttpMethod.GET, null, String::class.java)

                if (!entity.statusCode.is2xxSuccessful) {
                    logger.info("couldn't lookup stream url [statusCode: ${entity.statusCode}], fetching everything again")
                    fetchService.fetchUrl()
                    break
                }
            } catch (rce: RestClientException) {
                logger.error("error while trying to lookup stream url, fetching everything", rce)
                fetchService.fetchUrl()
                break
            }
        }
        logger.info("stream urls look ok")
    }
}