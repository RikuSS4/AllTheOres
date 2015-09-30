package com.rikuss4.alltheores.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.rikuss4.alltheores.blocks.ATOBlock;

public class ATOBlockItem extends ItemBlock {
    private ATOBlock block;

    public ATOBlockItem(Block block) {
        super(block);

        setBlock((ATOBlock) block);
    }

    public ATOBlock getBlock() {
        return block;
    }

    public void setBlock(ATOBlock block) {
        this.block = block;
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
  		String name = new ItemStack(getBlock().getOre()).getDisplayName(); 
  		name = name.substring(0, (name.lastIndexOf(StatCollector.translateToLocal("AllTheOres.oreSubstite").trim().replaceFirst("ORENAME", ""))));
        return (StatCollector.translateToLocal("AllTheOres.blockSubstite")).replaceFirst("ORENAME", name);
    }
}
