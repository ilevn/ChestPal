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

import org.bukkit.Location
import org.bukkit.inventory.DoubleChestInventory
import org.bukkit.inventory.Inventory
import tf.sou.mc.pal.persistence.Database

/**
 * Proxy class for single and double chests.
 */
class ChestInventoryProxy(private val inventory: Inventory) {
    private inline fun Database.validator(checkFunction: Database.(Location?) -> Boolean): Boolean {
        if (inventory !is DoubleChestInventory) {
            return checkFunction(inventory.location)
        }
        val leftLoc = inventory.leftSide.location
        val rightLoc = inventory.rightSide.location
        return checkFunction(leftLoc) || checkFunction(rightLoc)
    }

    /**
     * Check whether this proxy is a registered chest.
     *
     * @see Database.isRegisteredChest
     */
    fun isRegistered(database: Database): Boolean {
        return database.validator(Database::isRegisteredChest)
    }

    /**
     * Check whether this proxy is a receiver chest.
     *
     * @see Database.isReceiverChest
     */
    fun isReceiver(database: Database): Boolean {
        return database.validator(Database::isReceiverChest)
    }

    /**
     * Check whether this proxy is a sender chest.
     *
     * @see Database.isSenderChest
     */
    fun isSender(database: Database): Boolean {
        return database.validator(Database::isSenderChest)
    }
}
