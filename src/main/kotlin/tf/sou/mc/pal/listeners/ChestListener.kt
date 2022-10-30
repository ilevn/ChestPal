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
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.PluginNameConversationPrefix
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import tf.sou.mc.pal.ChestPal
import tf.sou.mc.pal.domain.HoeType
import tf.sou.mc.pal.domain.ItemFrameResult
import tf.sou.mc.pal.prompts.ChestBreakPrompt
import tf.sou.mc.pal.utils.*

/**
 * Listeners for chest based events.
 */
class ChestListener(private val pal: ChestPal) : Listener {
    private val conversationFactory = ConversationFactory(pal)
        .withModality(true)
        .withFirstPrompt(ChestBreakPrompt())
        .withTimeout(7)
        .withPrefix(PluginNameConversationPrefix(pal))
        .thatExcludesNonPlayersWithMessage("No console users!")

    @EventHandler
    fun onInventoryCloseEvent(event: InventoryCloseEvent) {
        val inventory = event.inventory
        val location = inventory.location ?: return

        val chestProxy = inventory.toChestInventoryProxy()
        if (!chestProxy.isRegistered(database = pal.database)) {
            return
        }

        val eventChest = location.resolveContainer() ?: error("This should never happen")
        val eventInventory = eventChest.inventory.toChestInventoryProxy()
        if (eventInventory.isReceiver(database = pal.database)) {
            handleClosedReceiverChest(location, inventory, event)
            return
        }


        val chestItems = inventory.contents
            .filterNotNull()

        for (material in chestItems) {
            var transportAmount = material.amount
            val receiverChests = pal.database.receiverLocationsFor(material.type)
            if (receiverChests.isEmpty()) {
                continue
            }

            val iterator = receiverChests.iterator()
            while (transportAmount > 0 && iterator.hasNext()) {
                val chest =
                    iterator.next().resolveContainer() ?: error("Unable to find receiver chest!")
                // Check how much space this container has
                // and calculate how many items we are allowed to add.
                val available = chest.inventory.countAvailableSpace(material.type)
                val allowedToAdd = available.coerceAtMost(transportAmount)
                allowedToAdd.asItemStacks(material).forEach { chest.inventory.addItem(it) }
                transportAmount -= allowedToAdd
            }

            eventChest.inventory.remove(material)
            if (transportAmount > 0) {
                // Re-add leftover items.
                transportAmount.asItemStacks(material).forEach { eventChest.inventory.addItem(it) }
            }

            (material.amount - transportAmount).takeIf { it > 0 }
                ?.let { event.player.sendMessage("Moved ${material.type.toPrettyString()} (x$it)") }
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
            if (it != null) {
                inventory.remove(it)
            }
            if (it != null) {
                event.player.inventory.addItem(it)
            }
        }
        event.player.sendMessage("This receiver chest only takes ${allowedItem.toPrettyString()}!")
    }

    @EventHandler
    fun onPlayerInteractEntityEvent(event: PlayerInteractEvent) {
        val hoeType = event.item?.let { HoeType.find(it.type) } ?: return
        if (event.clickedBlock?.type != Material.CHEST) {
            return
        }
        hoeType.act(event, pal)
        event.isCancelled = true
    }

    @EventHandler
    fun onBlockBreakEvent(event: BlockBreakEvent) {
        if (event.block.type != Material.CHEST) {
            return
        }

        val container = event.block.location.resolveContainer() ?: return
        val proxy = container.inventory.toChestInventoryProxy()
        if (proxy.isRegistered(pal.database)) {
            val player = event.player
            if (!player.hasPermission("chestpal.remove_chest")) {
                event.isCancelled = true
                player.redMessage("You are not allowed to break registered chests!")
                return
            }
            conversationFactory
                .withInitialSessionData(mapOf("block" to event.block))
                .buildConversation(event.player)
                .begin()
            // Cancel the event because we delegated to the conversation handler.
            event.isCancelled = true
        }
    }
}
