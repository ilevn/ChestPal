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

import org.bukkit.entity.ItemFrame

/**
 * Result class for an [ItemFrame] search result.
 */
sealed class ItemFrameResult {
    /**
     * Returned when an existing [ItemFrame] is missing an item.
     */
    object NoItem : ItemFrameResult()

    /**
     * Returned when no [ItemFrame] was found.
     */
    object NoFrame : ItemFrameResult()

    /**
     * Returned when an [ItemFrame] with an item was found.
     */
    data class Found(val frame: ItemFrame) : ItemFrameResult()
}
