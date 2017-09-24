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

import ing.leakfix.site.data.GasLeak
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
        val mergedLeaks = allLeaks.toMutableList()

        duplicateLeaks.forEach { firstLeak ->
            var uberLeak = firstLeak
            duplicateLeaks.forEach { testLeak ->
                if (firstLeak.shouldMergeWith(testLeak)) {
                    uberLeak = uberLeak merge testLeak
                    duplicateLeaks.remove(testLeak)
                }
            }
            mergedLeaks += uberLeak
        }

        return mergedLeaks
    }

    @RequestMapping("/leaks")
    fun getLeaks(): List<GasLeak> = repository.findAll()

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