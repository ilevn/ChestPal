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
package tf.sou.mc.pal.json

import com.google.gson.*
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import org.bukkit.Location

/**
 * JSON Serializer for [Location] data.
 */
object LocationSerializer : JsonSerializer<Location>, JsonDeserializer<Location> {
    private val typeToken: Type = object : TypeToken<Map<String, Any>>() {}.type

    override fun serialize(
        src: Location,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return context.serialize(src.serialize(), typeToken)
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Location {
        val data = context.deserialize<Map<String, Any>>(json, typeToken)
        return Location.deserialize(data)
    }
}
