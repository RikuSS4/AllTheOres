package com.rikuss4.alltheores.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.rikuss4.alltheores.blocks.ATOBrick;

public class ATOBrickItem extends ItemBlock {
    private ATOBrick brick;

    public ATOBrickItem(Block block) {
        super(block);

        setBlock((ATOBrick) block);
    }

    public ATOBrick getBrick() {
        return brick;
    }

    public void setBlock(ATOBrick brick) {
        this.brick = brick;
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
  		String name = new ItemStack(getBrick().getOre()).getDisplayName(); 
  		name = name.substring(0, (name.lastIndexOf(StatCollector.translateToLocal("AllTheOres.oreSubstite").trim().replaceFirst("ORENAME", ""))));
        return (StatCollector.translateToLocal("AllTheOres.brickSubstite")).replaceFirst("ORENAME", name);
    }
}
