package ne.fnfal113.fnamplifications.gems;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedPotionEffectType;

import ne.fnfal113.fnamplifications.gems.abstracts.AbstractGem;
import ne.fnfal113.fnamplifications.gems.handlers.GemUpgrade;
import ne.fnfal113.fnamplifications.gems.handlers.OnDamageHandler;
import ne.fnfal113.fnamplifications.utils.Utils;
import ne.fnfal113.fnamplifications.utils.WeaponArmorEnum;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.concurrent.ThreadLocalRandom;

public class SedateGem extends AbstractGem implements OnDamageHandler, GemUpgrade {

    public SedateGem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, 14);
    }

    @Override
    public void onDrag(Player player, SlimefunItem slimefunGemItem, ItemStack gemItem, ItemStack itemStackToSocket) {
        if (WeaponArmorEnum.SWORDS.isTagged(itemStackToSocket.getType()) || WeaponArmorEnum.AXES.isTagged(itemStackToSocket.getType())) {
            if(isUpgradeGem(gemItem, this.getId())) {
                upgradeGem(slimefunGemItem, itemStackToSocket, gemItem, player);
            } else {
                bindGem(slimefunGemItem, itemStackToSocket, player);
            }
        } else {
            Utils.sendMessage("Invalid item to socket! Gem works on swords and axes only", player);
        }
    }

    @Override
    public void onDamage(EntityDamageByEntityEvent event, ItemStack itemStack) {
        if(!(event.getEntity() instanceof LivingEntity)) return;

        if(!(event.getDamager() instanceof Player)) return;

        if(event.isCancelled()) return;

        Player player = (Player) event.getDamager();

        LivingEntity livingEntity = (LivingEntity) event.getEntity();

        int tier = getTier(itemStack, this.getId());

        if(ThreadLocalRandom.current().nextInt(100) < getChance() / tier) {
            int level = Math.abs(tier - 4);

            livingEntity.addPotionEffect(new PotionEffect(VersionedPotionEffectType.SLOWNESS, 120, level));
            
            sendGemMessage(player, this.getItemName());
        }

    }

}