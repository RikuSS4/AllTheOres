package com.rikuss4.alltheores.items;

import net.minecraft.item.Item;

import com.rikuss4.alltheores.reference.Reference;

public class ATOLogo extends Item {
	public ATOLogo() {
		setUnlocalizedName("Logo");
		setTextureName(Reference.MOD_ID + ":" + "Logo");
		//setCreativeTab(CreativeTabs.tabAllSearch);
		//setCreativeTab(ATOCreativeTab.ITEMS_TAB);
	}
}