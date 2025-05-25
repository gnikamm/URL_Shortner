package com.urlshortnerproject.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class ShortUrl(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var urlNr: Long = 0,

    @Column(nullable = false, unique = true)
    var shortCode: String = "",

    @Column(nullable = false, unique = true)
    var originalUrl: String = "",

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val strategy: ShortenStrategy
)
