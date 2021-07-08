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
package tf.sou.mc.pal

import kotlin.properties.Delegates
import org.bukkit.plugin.java.JavaPlugin
import tf.sou.mc.pal.commands.BaseCommand
import tf.sou.mc.pal.commands.ChestToolsCommand
import tf.sou.mc.pal.listeners.ChestListener
import tf.sou.mc.pal.persistence.JsonDatabase

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

        saveDefaultConfig()
        database = JsonDatabase(dataFolder)
        server.pluginManager.registerEvents(ChestListener(this), this)
        getCommand("chestpal")?.setExecutor(BaseCommand())
        getCommand("chesttools")?.setExecutor(ChestToolsCommand())
    }
}
