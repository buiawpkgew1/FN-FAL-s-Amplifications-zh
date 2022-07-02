package ne.fnfal113.fnamplifications.tools.implementation;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import ne.fnfal113.fnamplifications.utils.Utils;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.Player;

public class BlockRotatorTask {

    public BlockRotatorTask () {}

    public void onRightClick(Block block, Player player){
        if(!Slimefun.getProtectionManager().hasPermission(Bukkit.getOfflinePlayer(player.getUniqueId()),
                player.getLocation(), Interaction.BREAK_BLOCK)){
            player.sendMessage(Utils.colorTranslator("&c&l[FNAmpli" + "&b&lfications] > " + "&e你没有权限旋转这个方块!"));
            return;
        }
        BlockData blockData = block.getBlockData();

        if(player.isSneaking()){
            if(Tag.DOORS.isTagged(block.getType()) || Tag.TALL_FLOWERS.isTagged(block.getType()) ||
                    Tag.FLOWERS.isTagged(block.getType())){
                return;
            }

            if(blockData instanceof Orientable) {
                flip((Orientable) blockData, block);
            }
            if(blockData instanceof Bisected){
                flip((Bisected) blockData, block);
            }
            if(blockData instanceof Slab){
                flip((Slab) blockData, block);
            }
        } else {
            if(Tag.BEDS.isTagged(block.getType())){
                return;
            }

            if(blockData instanceof Orientable) {
                rotate((Orientable) blockData, block);
            }
            if(blockData instanceof Directional){
                rotate((Directional) blockData, block);
            }
        }
    }

    private void flip(Slab slab, Block block){
        switch (slab.getType()){
            case TOP:
                slab.setType(Slab.Type.BOTTOM);
                break;
            case BOTTOM:
                slab.setType(Slab.Type.TOP);
                break;
        }
        block.setBlockData(slab.clone());
    }

    private void flip(Bisected bisected, Block block){
        switch (bisected.getHalf()){
            case TOP:
                bisected.setHalf(Bisected.Half.BOTTOM);
                break;
            case BOTTOM:
                bisected.setHalf(Bisected.Half.TOP);
                break;
        }
        block.setBlockData(bisected.clone());
    }

    private void flip(Orientable orientable, Block block){
        switch (orientable.getAxis()) {
            case X:
            case Z:
                orientable.setAxis(Axis.Y);
                break;
            case Y:
                orientable.setAxis(Axis.X);
                break;
        }
        block.setBlockData(orientable.clone());
    }

    private void rotate(Orientable orientable, Block block){
        switch (orientable.getAxis()) {
            case X:
                orientable.setAxis(Axis.Z);
                break;
            case Z:
                orientable.setAxis(Axis.X);
                break;
        }
        block.setBlockData(orientable.clone());
    }

    private void rotate(Directional directional, Block block){
        switch (directional.getFacing()) {
            case NORTH:
                directional.setFacing(BlockFace.EAST);
                break;
            case EAST:
                directional.setFacing(BlockFace.SOUTH);
                break;
            case SOUTH:
                directional.setFacing(BlockFace.WEST);
                break;
            case WEST:
                directional.setFacing(BlockFace.NORTH);
                break;
        }
        block.setBlockData(directional.clone());
    }

}
