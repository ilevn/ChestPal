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
package tf.sou.mc.pal.commands

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import tf.sou.mc.pal.*
import tf.sou.mc.pal.utils.asSingleItem
import tf.sou.mc.pal.utils.asTextComponent

/**
 * Tool command for the [ChestPal][tf.sou.mc.pal.ChestPal] plugin.
 * This provides the player with a sender and a receiver hoe to register new chests.
 */
class ChestToolsCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) {
            sender.sendMessage("You need to invoke this command from a player account!")
            return false
        }

        if (!sender.hasPermission("chestpal.chesttools")) {
            sender.sendMessage("${ChatColor.RED}You do not have access to this command!")
            return true
        }

        createTools().toList().forEach { sender.inventory.addItem(it) }
        sender.sendMessage("[chestpal] I've given you the necessary tools.")
        return true
    }

    private fun createTools(): Pair<ItemStack, ItemStack> {
        val senderTool = SENDER_MATERIAL.asSingleItem()
        senderTool.editMeta {
            it.addEnchant(SENDER_ENCHANTMENT, -1, true)
            it.displayName(SENDER_HOE_NAME.asTextComponent())
        }

        val receiverTool = RECEIVER_MATERIAL.asSingleItem()
        receiverTool.editMeta {
            it.addEnchant(RECEIVER_ENCHANTMENT, -1, true)
            it.displayName(RECEIVER_HOE_NAME.asTextComponent())
        }

        return senderTool to receiverTool
    }
}
