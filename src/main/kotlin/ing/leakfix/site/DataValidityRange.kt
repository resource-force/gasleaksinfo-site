package ing.leakfix.site

import java.time.LocalDate

data class DataValidityRange(val start: LocalDate, val end: LocalDate) {
    init {
        if (!start.isBefore(end)) {
            throw IllegalArgumentException("start date must be before end date")
        }
    }
}