package tf.sou.mc.pal.domain

import org.bukkit.event.Event
import tf.sou.mc.pal.ChestPal

interface EventActor<E : Event> {
    fun act(event: E, pal: ChestPal)
}
