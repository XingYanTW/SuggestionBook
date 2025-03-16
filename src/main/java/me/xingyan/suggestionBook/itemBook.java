package me.xingyan.suggestionBook;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class itemBook {

    public static ItemStack getBook() {
        ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.displayName(Component.text("Suggestion Book").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
        book.setItemMeta(meta);
        book = NBTEditor.set(book, true, NBTEditor.CUSTOM_DATA, "isSuggestionBook");
        return book;
    }

}
