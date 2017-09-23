package ing.leakfix.site

import ing.leakfix.site.data.DataValidityRange
import ing.leakfix.site.data.GasLeak
import ing.leakfix.site.data.GasLeakSource
import ing.leakfix.site.data.GasLeakStatus
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class GasLeakTest {
    private final val NGRID_SOURCE = GasLeakSource(
            vendor = "NGRID",
            dataset = "Unrepaired 2016",
            dataBetween = DataValidityRange(
                    LocalDate.of(2016, 1, 1),
                    LocalDate.of(2016, 12, 31)))
    private final val HEET_SOURCE = GasLeakSource(
            vendor = "MAPC-HEET",
            dataset = "Study 2016",
            dataBetween = DataValidityRange(
                    LocalDate.of(2016, 1, 1),
                    LocalDate.of(2016, 12, 31)))

    private final val NGRID_REFERENCE_LEAK = GasLeak(
            location = "18 Piper Road, Acton MA 01720",
            sources = listOf(NGRID_SOURCE),
            size = null,
            status = GasLeakStatus.UNREPAIRED,
            reportedOn = LocalDate.of(2001, 1, 1),
            fixedOn = null,
            ngridId = Random().nextInt())
    private final val HEET_REFERENCE_LEAK = NGRID_REFERENCE_LEAK
            .copy(sources = listOf(HEET_SOURCE))
    private final val MERGED_REFERENCE_LEAK = NGRID_REFERENCE_LEAK
            .copy(sources = listOf(NGRID_SOURCE, HEET_SOURCE))

    @Test
    fun mergesSources() = Assert.assertEquals(
                MERGED_REFERENCE_LEAK,
                NGRID_REFERENCE_LEAK.mergeWith(HEET_REFERENCE_LEAK))

    @Test
    fun mergeSelectsProperStatus() {
        Assert.assertEquals(
                MERGED_REFERENCE_LEAK.copy(status = GasLeakStatus.FIXED),
                NGRID_REFERENCE_LEAK.copy(status = GasLeakStatus.FIXED).mergeWith(HEET_REFERENCE_LEAK))
        Assert.assertEquals(
                MERGED_REFERENCE_LEAK.copy(status = GasLeakStatus.UNREPAIRED),
                NGRID_REFERENCE_LEAK.copy(status = GasLeakStatus.MISSING).mergeWith(HEET_REFERENCE_LEAK))
        Assert.assertEquals(
                MERGED_REFERENCE_LEAK.copy(status = GasLeakStatus.FIXED),
                NGRID_REFERENCE_LEAK.copy(status = GasLeakStatus.MISSING)
                        .mergeWith(HEET_REFERENCE_LEAK.copy(status = GasLeakStatus.FIXED)))
    }

    @Test
    fun mergeSelectsProperDate() = Assert.assertEquals(
            MERGED_REFERENCE_LEAK.copy(
                    reportedOn = LocalDate.of(2001, 1, 1),
                    fixedOn = LocalDate.of(2001, 1, 1)),
            NGRID_REFERENCE_LEAK.copy(
                    reportedOn = LocalDate.of(2001, 1, 1),
                    fixedOn = LocalDate.of(2001, 1, 1))
                    .mergeWith(HEET_REFERENCE_LEAK.copy(
                            reportedOn = LocalDate.of(2001, 1, 2),
                            fixedOn = LocalDate.of(2001, 1, 2))))
}
