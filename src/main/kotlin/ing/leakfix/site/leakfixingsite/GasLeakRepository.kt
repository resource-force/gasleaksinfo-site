package ing.leakfix.site.leakfixingsite

import org.springframework.data.mongodb.repository.MongoRepository

interface GasLeakRepository : MongoRepository<GasLeak, String> {
    fun findByLocation(name: String): List<GasLeak>
}