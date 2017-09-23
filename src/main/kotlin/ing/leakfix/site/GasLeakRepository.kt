package ing.leakfix.site

import ing.leakfix.site.data.GasLeak
import org.springframework.data.mongodb.repository.MongoRepository

interface GasLeakRepository : MongoRepository<GasLeak, String> {
    fun findByLocation(location: String): List<GasLeak>
}