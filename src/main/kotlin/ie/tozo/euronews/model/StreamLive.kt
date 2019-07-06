package ie.tozo.euronews.model

import com.fasterxml.jackson.annotation.JsonProperty

data class StreamLive(@JsonProperty("url") val url: String)