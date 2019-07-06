package ie.tozo.euronews.service

import ie.tozo.euronews.model.Language

interface FetchService {
    fun fetchUrl()

    fun getUrls(): Map<Language, String>
}