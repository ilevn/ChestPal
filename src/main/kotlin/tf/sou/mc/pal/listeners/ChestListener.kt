package tf.sou.mc.pal.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.plugin.java.JavaPlugin

/**
 * Listeners for chest based events.
 */
class ChestListener(private val plugin: JavaPlugin) : Listener {
    @EventHandler
    fun onInventoryCloseEvent(event: InventoryCloseEvent) {
        val chestItems = event.inventory.contents.filterNotNull().groupBy { it.type }
        if (chestItems.isEmpty()) {
            return
        }

        // some block -> [cords, ...]
    }
}
