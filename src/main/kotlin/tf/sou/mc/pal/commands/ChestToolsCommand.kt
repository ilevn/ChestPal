package tf.sou.mc.pal.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import tf.sou.mc.pal.*
import tf.sou.mc.pal.utils.asSingleItem
import tf.sou.mc.pal.utils.asTextComponent

class ChestToolsCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("You need to invoke this command from a player account!")
            return false
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
