package tf.sou.mc.pal.domain

import org.bukkit.Location
import org.bukkit.Material

/**
 * Concrete class for a [Material] with associated [locations][Location].
 */
data class MaterialLocation(val material: Material, val locations: List<Location>)
