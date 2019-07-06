package ie.tozo.euronews.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import ie.tozo.euronews.model.Language
import ie.tozo.euronews.model.StreamInfo
import ie.tozo.euronews.model.StreamLive
import ie.tozo.euronews.service.FetchService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers
import org.springframework.test.web.client.response.MockRestResponseCreators
import org.springframework.web.client.RestTemplate

@SpringBootTest
@ExtendWith(SpringExtension::class)
class FetchServiceImplTest {

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var fetchService: FetchService

    private lateinit var server: MockRestServiceServer

    @BeforeEach
    fun setup() {
        server = MockRestServiceServer.createServer(restTemplate)

        Language.values().forEach { lang ->
            var languageCode = lang.code
            if (lang == Language.ENGLISH) {
                // if it's english, replace it with www
                languageCode = "www"
            }

            val watchLive = objectMapper.writeValueAsString(StreamLive("//$languageCode.euronews-test-url"))

            server.expect(MockRestRequestMatchers.requestTo("https://$languageCode.euronews.com/api/watchlive.json"))
                    .andRespond(MockRestResponseCreators.withSuccess(watchLive, MediaType.APPLICATION_JSON))

            val streamInfo = objectMapper.writeValueAsString(StreamInfo("$lang.status", "$lang.protocol", "$lang.primary", "$lang.backup"))
            server.expect(MockRestRequestMatchers.requestTo("https://$languageCode.euronews-test-url"))
                    .andRespond(MockRestResponseCreators.withSuccess(streamInfo, MediaType.APPLICATION_JSON))
        }
    }

    @Test
    fun `fetching all urls`() {
        fetchService.fetchUrl()

        val urls = fetchService.getUrls()

        Language.values().forEach { lang ->
            Assertions.assertEquals("$lang.primary", urls[lang])
        }
    }

    @Test
    fun `watchlive url fails`() {
        server.reset()
        server.expect(MockRestRequestMatchers.requestTo("https://www.euronews.com/api/watchlive.json"))
                .andRespond(MockRestResponseCreators.withBadRequest())

        fetchService.fetchUrl()
        Assertions.assertEquals(0, fetchService.getUrls().size)
    }
}