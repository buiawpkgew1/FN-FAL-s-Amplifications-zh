package ne.fnfal113.fnamplifications.quivers.listener;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import ne.fnfal113.fnamplifications.quivers.abstracts.AbstractQuiver;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class QuiverListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        ItemStack itemInOffHand = player.getInventory().getItemInOffHand();

        int playerInvLength = player.getInventory().getContents().length;

        boolean rightClickAction = (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK);
        boolean leftClickAction = (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK);

        if(rightClickAction) {
            // crossbow consumes the quiver when state is open (arrow type), change type to leather right away
            if (itemInMainHand.getType() == Material.CROSSBOW) {
                for (int i = 0; i < playerInvLength; i++) {
                    ItemStack itemStack = player.getInventory().getItem(i);
                    SlimefunItem sfItem = SlimefunItem.getByItem(itemStack);

                    if(sfItem instanceof AbstractQuiver && itemStack.getType() == ((AbstractQuiver) sfItem).getArrowType().getType()) {
                        AbstractQuiver abstractQuiver = (AbstractQuiver) sfItem;

                        abstractQuiver.getQuiverTask().changeState(itemStack, itemStack.getItemMeta());
                    }
                }
            } 

            // deposit arrows if arrow equipped in main-hand
            if(itemInMainHand.getType() == Material.ARROW || itemInMainHand.getType() == Material.SPECTRAL_ARROW) {
                if(itemInOffHand.getType() == Material.BOW || itemInOffHand.getType() == Material.CROSSBOW) {
                    return;
                } // prevent arrows being deposited when shooting bow in off-hand

                for (int i = 0; i < playerInvLength; i++) {
                    // retrieve the first quiver item in player inventory then deposit arrows equipped in main-hand
                    ItemStack itemStack = player.getInventory().getItem(i);
                    SlimefunItem sfItem = SlimefunItem.getByItem(itemStack);
                    
                    if(sfItem instanceof AbstractQuiver) {
                        AbstractQuiver abstractQuiver = (AbstractQuiver) sfItem;

                        abstractQuiver.getQuiverTask().depositArrows(itemStack, itemStack.getItemMeta(), player);
                    }
                } 
            }

        }

        if(rightClickAction || leftClickAction) {
            // withdraw arrows or change quiver state if quiver equipped in main-hand
            SlimefunItem sfItem = SlimefunItem.getByItem(itemInMainHand);

            if(sfItem instanceof AbstractQuiver) {
                AbstractQuiver abstractQuiver = (AbstractQuiver) sfItem;

                if(player.isSneaking()) {
                    abstractQuiver.getQuiverTask().withdrawArrows(itemInMainHand, itemInMainHand.getItemMeta(), event.getPlayer());
                } else {
                    abstractQuiver.getQuiverTask().changeState(itemInMainHand, itemInMainHand.getItemMeta());
                }
            }

        }

    }

    @EventHandler
    public void onEntityBowShoot(EntityShootBowEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        if(player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        Material quiverMaterial = Material.matchMaterial(event.getProjectile().getType().toString());

        if(quiverMaterial == null) {
            return;
        }

        Inventory playerInven = player.getInventory();
        
        // get first quiver item slot
        int slot = playerInven.first(quiverMaterial);

        if(slot == -1) {
            return;
        }

        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        ItemStack itemInOffHand = player.getInventory().getItemInOffHand();

        ItemStack quiverItemStack = isItemInHandQuiver(itemInMainHand) ? itemInMainHand :
            isItemInHandQuiver(itemInOffHand) ? itemInOffHand : playerInven.getItem(slot);

        if(quiverItemStack == null) {
            return;
        }

        SlimefunItem sfItem = SlimefunItem.getByItem(quiverItemStack);

        if(sfItem instanceof AbstractQuiver) {
            ((AbstractQuiver) sfItem).getQuiverTask().bowShoot(event, quiverItemStack, quiverItemStack.getType() == Material.ARROW);
        }

    }

    public boolean isItemInHandQuiver(ItemStack itemStack) {
        return SlimefunItem.getByItem(itemStack) instanceof AbstractQuiver && itemStack.getType() != Material.LEATHER;
    }

}
