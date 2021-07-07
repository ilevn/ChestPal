package tf.sou.mc.pal

import org.bukkit.plugin.java.JavaPlugin
import tf.sou.mc.pal.commands.BaseCommand
import tf.sou.mc.pal.listeners.ChestListener

/**
 * Main plugin entry point.
 */
class ChestPal : JavaPlugin() {
    private val conf by lazy { config }

    private fun consoleMessage(content: String) = server.consoleSender.sendMessage(content)

    override fun onEnable() {
        if (conf["enabled"] == false) {
            consoleMessage("Chest Pal is disabled!")
            return
        }

        server.pluginManager.registerEvents(ChestListener(this), this)
        getCommand("chestpal")?.setExecutor(BaseCommand())
    }
}
