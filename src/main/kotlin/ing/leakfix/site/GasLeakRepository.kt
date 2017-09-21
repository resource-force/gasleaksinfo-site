package ing.leakfix.site

import org.springframework.data.mongodb.repository.MongoRepository

interface GasLeakRepository : MongoRepository<GasLeak, String> {
    fun findByLocation(name: String): List<GasLeak>
}