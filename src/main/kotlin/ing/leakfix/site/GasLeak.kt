package ing.leakfix.site

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

    /**
     * Simple constructor for a non-merged entry
     */
    constructor(location: String,
                source: GasLeakSource,
                size: Int?,
                status: GasLeakStatus,
                reportedOn: LocalDate?,
                fixedOn: LocalDate?,
                ngridId: Int?) :
            this(location, listOf(source), size, status, reportedOn, fixedOn, ngridId)

    fun shouldMergeWith(leak: GasLeak): Boolean =
            location == leak.location && sources != leak.sources && !merged && !leak.merged

    fun mergeWith(other: GasLeak): GasLeak {
        if (!shouldMergeWith(other)) {
            throw IllegalArgumentException("Leaks should not be merged")
        }

        return GasLeak(
                location,
                sources.union(other.sources).toList(),
                // TODO: Improve algorithm
                // Pick the lowest size or the non-null one, or null.
                if (size == null && other.size != null) {
                    other.size
                } else if (size != null && other.size == null) {
                    size
                } else if (size != null && other.size != null) {
                    Math.min(size, other.size)
                } else {
                    null
                },
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
                if (reportedOn!! < other.reportedOn) reportedOn else other.reportedOn,
                if (fixedOn!! < other.fixedOn) fixedOn else other.fixedOn,
                ngridId ?: other.ngridId)
    }
}