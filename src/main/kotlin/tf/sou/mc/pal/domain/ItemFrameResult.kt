package tf.sou.mc.pal.domain

import org.bukkit.entity.ItemFrame

sealed class ItemFrameResult {
    object NoItem : ItemFrameResult()
    object NoFrame : ItemFrameResult()
    data class Found(val frame: ItemFrame) : ItemFrameResult()
}
