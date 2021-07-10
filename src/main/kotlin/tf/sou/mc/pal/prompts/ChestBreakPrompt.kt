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
package tf.sou.mc.pal.prompts

import org.bukkit.block.Block
import org.bukkit.conversations.BooleanPrompt
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import tf.sou.mc.pal.ChestPal

/**
 * Prompt for [org.bukkit.event.block.BlockBreakEvent].
 * This takes care of deciding whether a chest should be deleted from the system or not.
 */
class ChestBreakPrompt : BooleanPrompt() {
    override fun getPromptText(context: ConversationContext): String {
        return "Do you want to remove this chest from the system?"
    }

    override fun acceptValidatedInput(context: ConversationContext, input: Boolean): Prompt? {
        if (!input) {
            // Deny breaking the block.
            return END_OF_CONVERSATION
        }
        val block = context.getSessionData("block") as Block
        val db = (context.plugin as ChestPal).database
        block.run { db.removeLocation(location); breakNaturally() }
        return END_OF_CONVERSATION
    }
}
