package tf.sou.mc.pal.json

import com.google.gson.GsonBuilder
import org.bukkit.Location
import org.bukkit.Material
import tf.sou.mc.pal.domain.MaterialLocation
import tf.sou.mc.pal.domain.ReceiverChests


/**
 * JSON Serializer for material based location data.
 */
object JsonSerializer {
    private val gson = GsonBuilder().registerTypeAdapter(Location::class.java, LocationSerializer).create()

    fun locationToJson(location: Location): String {
        return gson.toJson(location)
    }

    fun locationsToJson(locations: Collection<Location>): String {
        return gson.toJson(locations)
    }

    fun jsonToLocations(json: String): List<Location> {
        return gson.fromJson(json, Array<Location>::class.java).toList()
    }

    fun jsonToLocation(json: String): Location {
        return gson.fromJson(json, Location::class.java)
    }

    fun materialLocationsToJson(material: Material, locations: Collection<Location>): String {
        val materialLocations = MaterialLocation(material, locations.toList())
        return gson.toJson(materialLocations)
    }

    fun receiverChestsToJson(chests: ReceiverChests): String {
        return gson.toJson(chests)
    }

    fun jsonToReceiverChests(json: String): ReceiverChests {
        return gson.fromJson(json, ReceiverChests::class.java)
    }

    fun jsonToMaterialLocation(json: String): MaterialLocation {
        return gson.fromJson(json, MaterialLocation::class.java)
    }
}


