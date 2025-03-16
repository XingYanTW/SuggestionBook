package me.xingyan.suggestionBook;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public final class Components {
    public static Component mm(String miniMessageString) { // mm, short for MiniMessage
        return MiniMessage.miniMessage().deserialize(miniMessageString);
    }

    public static String getStringFromComponent(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }
}