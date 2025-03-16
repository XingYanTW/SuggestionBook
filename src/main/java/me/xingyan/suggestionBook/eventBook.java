package me.xingyan.suggestionBook;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.EventListener;

import static me.xingyan.suggestionBook.Components.getStringFromComponent;

public class eventBook implements Listener {

    @EventHandler
    public void onBookWritened(PlayerEditBookEvent event) throws SQLException {
        Player player = event.getPlayer();
        ItemStack book = player.getInventory().getItemInMainHand();
        if(event.isSigning() && NBTEditor.contains(book, NBTEditor.CUSTOM_DATA, "isSuggestionBook")) {
            Bukkit.broadcast(Component.text(event.getNewBookMeta().pages().size()));
            String content = "";
            for(int i = 1; i <= event.getNewBookMeta().pages().size(); i++) {
                Bukkit.broadcast(event.getNewBookMeta().page(i));
                content += getStringFromComponent(event.getNewBookMeta().page(i)) +"\n";
            }
            try {
                Database.getConnection().createStatement().execute("INSERT INTO suggestions (player, title, suggestion) VALUES ('"+player.getName()+"', '"+event.getNewBookMeta().getTitle()+"', '"+content+"')");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
