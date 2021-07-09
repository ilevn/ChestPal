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
package tf.sou.mc.pal.domain

import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import tf.sou.mc.pal.ChestPal
import tf.sou.mc.pal.RECEIVER_MATERIAL
import tf.sou.mc.pal.SENDER_MATERIAL
import tf.sou.mc.pal.utils.*

/**
 * Stateless actor enum for [Material] based interactions with a chest.
 */
@Suppress("unused")
enum class HoeType(private val material: Material) : EventActor<PlayerInteractEvent> {
    /**
     * Debug interaction with a chest.
     */
    DEBUG(Material.DIAMOND_HOE) {
        override fun act(event: PlayerInteractEvent, pal: ChestPal) {
            if (event.player.displayName().contains("0x1".asTextComponent())) {
                event.clickedBlock?.let { event.reply(it.location.toVectorString()) }
            }
        }
    },

    /**
     * Sender interaction with a chest.
     * This is triggered whenever a new sender chest is registered.
     */
    SENDER(SENDER_MATERIAL) {
        override fun act(event: PlayerInteractEvent, pal: ChestPal) {
            val chestLocation = event.clickedBlock?.location ?: return
            val container = chestLocation.resolveContainer() ?: return
            if (container.inventory.toChestInventoryProxy().isRegistered(pal.database)) {
                event.reply("This chest is already a registered!")
                return
            }
            event.reply("Saving sender chest at location ${chestLocation.toVectorString()}!")
            pal.database.saveSenderLocation(chestLocation)
        }
    },

    /**
     * Receiver interaction with a chest.
     * This is triggered whenever a new receiver chest is registered.
     */
    RECEIVER(RECEIVER_MATERIAL) {
        override fun act(event: PlayerInteractEvent, pal: ChestPal) {
            val chestLocation = event.clickedBlock?.location ?: return
            val container = chestLocation.resolveContainer() ?: return
            if (container.inventory.toChestInventoryProxy().isRegistered(pal.database)) {
                // Either a receiver or sender chest.
                event.reply("This chest is already registered!")
                return
            }

            when (val result = chestLocation.findItemFrame()) {
                is ItemFrameResult.NoFrame -> event.reply("Could not find item frame")
                is ItemFrameResult.NoItem -> event.reply("Item frame does not have an item")
                is ItemFrameResult.Found -> {
                    val type = result.frame.item.type
                    pal.database.saveMaterialLocation(type, chestLocation)
                    event.reply("Saved new item chest for type ${type.toPrettyString()}!")
                }
            }
        }
    };

    companion object {
        /**
         * Attempts to find the appropriate enum based on the provided [Material].
         */
        fun find(material: Material) = values().find { it.material == material }
    }
}
