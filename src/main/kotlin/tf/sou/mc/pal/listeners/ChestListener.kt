package tf.sou.mc.pal.listeners

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEvent
import tf.sou.mc.pal.ChestPal
import tf.sou.mc.pal.domain.HoeType
import tf.sou.mc.pal.utils.asItemStacks
import tf.sou.mc.pal.utils.countAvailableSpace
import tf.sou.mc.pal.utils.resolveContainer

/**
 * Listeners for chest based events.
 */
class ChestListener(private val pal: ChestPal) : Listener {
    @EventHandler
    fun onInventoryCloseEvent(event: InventoryCloseEvent) {
        val inventory = event.inventory
        if (inventory.location == null || !pal.database.isSenderChest(inventory.location)) {
            return
        }

        val senderChest = inventory.location?.resolveContainer() ?: error("This should never happen")
        val chestItems = inventory.contents.filterNotNull().groupingBy { it.type }
            .fold(0) { acc, stack -> acc + stack.amount }

        for ((material, amount) in chestItems) {
            var transportAmount = amount
            val receiverChests = pal.database.receiverLocationsFor(material)
            if (receiverChests.isEmpty()) {
                continue
            }

            val iterator = receiverChests.iterator()
            while (transportAmount > 0 && iterator.hasNext()) {
                val chest = iterator.next().resolveContainer()
                // Check how much space this container has
                // and calculate how much we are allowed to add.
                val available = chest.countAvailableSpace(material)
                val allowedToAdd = available.coerceAtMost(transportAmount)
                allowedToAdd.asItemStacks(material).forEach { chest.inventory.addItem(it) }
                transportAmount -= allowedToAdd
            }

            senderChest.inventory.remove(material)
            if (transportAmount > 0) {
                // We couldn't port everything over to other chests.
                transportAmount.asItemStacks(material).forEach { senderChest.inventory.addItem(it) }
            }

            event.player.sendMessage("Moved ${amount - transportAmount} ${material.name}s!")
        }
    }


    @EventHandler
    fun onPlayerInteractEntityEvent(event: PlayerInteractEvent) {
        val hoeType = event.item?.let { HoeType.find(it.type) } ?: return
        if (event.clickedBlock?.type != Material.CHEST) {
            return
        }

        hoeType.act(event, pal)
    }

    @EventHandler
    fun onBlockBreakEvent(e: BlockBreakEvent) {
        // TODO: prevent breaking of chests
    }

    // TODO: Prevent users from placing other items in rec. boxes.
}
