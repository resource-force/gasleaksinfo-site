package ing.leakfix.site.data

data class GasLeakSource(
        val vendor: String,
        val dataset: String,
        val dataBetween: DataValidityRange)