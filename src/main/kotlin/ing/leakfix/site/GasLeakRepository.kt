package ing.leakfix.site

import org.springframework.data.mongodb.repository.MongoRepository

interface GasLeakRepository : MongoRepository<GasLeak, String> {
    fun findByLocation(location: String): List<GasLeak>
}