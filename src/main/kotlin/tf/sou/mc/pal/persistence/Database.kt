/*
 * This file is part of ChestPal.
 *
 * ChestPal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ChestPal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ChestPal.  If not, see <https://www.gnu.org/licenses/>.
 */
package tf.sou.mc.pal.persistence

import org.bukkit.Location
import org.bukkit.Material

/**
 * Database interface for interactions between the server and the persistence layer.
 */
interface Database {
    /**
     * Save a location for a given [Material].
     */
    fun saveMaterialLocation(material: Material, location: Location)

    /**
     * Save the [Location] of a sender chest.
     */
    fun saveSenderLocation(location: Location)

    /**
     * Return a set of receiver locations for a given [Material]
     */
    fun receiverLocationsFor(material: Material): Set<Location>

    /**
     * Return whether the provided [Location] belongs to a registered chest.
     */
    fun isRegisteredChest(location: Location?): Boolean

    /**
     * Return whether the provided [Location] belongs to a receiver chest.
     */
    fun isReceiverChest(location: Location?): Boolean

    /**
     * Return whether the provided [Location] belongs to a sender chest.
     */
    fun isSenderChest(location: Location?): Boolean
}
