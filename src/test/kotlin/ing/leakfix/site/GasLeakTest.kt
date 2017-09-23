package ing.leakfix.site

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate

@RunWith(SpringRunner::class)
@SpringBootTest
class GasLeakTest {

    @Test
    fun mergeWith() {
        // TODO: Add correct expected outcome to pass test
        Assert.assertEquals(false,
                GasLeak(
                        location = "xd",
                        source = GasLeakSource(
                                vendor = "NGRID",
                                dataset = "Unrepaired 2016",
                                dataBetween = DataValidityRange(
                                        LocalDate.of(2017, 1, 1),
                                        LocalDate.of(2017, 12, 31))),
                        size = null,
                        status = GasLeakStatus.MISSING,
                        reportedOn = LocalDate.now(),
                        fixedOn = LocalDate.now(),
                        ngridId = 0).mergeWith(
                        GasLeak(
                                location = "xd",
                                source = GasLeakSource(
                                        vendor = "MAPC-HEET",
                                        dataset = "Study 2016",
                                        dataBetween = DataValidityRange(
                                                LocalDate.of(2017, 1, 1),
                                                LocalDate.of(2017, 12, 31))),
                                size = 500,
                                status = GasLeakStatus.UNREPAIRED,
                                reportedOn = LocalDate.now(),
                                fixedOn = LocalDate.now(),
                                ngridId = 0)))
    }
}
