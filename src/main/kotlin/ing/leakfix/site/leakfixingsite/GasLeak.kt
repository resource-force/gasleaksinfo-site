package ing.leakfix.site.leakfixingsite

import java.time.LocalDate

data class GasLeak(
        val location: String = "",
        val sourceVendor: String = "",
        val sourceDataset: String = "",
        val size: Int = 0,
        val status: GasLeakStatus = GasLeakStatus.FIXED,
        val reportedOn: LocalDate = LocalDate.now(),
        val fixedOn: LocalDate = LocalDate.now(),
        val ngridId: Int = 0)