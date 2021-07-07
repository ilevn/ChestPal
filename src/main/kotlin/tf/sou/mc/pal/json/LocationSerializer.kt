package tf.sou.mc.pal.json

import com.google.gson.*
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import org.bukkit.Location
import java.lang.reflect.Type

/**
 * JSON Serializer for [Location] data.
 */
object LocationSerializer : JsonSerializer<Location>, JsonDeserializer<Location> {
    private val typeToken: Type = object : TypeToken<Map<String, Any>>() {}.type

    override fun serialize(src: Location, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return context.serialize(src.serialize(), typeToken)
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Location {
        val data = context.deserialize<Map<String, Any>>(json, typeToken)
        return Location.deserialize(data)
    }
}
