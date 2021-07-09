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
import tf.sou.mc.pal.json.JsonSerializer

/**
 * Json database used for storing serialized location data.
 */
class JsonDatabase(configDirectory: File) : Database {
    private val serializer = JsonSerializer
    private val receiverLocations = configDirectory.resolve("chest_location_data.json")
    private val senderLocations = configDirectory.resolve("sender_locations.json")
    private val locationCache = LocationCache.fromFiles(
        receiverLocations, senderLocations,
        serializer::jsonToLocations, serializer::jsonToReceiverChests
    )

    init {
        configDirectory.mkdirs()
    }

    // TODO: Schedule bulk insertion of locations.
    override fun saveMaterialLocation(material: Material, location: Location) {
        locationCache.addReceiverLocation(material, location)
        val chests = locationCache.chestLocationsToReceiverChests()
        receiverLocations.writeText(serializer.receiverChestsToJson(chests))
    }

    override fun saveSenderLocation(location: Location) {
        locationCache.addSenderLocation(location)
        senderLocations.writeText(serializer.locationsToJson(locationCache.senderLocations))
    }

    override fun receiverLocationsFor(material: Material): Set<Location> =
        locationCache.receiverLocationsFor(material) ?: emptySet()

    override fun isRegisteredChest(location: Location?): Boolean =
        locationCache.isRegisteredChestLocation(location)

    override fun isReceiverChest(location: Location?): Boolean =
        locationCache.isReceiverChestLocation(location)

    override fun isSenderChest(location: Location?): Boolean =
        locationCache.isSenderChestLocation(location)

    override fun removeLocation(location: Location): Boolean {
        if (!locationCache.removeLocation(location)) {
            return false
        }
        // Location was actually removed.
        val chests = locationCache.chestLocationsToReceiverChests()
        receiverLocations.writeText(serializer.receiverChestsToJson(chests))
        senderLocations.writeText(serializer.locationsToJson(locationCache.senderLocations))
        return true
    }
}
