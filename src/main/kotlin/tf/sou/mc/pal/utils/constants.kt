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

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

val SENDER_MATERIAL = Material.WOODEN_HOE
val RECEIVER_MATERIAL = Material.GOLDEN_HOE

val SENDER_ENCHANTMENT: Enchantment = Enchantment.ARROW_FIRE
val RECEIVER_ENCHANTMENT: Enchantment = Enchantment.ARROW_DAMAGE

val SENDER_HOE_NAME = "${ChatColor.DARK_RED}sender hoe"
val RECEIVER_HOE_NAME = "${ChatColor.DARK_GREEN}receiver hoe"
