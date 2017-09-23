package ing.leakfix.site

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PostMapping


@RestController
@RequestMapping("/api/r0/") class LeakController {
    @Autowired
    lateinit var repository: GasLeakRepository

    @RequestMapping("/leaks/at/{location}")
    fun getLeak(@PathVariable("location") location: String): List<GasLeak> {
        return repository.findByLocation(location)
    }

    @RequestMapping("leaks/merged")
    fun getMergedLeaks(): List<GasLeak> {
        val allLeaks = getLeaks()
        val uniqueLeaks = allLeaks.distinctBy { it.location }
        val duplicateLeaks = allLeaks.subtract(uniqueLeaks).toMutableList()
        var mergedLeaks: List<GasLeak> = mutableListOf()

        duplicateLeaks.forEach { firstLeak ->
            duplicateLeaks
                    .filter { it != firstLeak && it.location == firstLeak.location }
                    .forEach {
                duplicateLeaks.remove(it)
            }
        }

        return uniqueLeaks + duplicateLeaks
    }

    @RequestMapping("/leaks")
    fun getLeaks(): List<GasLeak> {
        return repository.findAll()
    }

    @PostMapping("/leaks")
    fun addLeak(@RequestBody leak: GasLeak): ResponseEntity<String> {
        repository.save(leak)
        return ResponseEntity(HttpStatus.CREATED)
    }

    //This is of course a very naive implementation! We are assuming unique names...
    // TODO: Fix
    @DeleteMapping("/leaks/{location}")
    fun deleteLeak(@PathVariable location: String): ResponseEntity<String> {
        val leaks = repository.findByLocation(location)
        if (leaks.size == 1) {
            val leak = leaks.get(0)
            repository.delete(leak)
            return ResponseEntity(HttpStatus.ACCEPTED)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }
}