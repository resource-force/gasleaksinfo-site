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

data class MergedGasLeak(
        val fromIds: List<Int>,
        val inDatasets: List<SourceDataset>,
        val location: String,
        val status: GasLeakStatus,
        val size: Int?,
        val firstReportedOn: LocalDate?,
        val fixedOn: LocalDate?) {
    companion object {
        fun shouldMerge(vararg leaks: GasLeak): Boolean {
            val first = leaks.first()

            return !leaks.all { it.source == first.source } &&
                    !leaks.all { it.id == first.id } &&
                    leaks.all { it.location == first.location }
        }

        fun of(vararg leaks: GasLeak): MergedGasLeak {
            if (!shouldMerge(*leaks)) {
                throw IllegalArgumentException("Leaks $leaks should not be merged.")
            }

            return MergedGasLeak(
                    fromIds = leaks.map { it.id },
                    inDatasets = leaks.map { it.source },
                    location = leaks.first().location,
                    // Pick the lowest size or the non-null one, or null.
                    size = leaks.map { it.size }.filterNotNull().min(),
                    // Pick fixed -> unrepaired -> missing
                    status = leaks.map { it.status }.maxBy {
                        when (it) {
                            GasLeakStatus.FIXED -> 3
                            GasLeakStatus.UNREPAIRED -> 2
                            GasLeakStatus.MISSING -> 1
                        } }!!,
                    // Pick the earliest reported-on date and the latest fixed-on date
                    // We want to be conservative here
                    firstReportedOn = leaks.map { it.reportedOn }.filterNotNull().min(),
                    fixedOn = leaks.map { it.fixedOn }.filterNotNull().max())
        }
    }
}