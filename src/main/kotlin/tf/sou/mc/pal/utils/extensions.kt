package tf.sou.mc.pal.utils

import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Container
import org.bukkit.entity.ItemFrame
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.ItemStack
import tf.sou.mc.pal.RECEIVER_ENCHANTMENT
import tf.sou.mc.pal.RECEIVER_MATERIAL
import tf.sou.mc.pal.SENDER_ENCHANTMENT
import tf.sou.mc.pal.SENDER_MATERIAL
import tf.sou.mc.pal.domain.ItemFrameResult

fun ItemStack.isSenderTool(): Boolean = getEnchantmentLevel(SENDER_ENCHANTMENT) == -1 && type == SENDER_MATERIAL
fun ItemStack.isReceiverTool(): Boolean = getEnchantmentLevel(RECEIVER_ENCHANTMENT) == -1 && type == RECEIVER_MATERIAL

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
    val firstFrame: ItemFrame = possibleFrames.toList()[0]
    if (firstFrame.location.block.getRelative(firstFrame.attachedFace).type == Material.CHEST) {
        return ItemFrameResult.Found(firstFrame)
    }

    return ItemFrameResult.NoItem
}

fun Location.resolveContainer(): Container {
    return world.getBlockAt(this).state as Container
}


fun Int.asItemStacks(material: Material): List<ItemStack> {
    val fullStacks = this / 64
    return (1..fullStacks).map { ItemStack(material, 64) } + ItemStack(material, this % 64)
}

fun Container.countAvailableSpace(item: Material): Int {
    val maxSize = item.maxStackSize
    return inventory.contents.sumOf { if (it == null) maxSize else maxSize - it.amount }
}



