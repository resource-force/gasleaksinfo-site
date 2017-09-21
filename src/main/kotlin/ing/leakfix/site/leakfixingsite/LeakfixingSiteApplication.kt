package ing.leakfix.site.leakfixingsite

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class LeakfixingSiteApplication

fun main(args: Array<String>) {
    SpringApplication.run(LeakfixingSiteApplication::class.java, *args)
}