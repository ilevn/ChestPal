package tf.sou.mc.pal.json

import com.google.gson.GsonBuilder
import org.bukkit.Location
import org.bukkit.Material
import tf.sou.mc.pal.domain.MaterialLocation


/**
 * JSON Serializer for material based location data.
 */
object JsonSerializer {
    private val gson = GsonBuilder().registerTypeAdapter(Location::class.java, LocationSerializer).create()

    fun locationToJson(location: Location): String {
        return gson.toJson(location)
    }

    fun jsonToLocation(json: String): Location {
        return gson.fromJson(json, Location::class.java)
    }

    fun materialLocationsToJson(material: Material, locations: List<Location>): String {
        val materialLocations = MaterialLocation(material, locations)
        return gson.toJson(materialLocations)
    }

    fun jsonToMaterialLocation(json: String): MaterialLocation {
        return gson.fromJson(json, MaterialLocation::class.java)
    }
}


