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
package tf.sou.mc.pal.json

import com.google.gson.GsonBuilder
import org.bukkit.Location
import tf.sou.mc.pal.domain.ReceiverChests

/**
 * JSON Serializer for material based location data.
 */
object JsonSerializer {
    private val gson = GsonBuilder()
        .registerTypeAdapter(Location::class.java, LocationSerializer).create()

    /**
     * Serializes a collection of [locations][Location] to a JSON string.
     */
    fun locationsToJson(locations: Collection<Location>): String {
        return gson.toJson(locations)
    }

    /**
     * Deserializes a JSON string to a list.
     */
    fun jsonToLocations(json: String): List<Location> {
        return gson.fromJson(json, Array<Location>::class.java).toList()
    }

    /**
     * Serializes a [ReceiverChests] to a JSON string.
     */
    fun receiverChestsToJson(chests: ReceiverChests): String {
        return gson.toJson(chests)
    }

    /**
     * Deserializes a JSON string to a [ReceiverChests].
     */
    fun jsonToReceiverChests(json: String): ReceiverChests {
        return gson.fromJson(json, ReceiverChests::class.java)
    }
}
