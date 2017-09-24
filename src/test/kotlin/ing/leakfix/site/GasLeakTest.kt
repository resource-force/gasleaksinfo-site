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

package ing.leakfix.site

import ing.leakfix.site.data.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class GasLeakTest {
    private final val NGRID_SOURCE = SourceDataset(
            vendor = "NGRID",
            name = "Unrepaired 2016",
            validBetween = DatasetValidityRange(
                    LocalDate.of(2016, 1, 1),
                    LocalDate.of(2016, 12, 31)))
    private final val HEET_SOURCE = SourceDataset(
            vendor = "MAPC-HEET",
            name = "Study 2016",
            validBetween = DatasetValidityRange(
                    LocalDate.of(2016, 1, 1),
                    LocalDate.of(2016, 12, 31)))
    private final val NGRID_ENTRY = SourceEntry(1, NGRID_SOURCE)
    private final val HEET_ENTRY = SourceEntry(2, HEET_SOURCE)

    private final val NGRID_REFERENCE_LEAK = GasLeak(
            represents = listOf(NGRID_ENTRY),
            location = "18 Piper Road, Acton MA 01720",
            size = null,
            status = GasLeakStatus.UNREPAIRED,
            reportedOn = LocalDate.of(2001, 1, 1),
            fixedOn = null)
    private final val HEET_REFERENCE_LEAK = NGRID_REFERENCE_LEAK
            .copy(represents = listOf(HEET_ENTRY))
    private final val MERGED_REFERENCE_LEAK = NGRID_REFERENCE_LEAK
            .copy(represents = listOf(NGRID_ENTRY, HEET_ENTRY))

    @Test
    fun mergeSetsMergedFlag() {
        assertTrue(MERGED_REFERENCE_LEAK.merged)
        assertFalse(NGRID_REFERENCE_LEAK.merged)
        assertFalse(HEET_REFERENCE_LEAK.merged)
    }

    @Test
    fun mergeCombinesSources() = assertEquals(
                MERGED_REFERENCE_LEAK,
                NGRID_REFERENCE_LEAK.merge(HEET_REFERENCE_LEAK))

    @Test
    fun mergeSelectsProperStatus() {
        assertEquals(
                MERGED_REFERENCE_LEAK.copy(status = GasLeakStatus.FIXED),
                NGRID_REFERENCE_LEAK.copy(status = GasLeakStatus.FIXED) merge HEET_REFERENCE_LEAK)
        assertEquals(
                MERGED_REFERENCE_LEAK.copy(status = GasLeakStatus.UNREPAIRED),
                NGRID_REFERENCE_LEAK.copy(status = GasLeakStatus.MISSING) merge HEET_REFERENCE_LEAK)
        assertEquals(
                MERGED_REFERENCE_LEAK.copy(status = GasLeakStatus.FIXED),
                NGRID_REFERENCE_LEAK.copy(status = GasLeakStatus.MISSING)
                        merge HEET_REFERENCE_LEAK.copy(status = GasLeakStatus.FIXED))
    }

    @Test
    fun mergeSelectsProperDate() = assertEquals(
            MERGED_REFERENCE_LEAK.copy(
                    reportedOn = LocalDate.of(2001, 1, 1),
                    fixedOn = LocalDate.of(2001, 1, 2)),
            NGRID_REFERENCE_LEAK.copy(
                    reportedOn = LocalDate.of(2001, 1, 1),
                    fixedOn = LocalDate.of(2001, 1, 2))
                    merge HEET_REFERENCE_LEAK.copy(
                            reportedOn = LocalDate.of(2001, 1, 2),
                            fixedOn = LocalDate.of(2001, 1, 2)))

    @Test
    fun doesNotMergeSameIds() = assertFalse(NGRID_REFERENCE_LEAK
            .shouldMergeWith(HEET_REFERENCE_LEAK.copy(represents = listOf(NGRID_ENTRY))))

    @Test
    fun doesNotMergeSameSource() {
        assertFalse(NGRID_REFERENCE_LEAK.shouldMergeWith(NGRID_REFERENCE_LEAK))
        assertFalse(HEET_REFERENCE_LEAK.shouldMergeWith(HEET_REFERENCE_LEAK))
    }

    @Test
    fun doesNotMergeDifferentAddresses() = assertFalse(NGRID_REFERENCE_LEAK
            .shouldMergeWith(HEET_REFERENCE_LEAK.copy(location = "19 Piper Road, Acton MA 01720")))

    @Test
    fun doesNotMergeMergedLeak() {
        assertFalse(MERGED_REFERENCE_LEAK.shouldMergeWith(NGRID_REFERENCE_LEAK))
        assertFalse(MERGED_REFERENCE_LEAK.shouldMergeWith(HEET_REFERENCE_LEAK))
    }
}
