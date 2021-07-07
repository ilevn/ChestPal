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
    private val chestLocations = configDirectory.resolve("chest_location_data.json")
    private val senderLocations = configDirectory.resolve("sender_locations.json")
    private val locationCache = LocationCache.fromFiles(
        chestLocations, senderLocations,
        serializer::jsonToLocations, serializer::jsonToReceiverChests
    )

    init {
        configDirectory.mkdirs()
    }

    // TODO: Schedule bulk insertion of locations.
    override fun saveMaterialLocation(material: Material, location: Location) {
        locationCache.addReceiverLocation(material, location)
        val chests = locationCache.chestLocationsToReceiverChests()
        chestLocations.writeText(serializer.receiverChestsToJson(chests))
    }

    fun saveSenderLocation(location: Location) {
        locationCache.addSenderLocation(location)
        senderLocations.writeText(serializer.locationsToJson(locationCache.senderLocations))
    }

    fun receiverLocationsFor(material: Material): Set<Location> =
        locationCache.receiverLocationsFor(material) ?: emptySet()

    fun isRegisteredChest(location: Location?): Boolean = locationCache.isRegisteredChestLocation(location)

    fun isReceiverChest(location: Location?): Boolean = locationCache.isReceiverChestLocation(location)

    fun isSenderChest(location: Location?): Boolean = locationCache.isSenderChestLocation(location)
}
