/*
 * leakfixing-site: a site to show and manage a gas leak database
 * Copyright (C) 2017 Kevin Liu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package ing.leakfix.site.data

import java.time.LocalDate

// TODO Convert sourceEntries to a map-kinda-thing.
data class GasLeak(
        val sourceEntries: List<SourceEntry>,
        val location: String,
        val size: Int?,
        val status: GasLeakStatus,
        val reportedOn: LocalDate?,
        val fixedOn: LocalDate?) {
    val isMerged: Boolean

    init {
        sourceEntries.isEmpty() && throw IllegalArgumentException("Must have at least one source.")
        isMerged = sourceEntries.size != 1
    }

    fun shouldMergeWith(other: GasLeak): Boolean =
            !isMerged && !other.isMerged &&
                    sourceEntries[0] != other.sourceEntries[0] &&
                    sourceEntries[0].dataset != other.sourceEntries[0].dataset &&
                    location == other.location

    private fun <T> selectNonNullOrCondition(
            one: T?, two: T?, condition: (T, T) -> T): T? {
        return if (one != null && two != null) {
            condition(one, two)
        } else {
            one ?: two
        }
    }

    infix fun merge(other: GasLeak): GasLeak {
        if (!shouldMergeWith(other)) {
            throw IllegalArgumentException("Leaks $this and $other should not be merged.")
        }

        return GasLeak(
                sourceEntries = sourceEntries + other.sourceEntries,
                location = location,
                // Pick the lowest size or the non-null one, or null.
                size = selectNonNullOrCondition(size, other.size) { one, two -> Math.min(one, two) },
                // Pick fixed -> unrepaired -> missing
                status = if (status == GasLeakStatus.FIXED ||
                        other.status == GasLeakStatus.FIXED) {
                    GasLeakStatus.FIXED
                } else if (status == GasLeakStatus.UNREPAIRED ||
                        other.status == GasLeakStatus.UNREPAIRED) {
                    GasLeakStatus.UNREPAIRED
                } else if (status == GasLeakStatus.MISSING ||
                        other.status == GasLeakStatus.MISSING) {
                    GasLeakStatus.MISSING
                } else {
                    throw NotImplementedError("Unimplemented gas leak status in merge.")
                },
                // Pick the earliest reported-on date and the latest fixed-on date
                // We want to be conservative here
                reportedOn = selectNonNullOrCondition(reportedOn, other.reportedOn) {
                    one, two -> if (one.isBefore(two)) one else two },
                fixedOn = selectNonNullOrCondition(fixedOn, other.fixedOn) {
                    one, two -> if (one.isAfter(two)) one else two })
    }
}