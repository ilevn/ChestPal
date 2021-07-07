package tf.sou.mc.pal.commands

import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * The base command of the [ChestPal][tf.sou.mc.pal.ChestPal] plugin.
 */
class BaseCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("You need to invoke this command from a player account!")
            return false
        }

        val dirt = ItemStack(Material.DIRT, 1)
        sender.inventory.addItem(dirt)
        sender.sendMessage("Here is some dirt!")
        return true
    }
}
