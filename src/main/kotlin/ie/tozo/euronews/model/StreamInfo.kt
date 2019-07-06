package ie.tozo.euronews.model

import com.fasterxml.jackson.annotation.JsonProperty

data class StreamInfo(@JsonProperty("status") val status: String,
                      @JsonProperty("protocol") val protocol: String,
                      @JsonProperty("primary") val primary: String,
                      @JsonProperty("backup") val backup: String)