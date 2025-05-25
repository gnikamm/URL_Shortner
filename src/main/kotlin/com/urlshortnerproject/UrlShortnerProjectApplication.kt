package com.urlshortnerproject

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UrlShortnerProjectApplication

fun main(args: Array<String>) {
	runApplication<UrlShortnerProjectApplication>(*args)
}
