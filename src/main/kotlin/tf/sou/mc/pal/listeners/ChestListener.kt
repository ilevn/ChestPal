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
        // TODO: Prevent users from placing other items in rec. boxes.
        val inventory = event.inventory
        if (inventory.location == null || !pal.database.isSenderChest(inventory.location)) {
            return
        }

        val senderChest =
            inventory.location?.resolveContainer() ?: error("This should never happen")
        val chestItems = inventory.contents
            .filterNotNull().groupingBy { it.type }
            .fold(0) { acc, stack -> acc + stack.amount }

        for ((material, amount) in chestItems) {
            var transportAmount = amount
            val receiverChests = pal.database.receiverLocationsFor(material)
            if (receiverChests.isEmpty()) {
                continue
            }

            val iterator = receiverChests.iterator()
            while (transportAmount > 0 && iterator.hasNext()) {
                val chest =
                    iterator.next().resolveContainer() ?: error("Unable to find receiver chest!")
                // Check how much space this container has
                // and calculate how many items we are allowed to add.
                val available = chest.countAvailableSpace(material)
                val allowedToAdd = available.coerceAtMost(transportAmount)
                allowedToAdd.asItemStacks(material).forEach { chest.inventory.addItem(it) }
                transportAmount -= allowedToAdd
            }

            senderChest.inventory.remove(material)
            if (transportAmount > 0) {
                // Re-add leftover items.
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
    fun onBlockBreakEvent(event: BlockBreakEvent) {
        if (pal.database.isRegisteredChest(event.block.location)) {
            event.player.sendMessage("You cannot break registered chests at the moment!")
            // Prevent players from breaking registered chests.
            event.isCancelled = true
        }
    }
}
