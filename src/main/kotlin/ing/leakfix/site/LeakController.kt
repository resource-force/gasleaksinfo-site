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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController


@RepositoryRestController
class LeakController {
    @Autowired
    lateinit var gasLeakRepository: LeakRepository

    @RequestMapping("/leaks/merged")
    fun getLeaksMerged(): ResponseEntity<List<MergedLeak>> {
        val mergeableLeakSets: MutableList<Array<Leak>> = mutableListOf()
        gasLeakRepository.findAll().forEach { testLeak ->
            var added = false
            mergeableLeakSets.forEachIndexed { i, it ->
                if (MergedLeak.mergeable(*(it + testLeak))) {
                    mergeableLeakSets[i] = it + testLeak
                    added = true
                } else {
                    mergeableLeakSets[i] = it
                }
            }
            if (!added) {
                mergeableLeakSets += arrayOf(testLeak)
            }
        }
        return ResponseEntity.ok(mergeableLeakSets.map { MergedLeak.of(*it) })
    }
}