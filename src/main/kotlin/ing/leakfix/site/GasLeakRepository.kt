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
import ing.leakfix.site.data.SourceEntry
import org.springframework.data.mongodb.repository.MongoRepository

// ArrayList because it has to be serializable
interface GasLeakRepository : MongoRepository<GasLeak, ArrayList<SourceEntry>> {
    fun findBySourceEntries(sourceEntries: ArrayList<SourceEntry>): GasLeak
}