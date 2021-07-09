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
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Material.AIR
import org.bukkit.block.Container
import org.bukkit.entity.ItemFrame
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
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
 * @see ItemFrameResult
 * _Note: This performs an expensive distance calculation to find the correct chest._
 */
fun Location.findItemFrame(): ItemFrameResult {
    val possibleFrames = getNearbyEntitiesByType(ItemFrame::class.java, 2.0)
    if (possibleFrames.isEmpty()) {
        // oh oh.
        return ItemFrameResult.NoFrame
    }
    val frames = possibleFrames.toList()
    val frame = frames
        .minByOrNull { it.location.toBlockLocation().distance(this) }
        ?: error("This should never happen")

    if (frame.location.block.getRelative(frame.attachedFace).type == CHEST &&
        frame.item.type != AIR
    ) {
        return ItemFrameResult.Found(frame)
    }

    return ItemFrameResult.NoItem
}

/**
 * Resolves this location to a nullable [Container].
 */
fun Location.resolveContainer(): Container? {
    // TODO: Account for double chests
    return world.getBlockAt(this).state as? Container
}

/**
 * Partitions this integer into a list of item stacks based
 * on the maximum stack size of the provided [Material].
 */
fun Int.asItemStacks(material: Material): List<ItemStack> {
    val size = material.maxStackSize
    val fullStacks = this / size
    return (1..fullStacks).map { ItemStack(material, size) } + ItemStack(material, this % size)
}

/**
 * Counts the amount of available space in a container.
 * This is based on the maximum stack size of the provided [Material].
 */
fun Container.countAvailableSpace(item: Material): Int {
    val maxSize = item.maxStackSize
    return inventory.contents.sumOf { if (it == null) maxSize else maxSize - it.amount }
}

/**
 * Attempts to find items in an inventory that differ from the provided [Material].
 */
fun Inventory.findBadItems(allowed: Material): List<ItemStack> {
    return contents.filter { it != null && it.type != allowed }
}
