package ing.leakfix.site.data

import java.time.LocalDate

data class GasLeak(
        val location: String,
        val sources: List<GasLeakSource>,
        val size: Int?,
        val status: GasLeakStatus,
        val reportedOn: LocalDate?,
        val fixedOn: LocalDate?,
        val ngridId: Int?) {
    val merged: Boolean

    init {
        if (sources.isEmpty()) {
            throw IllegalArgumentException("sources must have at least one entry")
        }
        merged = sources.size != 1
    }

    fun shouldMergeWith(leak: GasLeak): Boolean =
            location == leak.location && sources != leak.sources && !merged && !leak.merged

    private fun <T> selectNonNullOrCondition(
            one: T?, two: T?, condition: (T, T) -> T): T? {
        return if (one == null && two != null) {
            two
        } else if (one != null && two == null) {
            one
        } else if (one != null && two != null) {
            condition(one, two)
        } else {
            null
        }
    }

    fun mergeWith(other: GasLeak): GasLeak {
        if (!shouldMergeWith(other)) {
            throw IllegalArgumentException("Leaks should not be merged")
        }

        return GasLeak(
                location,
                sources + other.sources,
                // Pick the lowest size or the non-null one, or null.
                selectNonNullOrCondition(size, other.size) { one, two -> Math.min(one, two) },
                // Pick fixed -> unrepaired -> missing
                if (status == GasLeakStatus.FIXED || other.status == GasLeakStatus.FIXED) {
                    GasLeakStatus.FIXED
                } else if (status == GasLeakStatus.UNREPAIRED || other.status == GasLeakStatus.UNREPAIRED) {
                    GasLeakStatus.UNREPAIRED
                } else if (status == GasLeakStatus.MISSING || other.status == GasLeakStatus.MISSING) {
                    GasLeakStatus.MISSING
                } else {
                    throw NotImplementedError("illegal gas leak status")
                },
                selectNonNullOrCondition(reportedOn, other.reportedOn) {
                    one, two -> if (one.isBefore(two)) one else two },
                selectNonNullOrCondition(fixedOn, other.fixedOn) {
                    one, two -> if (one.isBefore(two)) one else two },
                ngridId ?: other.ngridId)
    }
}