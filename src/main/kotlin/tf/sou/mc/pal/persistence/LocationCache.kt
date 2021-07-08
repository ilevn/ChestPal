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

import java.io.File
import org.bukkit.Location
import org.bukkit.Material
import tf.sou.mc.pal.domain.MaterialLocation
import tf.sou.mc.pal.domain.ReceiverChests

class LocationCache(receiverChests: ReceiverChests, senderLocations: List<Location>) {
    private val chestLocations = receiverChests
        .data.associate { it.material to it.receivers.toMutableSet() }.toMutableMap()
    internal val senderLocations = senderLocations.toMutableSet()
    private var cachedChestLocations = chestLocations.values.flatten()

    internal fun receiverLocationsFor(material: Material): Set<Location>? = chestLocations[material]

    internal fun isSenderChestLocation(location: Location?): Boolean = location in senderLocations

    internal fun isReceiverChestLocation(location: Location?): Boolean =
        location in cachedChestLocations

    internal fun isRegisteredChestLocation(location: Location?): Boolean =
        isSenderChestLocation(location) || location in cachedChestLocations

    internal fun chestLocationsToReceiverChests(): ReceiverChests {
        val materialChests = chestLocations.map { MaterialLocation(it.key, it.value.toList()) }
        return ReceiverChests(materialChests)
    }

    internal fun addReceiverLocation(material: Material, location: Location): Boolean {
        val result = chestLocations.computeIfAbsent(material) { mutableSetOf() }.add(location)
        cachedChestLocations = chestLocations.values.flatten()
        return result
    }

    internal fun addSenderLocation(location: Location): Boolean = senderLocations.add(location)

    companion object {
        internal fun fromFiles(
            receiverLocationFile: File,
            senderLocationFile: File,
            locationTransformer: (String) -> List<Location>,
            receiverTransformer: (String) -> ReceiverChests
        ): LocationCache {
            val receivers = receiverLocationFile
                .takeIf { it.exists() }?.let { receiverTransformer(it.readText()) }
                ?: ReceiverChests(emptyList())

            val senders = senderLocationFile
                .takeIf { it.exists() }?.let { locationTransformer(it.readText()) } ?: emptyList()

            return LocationCache(receivers, senders)
        }
    }
}
