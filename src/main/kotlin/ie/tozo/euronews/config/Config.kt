package ie.tozo.euronews.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("app")
data class Config(var checkStreamCron: String = "",
                  var euronewsUrl: String = "")