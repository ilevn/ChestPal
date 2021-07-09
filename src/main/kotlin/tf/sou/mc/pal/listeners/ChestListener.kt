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

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import tf.sou.mc.pal.ChestPal
import tf.sou.mc.pal.domain.HoeType
import tf.sou.mc.pal.domain.ItemFrameResult
import tf.sou.mc.pal.utils.*

/**
 * Listeners for chest based events.
 */
class ChestListener(private val pal: ChestPal) : Listener {
    @EventHandler
    fun onInventoryCloseEvent(event: InventoryCloseEvent) {
        val inventory = event.inventory
        val location = inventory.location ?: return

        if (!pal.database.isRegisteredChest(location)) {
            return
        }

        val eventChest = location.resolveContainer() ?: error("This should never happen")
        if (pal.database.isReceiverChest(location)) {
            handleClosedReceiverChest(location, inventory, event)
            return
        }

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

            eventChest.inventory.remove(material)
            if (transportAmount > 0) {
                // Re-add leftover items.
                transportAmount.asItemStacks(material).forEach { eventChest.inventory.addItem(it) }
            }

            (amount - transportAmount).takeIf { it > 0 }
                ?.let { event.player.sendMessage("Moved $it ${material.name}s!") }
        }
    }

    private fun handleClosedReceiverChest(
        location: Location,
        inventory: Inventory,
        event: InventoryCloseEvent
    ) {
        // This could be a cache lookup in the future.
        val item = location.findItemFrame() as? ItemFrameResult.Found ?: return
        val allowedItem = item.frame.item.type
        val badItems = inventory.findBadItems(allowedItem)
        if (badItems.isEmpty()) {
            return
        }
        // Clean up.
        badItems.forEach {
            inventory.remove(it)
            event.player.inventory.addItem(it)
        }
        event.player.sendMessage("This receiver chest only takes $allowedItem!")
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
