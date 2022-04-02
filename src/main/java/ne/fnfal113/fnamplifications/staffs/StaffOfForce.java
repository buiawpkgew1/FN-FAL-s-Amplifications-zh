package ne.fnfal113.fnamplifications.staffs;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import ne.fnfal113.fnamplifications.FNAmplifications;
import ne.fnfal113.fnamplifications.staffs.abstracts.AbstractStaff;
import ne.fnfal113.fnamplifications.staffs.implementations.AreaOfEffectStaffTask;
import ne.fnfal113.fnamplifications.staffs.implementations.MainStaff;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;

public class StaffOfForce extends AbstractStaff {

    private final NamespacedKey defaultUsageKey;

    private final MainStaff mainStaff;

    public StaffOfForce(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, 10);

        this.defaultUsageKey = new NamespacedKey(FNAmplifications.getInstance(), "forcestaff");
        this.mainStaff = new MainStaff(getStorageKey(), this.getId());
    }

    protected @Nonnull
    NamespacedKey getStorageKey() {
        return defaultUsageKey;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onClick(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        Block block = event.getPlayer().getTargetBlockExact(50);

        if(block == null || item.getType() == Material.AIR){
            return;
        }

        if (!Slimefun.getProtectionManager().hasPermission(
                Bukkit.getOfflinePlayer(player.getUniqueId()),
                block,
                Interaction.BREAK_BLOCK)
        ) {
            player.sendMessage(ChatColor.DARK_RED + "You don't have permission to cast force cloud there!");
            return;
        }

        ItemMeta meta = item.getItemMeta();

        mainStaff.updateMeta(item, meta, player);

        Particle particle = Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17) ? Particle.ELECTRIC_SPARK : Particle.SNEEZE;

        if(!player.isSneaking()) {
            AreaOfEffectStaffTask cloudStaff = new AreaOfEffectStaffTask(player, block, "FN_FORCE", 2.85F, 160, particle, null);
            cloudStaff.spawnCloud();
            player.sendMessage(ChatColor.GREEN + "You spawned a cloud effect with forward force");
        } else {
            AreaOfEffectStaffTask cloudStaff = new AreaOfEffectStaffTask(player, block, "FN_BACKWARD_FORCE", 2.85F, 160, Particle.END_ROD, null);
            cloudStaff.spawnCloud();
            player.sendMessage(ChatColor.RED  + "You spawned a cloud effect with backward force");
        }

    }

}