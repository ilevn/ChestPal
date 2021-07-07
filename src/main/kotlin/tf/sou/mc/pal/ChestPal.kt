package tf.sou.mc.pal

import org.bukkit.plugin.java.JavaPlugin
import tf.sou.mc.pal.commands.BaseCommand
import tf.sou.mc.pal.commands.ChestToolsCommand
import tf.sou.mc.pal.listeners.ChestListener
import tf.sou.mc.pal.persistence.JsonDatabase
import kotlin.properties.Delegates

/**
 * Main plugin entry point.
 */
class ChestPal : JavaPlugin() {
    private val conf by lazy { config }
    var database by Delegates.notNull<JsonDatabase>()

    fun consoleMessage(content: String) = server.consoleSender.sendMessage(content)

    override fun onEnable() {
        if (conf["enabled"] == false) {
            consoleMessage("Chest Pal is disabled!")
            return
        }

        database = JsonDatabase(dataFolder)
        server.pluginManager.registerEvents(ChestListener(this), this)
        getCommand("chestpal")?.setExecutor(BaseCommand())
        getCommand("chesttools")?.setExecutor(ChestToolsCommand())
    }
}
