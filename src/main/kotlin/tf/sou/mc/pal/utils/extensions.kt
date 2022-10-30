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
package tf.sou.mc.pal.utils

import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Material.CHEST
import org.bukkit.block.Container
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import tf.sou.mc.pal.domain.ChestInventoryProxy
import tf.sou.mc.pal.domain.ItemFrameResult

/**
 * Transforms this material to an [ItemStack] with a single block.
 */
fun Material.asSingleItem() = ItemStack(this, 1)

/**
 * Transforms this string to a [TextComponent][net.kyori.adventure.text.TextComponent].
 */
fun String.asTextComponent() = Component.text(this)

/**
 * Transforms this location to a vectorised string.
 */
fun Location.toVectorString(): String = toVector().toString()

/**
 * Convenience function for [org.bukkit.entity.Player.sendMessage] for an event.
 */
fun PlayerEvent.reply(content: String) = player.sendMessage(content)

/**
 * Attempts to find the appropriate [ItemFrame] for a chest location.
 *
 * _Note: This performs an expensive distance calculation to find the correct chest._
 * @see ItemFrameResult
 */
fun Location.findItemFrame(): ItemFrameResult {
    val box = block.boundingBox.expand(0.1)
    val frames = world.getNearbyEntities(box).filterIsInstance<ItemFrame>()
    if (frames.isEmpty()) {
        // oh oh.
        return ItemFrameResult.NoFrame
    }
    val frame = frames
        .minByOrNull { it.location.toBlockLocation().distance(this) }
        ?: error("This should never happen")

    if (frame.location.block.getRelative(frame.attachedFace).type == CHEST &&
        !frame.item.type.isEmpty
    ) {
        return ItemFrameResult.Found(frame)
    }

    return ItemFrameResult.NoItem
}

/**
 * Resolves this location to a nullable [Container].
 */
fun Location.resolveContainer(): Container? {
    return world.getBlockAt(this).state as? Container
}

/**
 * Partitions this integer into a list of item stacks based
 * on the maximum stack size of the provided [Material].
 */
fun Int.asItemStacks(material: ItemStack): List<ItemStack> {
    val size = material.maxStackSize
    val chunks = (1..this / size).map { material.amount = size ; material}
    return (this % size).takeIf { it != 0 }?.let {material.amount=it ; chunks + material } ?: chunks
}

/**
 * Counts the amount of available space in an inventory.
 * This is based on the maximum stack size of the provided [Material].
 */
fun Inventory.countAvailableSpace(item: Material): Int {
    val maxSize = item.maxStackSize
    return contents.sumOf { if (it == null) maxSize else maxSize - it.amount }
}

/**
 * Attempts to find items in an inventory that differ from the provided [Material].
 */
fun Inventory.findBadItems(allowed: Material): List<ItemStack> {
    val filteredInventory = mutableListOf<ItemStack>()
    for (item in contents) {
        if (item != null && item.type != allowed) filteredInventory.add(item)
    }
    return filteredInventory
}

/**
 * Transforms an enum uppercase name into its title case version.
 */
fun Enum<*>.toPrettyString(): String {
    val split = name.lowercase().split("_")
    return split.joinToString(" ") { it.replaceFirstChar(Char::uppercase) }
}

/**
 * Converts this inventory to a proxied, double-chest aware version.
 */
fun Inventory.toChestInventoryProxy() = ChestInventoryProxy(this)

/**
 * Sends a player message in red.
 */
fun Player.redMessage(content: String) = sendMessage("${ChatColor.RED}$content")
