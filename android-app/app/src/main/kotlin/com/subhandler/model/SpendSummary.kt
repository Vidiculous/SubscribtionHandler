package com.subhandler.model

data class SpendSummary(
    val monthly: Double = 0.0,
    val yearly: Double = 0.0,
    val currency: String = "USD",  // "Mixed" if multiple currencies
    val count: Int = 0
)
