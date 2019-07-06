package ie.tozo.euronews.service.impl

import ie.tozo.euronews.config.Config
import ie.tozo.euronews.model.Language
import ie.tozo.euronews.model.StreamInfo
import ie.tozo.euronews.model.StreamLive
import ie.tozo.euronews.service.FetchService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.util.*
import kotlin.collections.HashMap

@Service
class FetchServiceImpl(private val restTemplate: RestTemplate,
                       private val config: Config) : FetchService {

    private val logger: Logger = LoggerFactory.getLogger(FetchServiceImpl::class.java)

    private val urls: MutableMap<Language, String> = HashMap(Language.values().size)
    private val protocol = "https:"

    override fun fetchUrl() {
        logger.info("fetching urls")

        for (lang in Language.values()) {
            var languageCode = lang.code
            if (lang == Language.ENGLISH) {
                // if it's english, replace it with www
                languageCode = "www"
            }
            val url = protocol + "//" + languageCode + "." + config.euronewsUrl
            logger.info("calling $url")
            try {
                val streamLive = restTemplate.getForObject(url, StreamLive::class.java)
                if (streamLive != null) {
                    logger.info("StreamLive is not null, url: [${streamLive.url}]")
                    val streamInfo = restTemplate.getForObject(protocol + streamLive.url, StreamInfo::class.java)
                    if (streamInfo != null) {
                        logger.info("StreamInfo is not null, primary: [${streamInfo.primary}]")
                        urls[lang] = streamInfo.primary
                    }
                }
            } catch (rce: RestClientException) {
                logger.error("Couldn't call euronews API, skipping the rest of the calls until the next scheduled attempt", rce)
                break
            }
        }
    }

    override fun getUrls(): Map<Language, String> {
        return Collections.unmodifiableMap(urls)
    }
}