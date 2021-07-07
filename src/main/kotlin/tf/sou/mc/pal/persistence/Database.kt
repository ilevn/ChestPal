package tf.sou.mc.pal.persistence

import org.bukkit.Location
import org.bukkit.Material

/**
 * Database interface for interactions between the server and the persistence layer.
 */
interface Database {
    /**
     * Save locations for a given [Material].
     */
    fun saveMaterialLocation(material: Material, location: Location)


}
