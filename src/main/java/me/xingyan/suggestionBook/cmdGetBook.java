package me.xingyan.suggestionBook;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static me.xingyan.suggestionBook.Components.mm;

public class cmdGetBook implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            for (ItemStack itemStack : player.getInventory().getContents()) {
                if (itemStack != null && itemStack.getType().equals(Material.WRITABLE_BOOK)&&NBTEditor.contains(itemStack, NBTEditor.CUSTOM_DATA, "isSuggestionBook")) {
                    player.sendMessage(mm("<red> You already have a suggestion book."));
                    return true;
                }
            }
            player.getInventory().addItem(itemBook.getBook());
            commandSender.sendMessage(mm("<green> You have received a suggestion book."));
        }else{
            commandSender.sendMessage(mm("<red> You must be a player to use this command."));
        }
        return true;
    }
}
