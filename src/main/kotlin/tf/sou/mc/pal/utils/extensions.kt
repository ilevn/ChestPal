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
import org.bukkit.block.Container
import org.bukkit.entity.ItemFrame
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.ItemStack
import tf.sou.mc.pal.domain.ItemFrameResult

fun Material.asSingleItem() = ItemStack(this, 1)

fun String.asTextComponent() = Component.text(this)

fun Location.toVectorString(): String = toVector().toString()

fun PlayerEvent.reply(content: String) = player.sendMessage(content)

fun Location.findItemFrame(): ItemFrameResult {
    val possibleFrames = getNearbyEntitiesByType(ItemFrame::class.java, 3.0)
    if (possibleFrames.isEmpty()) {
        // oh oh.
        return ItemFrameResult.NoFrame
    }
    // TODO: Location check
    // what happens if a sender chest is adjacent to two+ other chests with item frames
    val firstFrame: ItemFrame = possibleFrames.toList()[0]
    if (firstFrame.location.block.getRelative(firstFrame.attachedFace).type == Material.CHEST) {
        return ItemFrameResult.Found(firstFrame)
    }

    return ItemFrameResult.NoItem
}

fun Location.resolveContainer(): Container? {
    return world.getBlockAt(this).state as? Container
}

fun Int.asItemStacks(material: Material): List<ItemStack> {
    val size = material.maxStackSize
    val fullStacks = this / size
    return (1..fullStacks).map { ItemStack(material, size) } + ItemStack(material, this % size)
}

fun Container.countAvailableSpace(item: Material): Int {
    val maxSize = item.maxStackSize
    return inventory.contents.sumOf { if (it == null) maxSize else maxSize - it.amount }
}
