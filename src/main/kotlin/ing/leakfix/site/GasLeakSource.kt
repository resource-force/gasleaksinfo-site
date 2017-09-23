package ing.leakfix.site

import java.time.Period

data class GasLeakSource(
        val vendor: String,
        val dataset: String,
        val dataBetween: DataValidityRange)