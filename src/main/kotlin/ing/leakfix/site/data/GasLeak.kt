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

    infix fun merge(other: GasLeak): GasLeak {
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
                // Pick the earliest reported-on date and the latest fixed-on date
                // We want to be conservative here
                selectNonNullOrCondition(reportedOn, other.reportedOn) {
                    one, two -> if (one.isBefore(two)) one else two },
                selectNonNullOrCondition(fixedOn, other.fixedOn) {
                    one, two -> if (one.isAfter(two)) one else two },
                ngridId ?: other.ngridId)
    }
}