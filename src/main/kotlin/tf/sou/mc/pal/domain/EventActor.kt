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
package tf.sou.mc.pal.domain

import org.bukkit.event.Event
import tf.sou.mc.pal.ChestPal

/**
 * Actor interface for [events][Event].
 */
interface EventActor<E : Event> {
    /**
     * Respond to an [event][E] with an action.
     * This has access to the [ChestPal] plugin state.
     */
    fun act(event: E, pal: ChestPal)
}
