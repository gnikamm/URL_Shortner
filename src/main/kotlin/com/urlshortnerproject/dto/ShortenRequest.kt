package com.urlshortnerproject.dto

import com.urlshortnerproject.model.ShortenStrategy

data class ShortenRequest(val url: String, val strategy: ShortenStrategy)