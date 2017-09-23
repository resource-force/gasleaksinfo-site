package ing.leakfix.site.data

import java.time.LocalDate

/**
 * Represents the length of time that a GasLeakSource covers and is valid for
 */
data class DataValidityRange(val start: LocalDate, val end: LocalDate) {
    init {
        if (!start.isBefore(end)) {
            throw IllegalArgumentException("start date must be before end date")
        }
    }
}