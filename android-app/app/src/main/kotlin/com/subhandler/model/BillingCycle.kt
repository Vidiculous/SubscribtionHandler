package com.subhandler.model

enum class BillingCycle(val label: String) {
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly"),
    CUSTOM("Custom");

    companion object {
        fun fromString(value: String): BillingCycle =
            entries.firstOrNull { it.name == value } ?: MONTHLY
    }
}

enum class CustomCycleUnit(val label: String) {
    DAYS("days"),
    WEEKS("weeks"),
    MONTHS("months");

    companion object {
        fun fromString(value: String?): CustomCycleUnit =
            entries.firstOrNull { it.name == value } ?: MONTHS
    }
}
