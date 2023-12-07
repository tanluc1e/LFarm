package me.tanluc.starfarm.event;

import me.tanluc.starfarm.StarsFarm;
import me.tanluc.starfarm.data.User;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PickUp implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPickUp(EntityPickupItemEvent e) {
        Player player = (Player) e.getEntity();
        User user = User.of(player);
        if (StarsFarm.players.contains(player)) {
            Item item = e.getItem();
            ItemStack itemStack = item.getItemStack();

            if (StarsFarm.materials.contains(itemStack.getType())) {
                if (isVanillaItem(itemStack)) {
                    String type = itemStack.getType().name();
                    user.setHave(type, user.getHave(type) + itemStack.getAmount());

                    item.remove();
                    e.setCancelled(true);
                }

            }
        }
    }

    private boolean isVanillaItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasDisplayName() || itemMeta.hasLore() || !itemStack.getEnchantments().isEmpty()) {
            return false;
        }
        return true;
    }
}
