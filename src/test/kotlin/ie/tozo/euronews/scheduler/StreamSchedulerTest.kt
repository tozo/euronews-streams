package ie.tozo.euronews.scheduler

import ie.tozo.euronews.model.Language
import ie.tozo.euronews.service.FetchService
import org.hamcrest.core.StringContains
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.client.ExpectedCount
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators
import org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestTemplate


@SpringBootTest
@ExtendWith(SpringExtension::class)
class StreamSchedulerTest {

    @Autowired
    private lateinit var streamScheduler: StreamScheduler

    @Autowired
    private lateinit var restTemplate: RestTemplate

    private lateinit var server: MockRestServiceServer

    @MockBean
    private lateinit var fetchService: FetchService

    @BeforeEach
    fun setup() {
        server = MockRestServiceServer.createServer(restTemplate)

        val urls = HashMap<Language, String>()
        urls[Language.ENGLISH] = "https://www.euronews.com"
        urls[Language.PORTUGUESE] = "https://pt.euronews.com"

        `when`(fetchService.getUrls()).thenReturn(urls)

        `when`(fetchService.fetchUrl()).then {
            println("fetching urls")
        }
    }

    @Test
    fun `all streams are available`() {
        server.expect(ExpectedCount.manyTimes(), requestTo(StringContains.containsString("euronews.com")))
                .andRespond(withSuccess("OK", MediaType.APPLICATION_JSON))

        streamScheduler.checkStreamUrl()

        verify(fetchService, times(0)).fetchUrl()
    }

    @Test
    fun `stream not available no exception`() {
        server.expect(ExpectedCount.manyTimes(), requestTo(StringContains.containsString("euronews.com")))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.MOVED_PERMANENTLY))

        streamScheduler.checkStreamUrl()

        verify(fetchService, times(1)).fetchUrl()
    }

    @Test
    fun `stream not available with exception`() {
        server.expect(ExpectedCount.manyTimes(), requestTo(StringContains.containsString("euronews.com")))
                .andRespond(withBadRequest())

        streamScheduler.checkStreamUrl()

        verify(fetchService, times(1)).fetchUrl()
    }
}