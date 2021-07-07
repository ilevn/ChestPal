package tf.sou.mc.pal.persistence

import org.bukkit.Location
import org.bukkit.Material
import tf.sou.mc.pal.json.JsonSerializer
import java.io.File

/**
 * Json database used for storing serialized location data.
 */
class JsonDatabase(configDirectory: File) : Database {
    private val serializer = JsonSerializer
    private val jsonFile = configDirectory.resolve("chest_location_data.json")

    override fun saveMaterialLocations(material: Material, locations: List<Location>) {
        val data = serializer.materialLocationsToJson(material, locations)
        jsonFile.writeText(data)
    }
}
