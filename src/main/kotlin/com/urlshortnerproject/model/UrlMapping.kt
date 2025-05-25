package com.urlshortnerproject.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class UrlMapping(

    @Column(nullable = false)
    var shortCode: String,

    @Column(nullable = false)
    var originalUrl: String

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var urlNr: Int = 0
}
